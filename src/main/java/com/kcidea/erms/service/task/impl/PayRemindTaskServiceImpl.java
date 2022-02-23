package com.kcidea.erms.service.task.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.util.DateTimeUtil;
import com.kcidea.erms.common.util.MailUtil;
import com.kcidea.erms.dao.fund.ContractDao;
import com.kcidea.erms.dao.fund.PayRemindDao;
import com.kcidea.erms.dao.fund.PayRemindRelDao;
import com.kcidea.erms.dao.message.MessageDao;
import com.kcidea.erms.dao.user.UserDao;
import com.kcidea.erms.domain.fund.Contract;
import com.kcidea.erms.domain.fund.PayRemind;
import com.kcidea.erms.domain.message.Message;
import com.kcidea.erms.domain.user.User;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import com.kcidea.erms.enums.fund.EnumPayRemindType;
import com.kcidea.erms.enums.message.EnumMessageType;
import com.kcidea.erms.model.common.MailModel;
import com.kcidea.erms.service.task.PayRemindTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/25
 **/
@Slf4j
@Service
public class PayRemindTaskServiceImpl implements PayRemindTaskService {

    @Resource
    private ContractDao contractDao;

    @Resource
    private PayRemindDao payRemindDao;

    @Resource
    private UserDao userDao;

    @Resource
    private PayRemindRelDao payRemindRelDao;

    @Resource
    private MessageDao messageDao;

    @Resource
    private MailUtil mailUtil;

    /**
     * 执行定时提醒付款任务
     *
     * @author yeweiwei
     * @date 2021/11/25 9:20
     */
    @Override
    public void executePayRemindTask() {
        //查询所有待支付的合同，支付状态为未支付、已支付部分
        List<Contract> contractList = contractDao.findUnPaidList();

        Map<Long, List<Contract>> scContractMap = contractList.stream().collect(Collectors.groupingBy(Contract::getSId));

        for (Map.Entry<Long, List<Contract>> entry : scContractMap.entrySet()) {
            Long sid = entry.getKey();
            List<Contract> scContractList = entry.getValue();
            if (CollectionUtils.isEmpty(scContractList)) {
                continue;
            }

            //查询学校设置的付款到期提醒
            PayRemind payRemind = payRemindDao.findOneBySid(sid);
            if (null == payRemind) {
                continue;
            }
            Integer remindType = payRemind.getType();
            Integer remindDay = payRemind.getRemindDay();

            //查询提醒人
            Set<Long> userIdSet = payRemindRelDao.findUserIdSetBySidDisableFlag(sid, EnumTrueFalse.否.getValue());
            //查询用户信息
            List<User> userList = userDao.findListBySidDisableFlag(sid, EnumTrueFalse.否.getValue());
            Map<Long, User> userInfoMap = userList.stream().collect(Collectors.toMap(User::getId, a -> a));

            for (Contract contract : scContractList) {
                //付款截止日期
                LocalDate payEndDay = contract.getPayEndDay();

                //今天
                LocalDate now = LocalDate.now();

                //开始提醒的日期
                LocalDate startRemindDay = payEndDay.minusDays(remindDay);

                if (!now.isBefore(startRemindDay) && !now.isAfter(payEndDay)) {
                    sendRemindMessageOrEmail(remindType, contract, userIdSet, userInfoMap, sid);
                }
            }
        }
    }

    /**
     * 发送邮件或者站内信提醒
     *
     * @param remindType  提醒方式
     * @param contract    合同信息
     * @param userIdSet   提醒的用户id集合
     * @param userInfoMap 用户信息集合
     * @param sid         学校id
     * @author yeweiwei
     * @date 2021/11/17 15:12
     */
    private void sendRemindMessageOrEmail(Integer remindType, Contract contract, Set<Long> userIdSet,
                                          Map<Long, User> userInfoMap, Long sid) {
        EnumPayRemindType enumRemindType = EnumPayRemindType.getEnumPayRemindType(remindType);
        if (null == enumRemindType) {
            return;
        }

        String title = "合同付款到期提醒";
        String contractName = contract.getName();
        LocalDate payEndDay = contract.getPayEndDay();
        int days = DateTimeUtil.getDayDuration(LocalDate.now(), payEndDay);

        switch (enumRemindType) {
            case 邮箱:
                for (Long userId : userIdSet) {
                    User user = userInfoMap.get(userId);
                    if (null == user || Strings.isNullOrEmpty(user.getEmail())) {
                        continue;
                    }
                    MailModel mailModel = new MailModel();
                    mailModel.setToMail(user.getEmail());
                    mailModel.setTemplate(Constant.Template.PAY_REMIND_EMAIL);
                    mailModel.setTitle(title);
                    //参数
                    Map<String, Object> paramsMap = Maps.newHashMap();
                    paramsMap.put(Constant.Template.PAY_REMIND_CONTRACT_NAME, contractName);
                    paramsMap.put(Constant.Template.PAY_REMIND_END_DAY, payEndDay);
                    paramsMap.put(Constant.Template.PAY_REMIND_DAYS, days);
                    mailModel.setParamsMap(paramsMap);

                    mailUtil.sendEmail(mailModel);
                }
                break;
            case 站内信:
                LocalDateTime now = LocalDateTime.now();
                String content = "尊敬的ERMS用户，您好！您学校的【" + contractName + "】合同付款截止日期为："
                        + payEndDay.toString() + "，还有" + days + "天即将逾期，请尽快完成支付，避免逾期！";
                for (Long userId : userIdSet) {
                    Message message = new Message().create(sid, userId, title, EnumTrueFalse.否.getValue(),
                            content, "", EnumMessageType.支付到期提醒.getValue(), now);
                    messageDao.insert(message);
                }
                break;
            default:
                break;
        }
    }
}
