package com.kcidea.erms.dao.fund;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.fund.ContractDatabase;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/16
 **/
public interface ContractDatabaseDao extends BaseMapper<ContractDatabase> {
    /**
     * 根据学校id查询带合同的数据库id集合
     *
     * @param sid        学校id
     * @param contractId 合同id
     * @return 数据库id集合
     * @author yeweiwei
     * @date 2021/11/16 14:35
     */
    Set<Long> findDidSetBySidContractId(@Param("sid") Long sid, @Param("contractId") Long contractId);

    /**
     * 根据学校id、合同id删除
     *
     * @param sid        学校id
     * @param contractId 合同id
     * @author yeweiwei
     * @date 2021/11/17 19:02
     */
    void deleteBySidContractId(@Param("sid") Long sid, @Param("contractId") Long contractId);
}
