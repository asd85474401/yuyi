package com.kcidea.erms.service.fund;

import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.model.fund.ContractPayListModel;
import com.kcidea.erms.model.fund.PayRemindInfoModel;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/16
 **/
public interface ContractPayService {
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
    PageResult<ContractPayListModel> findContractPayList(Long sid, Long contractId, String invoiceNumber,
                                                         Integer pageNum, Integer pageSize);

    /**
     * 查询学校的到期提醒设置
     *
     * @param sid 学校id
     * @return 到期提醒设置
     * @author yeweiwei
     * @date 2021/11/17 9:43
     */
    PayRemindInfoModel findPayRemind(Long sid);

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
    String updatePayRemind(Long sid, PayRemindInfoModel payRemindInfo, Long userId);
}
