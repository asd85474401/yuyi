package com.kcidea.erms.service.fund.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.util.ConvertUtil;
import com.kcidea.erms.common.util.DownloadUtil;
import com.kcidea.erms.dao.ers.VdatabaseDao;
import com.kcidea.erms.dao.fund.ContractDao;
import com.kcidea.erms.dao.fund.ContractDatabaseDao;
import com.kcidea.erms.dao.fund.ContractPayDao;
import com.kcidea.erms.domain.fund.ContractPay;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import com.kcidea.erms.enums.fund.EnumOrderType;
import com.kcidea.erms.enums.fund.EnumPayState;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.fund.ContractInfoModel;
import com.kcidea.erms.model.fund.ContractListModel;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.fund.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/16
 **/
@Slf4j
@Service
public class ContractServiceImpl extends BaseService implements ContractService {

    @Resource
    private ContractDatabaseDao contractDatabaseDao;

    @Resource
    private VdatabaseDao vdatabaseDao;

    @Resource
    private ContractDao contractDao;

    @Resource
    private ContractPayDao contractPayDao;

    @Value("${my-config.temp-path}")
    private String tempPath;

    /**
     * 获取带有合同的数据库下拉集合
     *
     * @param sid 学校id
     * @return 数据库下拉集合
     * @author yeweiwei
     * @date 2021/11/16 13:59
     */
    @Override
    public List<IdNameModel> findDatabaseSelectList(Long sid) {
        super.checkSid(sid);
        List<IdNameModel> list = Lists.newArrayList(new IdNameModel().create(Constant.ALL_LONG_VALUE, "全部"));
        Set<Long> didSet = contractDatabaseDao.findDidSetBySidContractId(sid, null);
        //查询数据库名称
        List<IdNameModel> didNameList = vdatabaseDao.findDidNameListBySid(sid);
        list.addAll(didNameList.stream().filter(s -> didSet.contains(s.getId())).collect(Collectors.toList()));
        return list;
    }

    /**
     * 获取订购类型下拉集合
     *
     * @return 订购类型下拉集合
     * @author yeweiwei
     * @date 2021/11/16 14:11
     */
    @Override
    public List<IdNameModel> findOrderTypeSelectList() {
        return EnumOrderType.findOrderTypeSelectList();
    }

    /**
     * 获取支付状态下拉集合
     *
     * @return 支付状态下拉集合
     * @author yeweiwei
     * @date 2021/11/16 14:16
     */
    @Override
    public List<IdNameModel> findPayStateSelectList() {
        return EnumPayState.findPayStateSelectList();
    }

    /**
     * 分页查询合同
     *
     * @param sid         学校id
     * @param name        合同名称
     * @param number      合同编号
     * @param did         数据库id
     * @param payStartDay 开始付款日期
     * @param payEndDay   截止付款日期
     * @param budgetId    预算id
     * @param vYear       年份
     * @param orderType   订购类型
     * @param payStates   支付状态
     * @param pageNum     页码
     * @param pageSize    每页数量
     * @return 分页数据
     * @author yeweiwei
     * @date 2021/11/16 15:24
     */
    @Override
    public PageResult<ContractListModel> findContractList(Long sid, String name, String number, Long did,
                                                          String payStartDay, String payEndDay, Long budgetId,
                                                          Integer vYear, Integer orderType, String payStates,
                                                          Integer pageNum, Integer pageSize) {
        super.checkPageParam(pageNum, pageSize);
        super.checkSid(sid);
        PageResult<ContractListModel> result = new PageResult<>();
        Page<ContractListModel> page = new Page<>(pageNum, pageSize);

        List<Integer> payStateList = Arrays.stream(payStates.split(Constant.SplitChar.COMMA_CHAR))
                .map(s -> ConvertUtil.objToInt(s.trim())).collect(Collectors.toList());
        if (payStateList.contains(Constant.ALL_INT_VALUE)) {
            payStateList = Collections.emptyList();
        }
        //按条件分页查询合同
        List<ContractInfoModel> contractList = contractDao.findPage(sid, name, number, did, payStartDay, payEndDay,
                budgetId, vYear, orderType, payStateList, page);
        List<ContractListModel> list = getContractListModels(sid, contractList, EnumTrueFalse.否.getValue());

        result.success(list, page.getTotal());
        return result;
    }

