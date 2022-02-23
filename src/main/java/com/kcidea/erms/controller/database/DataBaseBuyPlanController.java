package com.kcidea.erms.controller.database;

import com.kcidea.erms.aop.ActionRights;
import com.kcidea.erms.common.result.MultipleResult;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.result.Result;
import com.kcidea.erms.controller.common.BaseController;
import com.kcidea.erms.enums.menu.EnumMenu;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.database.buyplan.DatabaseBuyListModel;
import com.kcidea.erms.model.database.buyplan.DatabaseBuyPlanRelInfoModel;
import com.kcidea.erms.service.database.DataBaseBuyPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 数据库年度采购计划
 *
 * @author huxubin
 * @version 1.0
 * @date 2021/12/6
 **/
@Slf4j
@RestController
@RequestMapping("/databaseBuyPlan")
public class DataBaseBuyPlanController extends BaseController {

    @Resource
    private DataBaseBuyPlanService dataBaseBuyPlanService;

    /**
     * 年度采购计划列表
     *
     * @param vYear    年份
     * @param title    采购计划名称
     * @param state    审核状态
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 采购计划列表
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.年度采购计划管理)
    @GetMapping(value = "/findBuyPlanList")
    public PageResult<DatabaseBuyListModel> findBuyPlanList(
            @RequestParam(value = "vYear") Integer vYear,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "state", defaultValue = "999") Integer state,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        super.saveInfoLog();
        super.checkYear(vYear);
        return dataBaseBuyPlanService.findBuyPlanList(getSid(), vYear, title, state, false, pageNum, pageSize);
    }

    /**
     * 年度采购计划数据库列表
     *
     * @param planId   采购计划id
     * @param did      数据库id
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 采购计划列表
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.年度采购计划管理)
    @GetMapping(value = "/findBuyPlanDataBaseList")
    public PageResult<DatabaseBuyListModel> findBuyPlanDataBaseList(
            @RequestParam(value = "planId") Long planId,
            @RequestParam(value = "did", defaultValue = "999") Long did,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        super.saveInfoLog();
        return dataBaseBuyPlanService.findBuyPlanDataBaseList(getSid(), planId, did, pageNum, pageSize);
    }

    /**
     * 回显采购计划
     *
     * @param planId 采购计划id
     * @return 采购计划
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.年度采购计划管理)
    @GetMapping(value = "/findDatabaseBuyPlanOne")
    public Result<String> findDatabaseBuyPlanOne(@RequestParam(value = "planId") Long planId) {
        super.saveInfoLog();
        return new Result<String>().success(
                dataBaseBuyPlanService.findDatabaseBuyPlanOne(getSid(), planId));
    }

    /**
     * 新增采购计划
     *
     * @param planId 采购计划id
     * @param title  采购计划标题
     * @param vYear  年份
     * @return 新增的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.新增, menu = EnumMenu.年度采购计划管理)
    @PostMapping(value = "/addDatabaseBuyPlan")
    public Result<IdNameModel> addDatabaseBuyPlan(@RequestParam(value = "planId", required = false) Long planId,
                                                  @RequestParam(value = "title") String title,
                                                  @RequestParam(value = "vYear", defaultValue = "999") Integer vYear) {
        super.saveInfoLog();
        super.checkYear(vYear);
        return dataBaseBuyPlanService.addOrUpdateDatabaseBuyPlan(getSid(), planId, title, vYear, getUserId());
    }

    /**
     * 编辑采购计划
     *
     * @param planId 采购计划id
     * @param title  采购计划标题
     * @param vYear  年份
     * @return 编辑的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.年度采购计划管理)
    @PostMapping(value = "/updateDatabaseBuyPlan")
    public Result<IdNameModel> updateDatabaseBuyPlan(@RequestParam(value = "planId", required = false) Long planId,
                                                     @RequestParam(value = "title") String title,
                                                     @RequestParam(value = "vYear", defaultValue = "999") Integer vYear) {
        super.saveInfoLog();
        super.checkYear(vYear);
        return dataBaseBuyPlanService.addOrUpdateDatabaseBuyPlan(getSid(), planId, title, vYear, getUserId());
    }

    /**
     * 采购计划提交审核
     *
     * @param planId 采购计划id
     * @return 修改的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.年度采购计划管理)
    @PostMapping(value = "/updateDatabaseBuyPlanState")
    public Result<String> updateDatabaseBuyPlanState(@RequestParam(value = "planId") Long planId) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                dataBaseBuyPlanService.updateDatabaseBuyPlanState(getSid(), planId, getUserId()));
    }

    /**
     * 回显采购数据库
     *
     * @param planRelId 采购关系id
     * @return 采购数据库
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.年度采购计划管理)
    @GetMapping(value = "/findDatabaseBuyPlanRelOne")
    public Result<DatabaseBuyPlanRelInfoModel> findDatabaseBuyPlanRelOne(@RequestParam(value = "planRelId") Long planRelId) {
        super.saveInfoLog();
        return new Result<DatabaseBuyPlanRelInfoModel>().success(
                dataBaseBuyPlanService.findDatabaseBuyPlanRelOne(getSid(), planRelId));
    }

    /**
     * 新增采购数据库
     *
     * @param databaseBuyPlanRelInfoModel 采购数据库详情
     * @return 新增的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.新增, menu = EnumMenu.年度采购计划管理)
    @PostMapping(value = "/addDatabaseBuyPlanRel")
    public Result<String> addDatabaseBuyPlanRel(@RequestBody @Valid DatabaseBuyPlanRelInfoModel
                                                        databaseBuyPlanRelInfoModel) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                dataBaseBuyPlanService.addOrUpdateDatabaseBuyPlanRel(getSid(), databaseBuyPlanRelInfoModel, getUserId()));
    }

    /**
     * 编辑采购数据库
     *
     * @param databaseBuyPlanRelInfoModel 采购数据库详情
     * @return 编辑的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.年度采购计划管理)
    @PostMapping(value = "/updateDatabaseBuyPlanRel")
    public Result<String> updateDatabaseBuyPlanRel(@RequestBody @Valid DatabaseBuyPlanRelInfoModel
                                                           databaseBuyPlanRelInfoModel) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                dataBaseBuyPlanService.addOrUpdateDatabaseBuyPlanRel(getSid(), databaseBuyPlanRelInfoModel, getUserId()));
    }

    /**
     * 删除采购计划
     *
     * @param planId 采购计划id
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.删除, menu = EnumMenu.年度采购计划管理)
    @DeleteMapping(value = "/deleteDatabaseBuyPlan")
    public Result<String> deleteDatabaseBuyPlan(@RequestParam(value = "planId") Long planId) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                dataBaseBuyPlanService.deleteDatabaseBuyPlan(getSid(), planId));
    }

    /**
     * 删除采购数据库
     *
     * @param planRelId 采购数据库详情
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.删除, menu = EnumMenu.年度采购计划管理)
    @DeleteMapping(value = "/deleteDatabaseBuyPlanRel")
    public Result<String> deleteDatabaseBuyPlanRel(@RequestParam(value = "planRelId") Long planRelId) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                dataBaseBuyPlanService.deleteDatabaseBuyPlanRel(getSid(), planRelId));
    }

    /**
     * 新增数据库采购计划 ==》获取数据库下拉框
     *
     * @param vYear 年份
     * @return 数据库下拉框
     * @author majuehao
     * @date 2021/12/7 14:23
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.年度采购计划管理)
    @GetMapping(value = "/findBuyPlanRelDataBaseDropDownList")
    public MultipleResult<IdNameModel> findBuyPlanRelDataBaseDropDownList(@RequestParam(value = "vYear") Integer vYear) {
        super.saveInfoLog();
        super.checkYear(vYear);
        return new MultipleResult<IdNameModel>().success(dataBaseBuyPlanService.findBuyPlanRelDataBaseDropDownList(getSid(), vYear));
    }

    /**
     * 数据库采购列表 ==》获取数据库下拉框
     *
     * @param planId 采购计划id
     * @return 数据库下拉框
     * @author majuehao
     * @date 2021/12/7 14:23
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.年度采购计划管理)
    @GetMapping(value = "/findBuyPlanDataBaseDropDownList")
    public MultipleResult<IdNameModel> findBuyPlanDataBaseDropDownList(@RequestParam(value = "planId") Long planId) {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(dataBaseBuyPlanService.findBuyPlanDataBaseDropDownList(getSid(), planId));
    }

    /**
     * 年度采购计划审核导出
     *
     * @param planId 采购计划id
     * @param vYear  年份
     * @return 数据库下拉框
     * @author majuehao
     * @date 2021/12/7 14:23
     **/
    @ActionRights(userAction = EnumUserAction.导出, menu = EnumMenu.年度采购计划管理)
    @GetMapping(value = "/exportBuyPlanDataBaseList")
    public ResponseEntity<byte[]> exportBuyPlanDataBaseList(@RequestParam(value = "planId") Long planId,
                                                            @RequestParam(value = "vYear") Integer vYear) {
        super.saveInfoLog();
        super.checkYear(vYear);
        return dataBaseBuyPlanService.exportBuyPlanDataBaseList(getSid(), planId, vYear);
    }


}
