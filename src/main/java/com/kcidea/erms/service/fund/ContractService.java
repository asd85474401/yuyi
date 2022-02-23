package com.kcidea.erms.service.fund;

import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.fund.ContractListModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/16
 **/
public interface ContractService {
    /**
     * 获取带有合同的数据库下拉集合
     *
     * @param sid 学校id
     * @return 数据库下拉集合
     * @author yeweiwei
     * @date 2021/11/16 13:59
     */
    List<IdNameModel> findDatabaseSelectList(Long sid);

    /**
     * 获取订购类型下拉集合
     *
     * @return 订购类型下拉集合
     * @author yeweiwei
     * @date 2021/11/16 14:11
     */
    List<IdNameModel> findOrderTypeSelectList();

    /**
     * 获取支付状态下拉集合
     *
     * @return 支付状态下拉集合
     * @author yeweiwei
     * @date 2021/11/16 14:16
     */
    List<IdNameModel> findPayStateSelectList();

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
    PageResult<ContractListModel> findContractList(Long sid, String name, String number, Long did, String payStartDay,
                                                   String payEndDay, Long budgetId, Integer vYear, Integer orderType,
                                                   String payStates, Integer pageNum, Integer pageSize);

    /**
     * 根据学校id、合同id删除
     *
     * @param sid        学校id
     * @param contractId 合同id
     * @return 删除的结果
     * @author yeweiwei
     * @date 2021/11/17 18:56
     */
    String deleteContract(Long sid, Long contractId);

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
    ResponseEntity<byte[]> exportContractList(Long sid, String name, String number, Long did, String payStartDay,
                                              String payEndDay, Long budgetId, Integer vYear, Integer orderType,
                                              String payStates);
}