    /**
     * 组装合同详细信息
     *
     * @param sid              学校id
     * @param contractInfoList 合同列表
     * @param downloadFlag     是否是下载
     * @return 合同详细信息列表
     * @author yeweiwei
     * @date 2021/11/17 19:11
     */
    private List<ContractListModel> getContractListModels(Long sid, List<ContractInfoModel> contractInfoList,
                                                          int downloadFlag) {
        //查询数据库名称
        Map<Long, String> dbIdNameMap = getDidNameMapBySid(sid);

        List<ContractListModel> list = Lists.newArrayList();
        for (ContractInfoModel contractInfo : contractInfoList) {
            Long contractId = contractInfo.getId();
            Long budgetId = contractInfo.getBudgetId();
            BigDecimal rmbPrice = contractInfo.getRmbPrice();

            Set<Long> didSet = contractDatabaseDao.findDidSetBySidContractId(sid, contractId);
            List<String> dbNameList = didSet.stream()
                    .map(s -> dbIdNameMap.getOrDefault(s, "")).collect(Collectors.toList());
            String dbNameListStr = StringUtils.join(dbNameList, Constant.SplitChar.COMMA_CHAR);

            ContractListModel model = new ContractListModel();
            model.setContractId(contractId);
            model.setContractName(contractInfo.getName());
            model.setNumber(contractInfo.getNumber());
            model.setDbNameList(dbNameList);
            model.setDatabaseName(dbNameListStr);

            EnumOrderType enumOrderType = EnumOrderType.getEnumOrderType(contractInfo.getOrderType());
            model.setOrderType( enumOrderType == null ? "" : enumOrderType.getName());

            model.setPayStartDay(contractInfo.getPayStartDay().toString());
            model.setPayEndDay(contractInfo.getPayEndDay().toString());
            model.setBudgetName(contractInfo.getBudgetName());

            Integer contractPayState = contractInfo.getPayState();
            EnumPayState enumPayState = EnumPayState.getEnumPayState(contractPayState);
            if (null == enumPayState) {
                continue;
            }
            model.setPayState(contractPayState);
            model.setPayStateStr(enumPayState.getName());

            BigDecimal paidPrice = BigDecimal.ZERO;
            BigDecimal unPaidPrice = BigDecimal.ZERO;
            switch (enumPayState) {
                case 未支付:
                    unPaidPrice = rmbPrice;
                    break;
                case 支付完成:
                    paidPrice = contractPayDao.findSumBySidBudgetIdContractIdPayFlag(sid,
                            budgetId, contractId, EnumTrueFalse.是.getValue());
                    break;
                case 已支付部分:
                    List<ContractPay> contractPayList =
                            contractPayDao.findListBySidBudgetIdContractId(sid, budgetId, contractId);

                    paidPrice = contractPayList.stream().filter(s -> s.getPayFlag() == EnumTrueFalse.是.getValue())
                            .map(ContractPay::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                    unPaidPrice = contractPayList.stream().filter(s -> s.getPayFlag() == EnumTrueFalse.否.getValue())
                            .map(ContractPay::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                    break;
                default:
                    break;
            }
            //下载 金额用万元做单位，保留四位小数
            if (downloadFlag == EnumTrueFalse.是.getValue()) {
                BigDecimal denominator = Constant.Price.TEN_THOUSAND;
                model.setRmbPrice(rmbPrice.divide(denominator, 4, BigDecimal.ROUND_HALF_UP)
                        .stripTrailingZeros().toPlainString());
                model.setPaidPrice(paidPrice.divide(denominator, 4, BigDecimal.ROUND_HALF_UP)
                        .stripTrailingZeros().toPlainString());
                model.setUnPaidPrice(unPaidPrice.divide(denominator, 4, BigDecimal.ROUND_HALF_UP)
                        .stripTrailingZeros().toPlainString());
            } else {
                model.setRmbPrice(rmbPrice.setScale(2, BigDecimal.ROUND_HALF_UP)
                        .stripTrailingZeros().toPlainString());
                model.setPaidPrice(paidPrice.setScale(2, BigDecimal.ROUND_HALF_UP)
                        .stripTrailingZeros().toPlainString());
                model.setUnPaidPrice(unPaidPrice.setScale(2, BigDecimal.ROUND_HALF_UP)
                        .stripTrailingZeros().toPlainString());
            }

            list.add(model);
        }
        return list;
    }

    /**
     * 根据学校id、合同id删除
     *
     * @param sid        学校id
     * @param contractId 合同id
     * @return 删除的结果
     * @author yeweiwei
     * @date 2021/11/17 18:56
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteContract(Long sid, Long contractId) {
        super.checkSid(sid);
        if (null == contractId) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }
        //删除合同
        contractDao.deleteById(contractId);

        //删除合同关联的数据库
        contractDatabaseDao.deleteBySidContractId(sid, contractId);

        //删除合同付款记录
        contractPayDao.deleteBySidContractId(sid, contractId);

        return Vm.DELETE_SUCCESS;
    }

    /**
     * 导出合同列表
     *
     * @param sid         学校id
     * @param name        合同名称
     * @param number      合同编号
     * @param did         数据库id
     * @param payStartDay 开始付款日期
     * @param payEndDay   截止付款日期
     * @param budgetId    预算id
     * @param vYear       年份
     * @param orderType   订购类型
     * @param payStates   支付状态
     * @return 导出的文件
     * @author yeweiwei
     * @date 2021/11/17 19:09
     */
    @Override
    public ResponseEntity<byte[]> exportContractList(Long sid, String name, String number, Long did, String payStartDay,
                                                     String payEndDay, Long budgetId, Integer vYear, Integer orderType,
                                                     String payStates) {
        super.checkSid(sid);
        List<Integer> payStateList = Arrays.stream(payStates.split(Constant.SplitChar.COMMA_CHAR))
                .map(s -> ConvertUtil.objToInt(s.trim())).collect(Collectors.toList());
        if (payStateList.contains(Constant.ALL_INT_VALUE)) {
            payStateList = Collections.emptyList();
        }
        List<ContractInfoModel> contractList = contractDao.findPage(sid, name, number, did, payStartDay, payEndDay,
                budgetId, vYear, orderType, payStateList, null);

        List<ContractListModel> list = getContractListModels(sid, contractList, EnumTrueFalse.是.getValue());

        // 导出
        String filePath = tempPath + System.currentTimeMillis();
        String fileName = "账簿列表";
        EasyExcel.write(filePath, ContractListModel.class).sheet(fileName).doWrite(list);
        return DownloadUtil.getResponseEntityCanDeleteFile(filePath,
                fileName.concat(Constant.Suffix.XLSX_WITH_POINT), EnumTrueFalse.是.getValue());
    }
}
