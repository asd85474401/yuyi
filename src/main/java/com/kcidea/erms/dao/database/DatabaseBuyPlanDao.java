package com.kcidea.erms.dao.database;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kcidea.erms.domain.database.DatabaseBuyPlan;
import com.kcidea.erms.model.database.buyplan.DatabaseBuyListModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/6 16:10
 **/
public interface DatabaseBuyPlanDao extends BaseMapper<DatabaseBuyPlan> {

    /**
     * 根据条件，查询采购计划列表
     *
     * @param vYear     年份
     * @param title     采购计划名称
     * @param state     审核状态
     * @param sid       学校id
     * @param checkFlag 审核标识
     * @param page      分页
     * @return 采购计划列表
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    List<DatabaseBuyListModel> findListByYearTitleStateSidCheckFlagPage(@Param("vYear") Integer vYear,
                                                                        @Param("title") String title,
                                                                        @Param("state") Integer state,
                                                                        @Param("sid") Long sid,
                                                                        @Param("checkFlag") boolean checkFlag,
                                                                        @Param("page") Page<DatabaseBuyListModel> page);

    /**
     * 根据学校id、标题、年份、采购计划id，查询采购计划数量
     *
     * @param sid    学校id
     * @param title  标题名称
     * @param vYear  年份
     * @param planId 采购计划id
     * @return 采购计划数量
     * @author majuehao
     * @date 2021/12/7 10:27
     **/
    int findCountBySidTitleYearPlanId(@Param("sid") Long sid, @Param("title") String title,
                                      @Param("vYear") Integer vYear, @Param("planId") Long planId);

    /**
     * 根据学校id、采购id，查找采购计划
     *
     * @param sid    学校id
     * @param planId 采购关系id
     * @return 采购计划
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    DatabaseBuyPlan findOneBySidPlanId(@Param("sid") Long sid, @Param("planId") Long planId);

    /**
     * 根据学校id、采购id，删除采购计划
     *
     * @param sid    学校id
     * @param planId 采购计划id
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    void deleteBySidPlanId(@Param("sid") Long sid, @Param("planId") Long planId);


    /**
     * 根据学校id、采购id，查找采购计划审核状态
     *
     * @param sid    学校id
     * @param planId 采购计划id
     * @return 审核状态
     * @author majuehao
     * @date 2021/12/8 14:37
     **/
    Integer findStateBySidPlanId(@Param("sid") Long sid, @Param("planId") Long planId);
}