package com.kcidea.erms.service.fund.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.dao.fund.ContractPayDao;
import com.kcidea.erms.dao.fund.PayRemindDao;
import com.kcidea.erms.dao.fund.PayRemindRelDao;
import com.kcidea.erms.dao.user.UserDao;
import com.kcidea.erms.domain.fund.PayRemind;
import com.kcidea.erms.domain.fund.PayRemindRel;
import com.kcidea.erms.domain.user.User;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import com.kcidea.erms.enums.fund.EnumPayRemindType;
import com.kcidea.erms.model.fund.ContractPayListModel;
import com.kcidea.erms.model.fund.PayRemindInfoModel;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.fund.ContractPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/16
 **/
@Slf4j
@Service
public class ContractPayServiceImpl extends BaseService implements ContractPayService {

    @Resource
    private ContractPayDao contractPayDao;

    @Resource
    private PayRemindDao payRemindDao;

    @Resource
    private PayRemindRelDao payRemindRelDao;

    @Resource
    private UserDao userDao;


    /**
     * 分页查询合同支付记录
     *
     * @param sid           学校id
     * @param contractId    合同id
     * @param invoiceNumber 发票编号
     * @param pageNum       页码
     * @param pageSize      每页数量
     * @return 合同支付记录
     * @author yeweiwei
     * @date 2021/11/16 19:31
     */
    @Override
    public PageResult<ContractPayListModel> findContractPayList(Long sid, Long contractId, String invoiceNumber,
                                                                Integer pageNum, Integer pageSize) {
        super.checkPageParam(pageNum, pageSize);
        super.checkSid(sid);
        PageResult<ContractPayListModel> result = new PageResult<>();
        Page<ContractPayListModel> page = new Page<>(pageNum, pageSize);
        List<ContractPayListModel> list = contractPayDao.findPage(sid, contractId, invoiceNumber, page);
        return result.success(list, page.getTotal());
    }

    /**
     * 查询学校的到期提醒设置
     *
     * @param sid 学校id
     * @return 到期提醒设置
     * @author yeweiwei
     * @date 2021/11/17 9:43
     */
    @Override
    public PayRemindInfoModel findPayRemind(Long sid) {
        super.checkSid(sid);
        PayRemind payRemind = payRemindDao.findOneBySid(sid);

        PayRemindInfoModel payRemindInfoModel = new PayRemindInfoModel();

        if (null == payRemind) {
            return payRemindInfoModel;
        }
        Set<Long> userIdSet = payRemindRelDao.findUserIdSetBySidDisableFlag(sid, EnumTrueFalse.否.getValue());

        payRemindInfoModel.setRemindDay(payRemind.getRemindDay());
        payRemindInfoModel.setType(payRemind.getType());
        payRemindInfoModel.setUserIdList(Lists.newArrayList(userIdSet));
        return payRemindInfoModel;
    }

    /**
     * 设置学校的付款到期提醒
     *
     * @param sid           学校id
     * @param payRemindInfo 到期提醒model
     * @param userId        操作人id
     * @return 设置结果
     * @author yeweiwei
     * @date 2021/11/17 9:59
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updatePayRemind(Long sid, PayRemindInfoModel payRemindInfo, Long userId) {
        super.checkSid(sid);
        String msg;
        LocalDateTime now = LocalDateTime.now();

        Integer remindDay = payRemindInfo.getRemindDay();
        Integer type = payRemindInfo.getType();

        PayRemind payRemind = payRemindDao.findOneBySid(sid);
        if (null == payRemind) {
            //新增
            payRemind = new PayRemind().create(sid, remindDay, type, userId, now);
            payRemindDao.insert(payRemind);

            msg = Vm.INSERT_SUCCESS;
        } else {
            payRemind.setRemindDay(remindDay);
            payRemind.setType(type);
            payRemind.setUpdatedBy(userId);
            payRemind.setUpdatedTime(now);
            payRemindDao.updateById(payRemind);

            //删除原来的提醒用户
            payRemindRelDao.deleteBySid(sid);

            msg = Vm.UPDATE_SUCCESS;
        }

        //设置提醒的用户
        List<Long> userIdList = payRemindInfo.getUserIdList();

        //如果是邮箱提醒
        if (type == EnumPayRemindType.邮箱.getValue()) {
            //如果这些用户全都没有设置邮箱，报错
            List<User> userInfoList = userDao.findListBySidDisableFlag(sid, EnumTrueFalse.否.getValue());
            List<String> emailList = userInfoList.stream().filter(u -> userIdList.contains(u.getId())
                    && !Strings.isNullOrEmpty(u.getEmail())).map(User::getEmail).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(emailList)) {
                throw new CustomException("很抱歉，您选中的用户全都没有填写邮箱！");
            }
        }

        for (Long user : userIdList) {
            PayRemindRel payRemindRel = new PayRemindRel().create(sid, user, EnumTrueFalse.否.getValue(), userId, now);
            payRemindRelDao.insert(payRemindRel);
        }

        return msg;
    }
}
