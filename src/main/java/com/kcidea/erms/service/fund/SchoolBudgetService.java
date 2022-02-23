package com.kcidea.erms.service.fund;


import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.fund.SchoolBudgetInfoModel;
import com.kcidea.erms.model.fund.SchoolBudgetModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/15 14:40
 **/
public interface SchoolBudgetService {

    /**
     * 分页查询预算列表
     *
     * @param sid        学校id
     * @param budgetName 预算来源
     * @param vYear      年份
     * @param pageNum    页码
     * @param pageSize   每页数量
     * @return 预算列表
     * @author majuehao
     * @date 2021/11/15 14:35
     **/
    PageResult<SchoolBudgetModel> findBudgetListByNameYearSidPage(Long sid, String budgetName, Integer vYear,
                                                                  Integer pageNum, Integer pageSize);

    /**
     * 新增学校预算来源
     *
     * @param sid                   学校id
     * @param schoolBudgetInfoModel 学校预算来源信息
     * @param adminId               操作人id
     * @return 新增的结果
     * @author majuehao
     * @date 2021/11/15 16:15
     **/
    String addSchoolBudget(Long sid, SchoolBudgetInfoModel schoolBudgetInfoModel, Long adminId);

    /**
     * 编辑预算回显
     *
     * @param sid         学校
     * @param budgetRelId 预算id
     * @return 预算信息
     * @author majuehao
     * @date 2021/11/15 16:15
     **/
    SchoolBudgetInfoModel findOneByBudgetRelId(Long sid, Long budgetRelId);

    /**
     * 编辑预算
     *
     * @param sid                   学校id
     * @param adminId               操作人id
     * @param schoolBudgetInfoModel 预算详情
     * @return 编辑的结果
     * @author majuehao
     * @date 2021/11/15 16:15
     **/
    String updateBudget(Long sid, Long adminId, SchoolBudgetInfoModel schoolBudgetInfoModel);

    /**
     * 删除预算
     *
     * @param sid         学校id
     * @param budgetRelId 预算id
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/15 16:15
     **/
    String deleteBudget(Long sid, Long budgetRelId);

    /**
     * 导出预算列表
     *
     * @param sid        学校id
     * @param budgetName 预算来源
     * @param vYear      年份
     * @return 预算列表
     * @author majuehao
     * @date 2021/11/15 14:35
     **/
    ResponseEntity<byte[]> exportBudgetList(Long sid, String budgetName, Integer vYear);

    /**
     * 根据学校id获取预算来源下拉集合
     *
     * @param sid 学校id
     * @return 预算来源下拉集合
     * @author yeweiwei
     * @date 2021/11/16 13:41
     */
    List<IdNameModel> findBudgetSelectListBySid(Long sid);

}
