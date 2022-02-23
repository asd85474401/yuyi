package com.kcidea.erms.dao.fund;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kcidea.erms.domain.fund.ContractPay;
import com.kcidea.erms.model.common.IdValueModel;
import com.kcidea.erms.model.fund.ContractPayListModel;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/16
 **/
public interface ContractPayDao extends BaseMapper<ContractPay> {
    /**
     * 根据条件查询已支付金额之和
     *
     * @param sid        学校id
     * @param budgetId   预算id
     * @param contractId 合同id
     * @param payFlag    支付状态
     * @return 已支付金额之和
     * @author yeweiwei
     * @date 2021/11/16 17:29
     */
    BigDecimal findSumBySidBudgetIdContractIdPayFlag(@Param("sid") Long sid, @Param("budgetId") Long budgetId,
                                                     @Param("contractId") Long contractId, @Param("payFlag") int payFlag);

    /**
     * 根据学校id、合同id、发票编号查询合同支付记录
     *
     * @param sid           学校id
     * @param contractId    合同id
     * @param invoiceNumber 发票编号
     * @param page          分页信息
     * @return 合同支付记录记录
     * @author yeweiwei
     * @date 2021/11/16 19:36
     */
    List<ContractPayListModel> findPage(@Param("sid") Long sid, @Param("contractId") Long contractId,
                                        @Param("invoiceNumber") String invoiceNumber,
                                        @Param("page") Page<ContractPayListModel> page);

    /**
     * 根据学校id、合同id删除
     *
     * @param sid        学校id
     * @param contractId 合同id
     * @author yeweiwei
     * @date 2021/11/17 19:04
     */
    void deleteBySidContractId(@Param("sid") Long sid, @Param("contractId") Long contractId);

    /**
     * 根据学校id、预算id，查找合同支付记录数量
     *
     * @param sid      学校id
     * @param budgetId 预算id
     * @return 合同支付记录数量
     * @author majuehao
     * @date 2021/11/18 13:28
     **/
    int findCountBySidBudgetId(@Param("sid") Long sid, @Param("budgetId") Long budgetId);

    /**
     * 根据学校id、经费id、合同id查询支付计划
     *
     * @param sid        学校id
     * @param budgetId   经费id
     * @param contractId 合同id
     * @return 支付计划集合
     * @author yeweiwei
     * @date 2021/11/18 15:20
     */
    List<ContractPay> findListBySidBudgetIdContractId(@Param("sid") Long sid, @Param("budgetId") Long budgetId,
                                                      @Param("contractId") Long contractId);

    /**
     * 根据条件查询已支付金额之和
     *
     * @param sid      学校id
     * @param budgetId 预算id
     * @param vYear    合同id
     * @param payFlag  支付状态
     * @return 已支付金额之和
     * @author majuehao
     * @date 2021/11/18 15:18
     */
    BigDecimal findSumBySidBudgetIdvYearPayFlag(@Param("sid") Long sid, @Param("budgetId") Long budgetId,
                                                @Param("vYear") Integer vYear, @Param("payFlag") Integer payFlag);

    /**
     * 根据条件，分组查询已支付/未支付金额之和
     *
     * @param sid     学校id
     * @param vYear   合同id
     * @param payFlag 支付状态
     * @return 已支付金额之和
     * @author majuehao
     * @date 2021/11/18 16:04
     **/
    List<IdValueModel> findSumBySidYearPayFlag(@Param("sid") Long sid,
                                               @Param("vYear") Integer vYear,
                                               @Param("payFlag") int payFlag);
}