package com.kcidea.erms.service.database;

import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.result.Result;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.database.buyplan.DatabaseBuyListModel;
import com.kcidea.erms.model.database.buyplan.DatabaseBuyPlanRelInfoModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/06
 **/
public interface DataBaseBuyPlanService {

    /**
     * 采购计划列表
     *
     * @param sid       学校id
     * @param vYear     年份
     * @param title     采购计划名称
     * @param state     审核状态
     * @param checkFlag 审核标识
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @return 采购计划列表
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    PageResult<DatabaseBuyListModel> findBuyPlanList(Long sid, Integer vYear, String title, Integer state,
                                                     boolean checkFlag, Integer pageNum, Integer pageSize);

    /**
     * 年度采购计划数据库列表
     *
     * @param sid      学校id
     * @param planId   采购计划id
     * @param did      数据库id
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 采购计划列表
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    PageResult<DatabaseBuyListModel> findBuyPlanDataBaseList(Long sid, Long planId, Long did, Integer pageNum,
                                                             Integer pageSize);

    /**
     * 新增/编辑采购计划
     *
     * @param sid    学校id
     * @param planId 采购计划id
     * @param title  采购计划标题
     * @param vYear  年份
     * @param userId 操作人id
     * @return 新增/编辑的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    Result<IdNameModel> addOrUpdateDatabaseBuyPlan(Long sid, Long planId, String title, Integer vYear, Long userId);

    /**
     * 回显采购计划
     *
     * @param sid    学校id
     * @param planId 采购计划id
     * @return 采购计划
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    String findDatabaseBuyPlanOne(Long sid, Long planId);

    /**
     * 新增/编辑采购数据库
     *
     * @param sid                         学校id
     * @param databaseBuyPlanRelInfoModel 采购数据库详情
     * @param userId                      操作人id
     * @return 新增/编辑的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    String addOrUpdateDatabaseBuyPlanRel(Long sid, DatabaseBuyPlanRelInfoModel databaseBuyPlanRelInfoModel, Long userId);

    /**
     * 回显采购数据库
     *
     * @param sid       学校id
     * @param planRelId 采购关系id
     * @return 采购数据库
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    DatabaseBuyPlanRelInfoModel findDatabaseBuyPlanRelOne(Long sid, Long planRelId);

    /**
     * 删除采购计划
     *
     * @param sid    学校id
     * @param planId 采购计划id
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    String deleteDatabaseBuyPlan(Long sid, Long planId);

    /**
     * 删除采购数据库
     *
     * @param sid       学校id
     * @param planRelId 采购数据库详情
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    String deleteDatabaseBuyPlanRel(Long sid, Long planRelId);

    /**
     * 新增数据库采购计划 ==》获取数据库下拉框
     *
     * @param sid   学校id
     * @param vYear 年份
     * @return 数据库下拉框
     * @author majuehao
     * @date 2021/12/7 14:23
     **/
    List<IdNameModel> findBuyPlanRelDataBaseDropDownList(Long sid, Integer vYear);

    /**
     * 数据库采购列表 ==》获取数据库下拉框
     *
     * @param sid    学校id
     * @param planId 采购计划id
     * @return 数据库下拉框
     * @author majuehao
     * @date 2021/12/7 14:23
     **/
    List<IdNameModel> findBuyPlanDataBaseDropDownList(Long sid, Long planId);

    /**
     * 采购计划提交审核
     *
     * @param sid    学校id
     * @param planId 采购计划id
     * @param userId 操作人id
     * @return 修改的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    String updateDatabaseBuyPlanState(Long sid, Long planId, Long userId);

    /**
     * 审核采购计划
     *
     * @param sid    学校id
     * @param planId 采购计划id
     * @param state  审核状态
     * @param remark 审核说明
     * @param userId 操作人id
     * @return 修改的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    String updateDataBaseBuyPlanCheck(Long sid, Long planId, Integer state, String remark, Long userId);

    /**
     * 年度采购计划审核导出
     *
     * @param sid    学校id
     * @param planId 采购计划id
     * @param vYear  年份
     * @return 数据库下拉框
     * @author majuehao
     * @date 2021/12/7 14:23
     **/
    ResponseEntity<byte[]> exportBuyPlanDataBaseList(Long sid, Long planId, Integer vYear);
}
