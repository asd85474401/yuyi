package com.kcidea.erms.dao.fund;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kcidea.erms.domain.fund.SchoolBudgetRel;
import com.kcidea.erms.model.fund.SchoolBudgetExportModel;
import com.kcidea.erms.model.fund.SchoolBudgetModel;
import com.kcidea.erms.model.fund.YearBudgetModel;
import com.kcidea.erms.model.user.UserManageModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/15
 **/
public interface SchoolBudgetRelDao extends BaseMapper<SchoolBudgetRel> {

    /**
     * 分页查询预算列表
     *
     * @param budgetName 预算来源
     * @param vYear      年份
     * @param sid        学校id
     * @param page       分页
     * @return 预算列表
     * @author majuehao
     * @date 2021/11/15 14:35
     **/
    List<SchoolBudgetModel> findBudgetListByNameYearSidPage(@Param("budgetName") String budgetName,
                                                            @Param("vYear") Integer vYear, @Param("sid") Long sid,
                                                            @Param("page") Page<UserManageModel> page);

    /**
     * 编辑预算回显
     *
     * @param sid 学校id
     * @param id  预算id
     * @return 预算信息
     * @author majuehao
     * @date 2021/11/15 16:15
     **/
    SchoolBudgetRel findOneBySidId(@Param("sid") Long sid, @Param("id") Long id);

    /**
     * 根据预算Id删除对应年份预算
     *
     * @param budgetId 预算Id
     * @author majuehao
     * @date 2021/11/16 13:49
     **/
    void deleteByBudgetId(@Param("budgetId") Long budgetId);

    /**
     * 根据预算关系id，学校Id查询来源
     *
     * @param sid      学校Id
     * @param budgetId 预算Id
     * @return 来源id
     * @author majuehao
     * @date 2021/11/16 14:00
     **/
    SchoolBudgetRel findOneBySidBudgetRelId(@Param("sid") Long sid, @Param("budgetId") Long budgetId);

    /**
     * 查询预算导出列表
     *
     * @param budgetName 预算来源
     * @param vYear      年份
     * @param sid        学校id
     * @return 预算列表
     * @author majuehao
     * @date 2021/11/15 14:35
     **/
    List<SchoolBudgetExportModel> findBudgetListByNameYearSid(@Param("budgetName") String budgetName,
                                                              @Param("vYear") Integer vYear, @Param("sid") Long sid);


    /**
     * 根据学校id，开始结束年份，查询历年预算排名
     *
     * @param sid       学校id
     * @param startYear 开始年份
     * @param endYear   结束年份
     * @return 历年预算排名
     * @author majuehao
     * @date 2021/11/17 10:19
     **/
    List<YearBudgetModel> findBudgetRankListBySidYear(@Param("sid") Long sid, @Param("startYear") Integer startYear,
                                                      @Param("endYear") Integer endYear);
}
