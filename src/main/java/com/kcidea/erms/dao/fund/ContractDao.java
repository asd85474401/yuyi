package com.kcidea.erms.dao.fund;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kcidea.erms.domain.fund.Contract;
import com.kcidea.erms.model.fund.ContractInfoModel;
import com.kcidea.erms.model.fund.ContractListModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/16
 **/
public interface ContractDao extends BaseMapper<Contract> {

    /**
     * 分页查询合同
     *
     * @param sid          学校id
     * @param name         合同名称
     * @param number       合同编号
     * @param did          数据库id
     * @param payStartDay  开始付款日期
     * @param payEndDay    截止付款日期
     * @param budgetId     预算id
     * @param vYear        年份
     * @param orderType    订购类型
     * @param payStateList 支付状态
     * @param page         分页信息
     * @return 合同列表
     * @author yeweiwei
     * @date 2021/11/16 15:51
     */
    List<ContractInfoModel> findPage(@Param("sid") Long sid, @Param("name") String name, @Param("number") String number,
                                     @Param("did") Long did, @Param("payStartDay") String payStartDay,
                                     @Param("payEndDay") String payEndDay, @Param("budgetId") Long budgetId,
                                     @Param("vYear") Integer vYear, @Param("orderType") Integer orderType,
                                     @Param("payStateList") List<Integer> payStateList,
                                     @Param("page") Page<ContractListModel> page);

    /**
     * 查询未支付和部分支付的合同列表
     *
     * @return 合同列表
     * @author yeweiwei
     * @date 2021/11/17 14:43
     */
    List<Contract> findUnPaidList();
}
