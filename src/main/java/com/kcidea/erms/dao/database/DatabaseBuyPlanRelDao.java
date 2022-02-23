package com.kcidea.erms.dao.database;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kcidea.erms.domain.database.DatabaseBuyPlanRel;
import com.kcidea.erms.model.database.buyplan.DatabaseBuyListModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/6 16:10
 **/
public interface DatabaseBuyPlanRelDao extends BaseMapper<DatabaseBuyPlanRel> {

    /**
     * 根据学校id、采购id，查询数据库数量，总金额
     *
     * @param sid    学校id
     * @param planId 采购id
     * @return 数据库数量，总金额
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    DatabaseBuyListModel findDataBasePriceCountBySidPlanId(@Param("sid") Long sid, @Param("planId") Long planId);

    /**
     * 根据学校id、采购id，数据库id，分页查询采购计划列表
     *
     * @param sid    学校id
     * @param planId 采购计划id
     * @param did    数据量id
     * @param page   分页
     * @return 采购计划列表
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    List<DatabaseBuyListModel> findListBySidPlanIdDidPage(@Param("sid") Long sid,
                                                          @Param("planId") Long planId,
                                                          @Param("did") Long did,
                                                          @Param("page") Page<DatabaseBuyListModel> page);

    /**
     * 根据学校id、数据库id、采购计划id，查询数据库的数量
     *
     * @param sid       学校id
     * @param did       数据库id
     * @param planId    采购计划id
     * @param planRelId 采购计划关系id
     * @return 数据库的数量
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    int findCountBySidDidPlanIdPlanRelId(@Param("sid") Long sid, @Param("did") Long did, @Param("planId") Long planId,
                                         @Param("planRelId") Long planRelId);

    /**
     * 根据学校id、关系id，查找采购数据库计划
     *
     * @param sid       学校id
     * @param planRelId 采购关系id
     * @return 采购数据库计划
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    DatabaseBuyPlanRel findOneBySidPlanRelId(@Param("sid") Long sid, @Param("planRelId") Long planRelId);

    /**
     * 根据学校id、关系id，删除采购数据库计划
     *
     * @param sid       学校id
     * @param planRelId 采购计划id
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    void deleteBySidPlanRelId(@Param("sid") Long sid, @Param("planRelId") Long planRelId);

    /**
     * 根据学校id、采购id，删除采购数据库计划
     *
     * @param sid    学校id
     * @param planId 采购计划id
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    void deleteBySidPlanId(@Param("sid") Long sid, @Param("planId") Long planId);
}