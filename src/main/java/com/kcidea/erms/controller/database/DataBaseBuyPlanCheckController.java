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
import com.kcidea.erms.service.database.DataBaseBuyPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/08
 **/
@Slf4j
@RestController
@RequestMapping("/databaseBuyPlanCheck")
public class DataBaseBuyPlanCheckController extends BaseController {

    @Resource
    private DataBaseBuyPlanService dataBaseBuyPlanService;

    /**
     * 年度采购计划审核列表
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
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.年度采购计划审核)
    @GetMapping(value = "/findBuyPlanCheckList")
    public PageResult<DatabaseBuyListModel> findBuyPlanCheckList(
            @RequestParam(value = "vYear") Integer vYear,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "state", defaultValue = "999") Integer state,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        super.saveInfoLog();
        super.checkYear(vYear);
        return dataBaseBuyPlanService.findBuyPlanList(getSid(), vYear, title, state, true, pageNum, pageSize);
    }

    /**
     * 审核采购计划
     *
     * @param planId 采购计划id
     * @param state  审核状态
     * @param remark 审核说明
     * @return 修改的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.年度采购计划审核)
    @PostMapping(value = "/updateDataBaseBuyPlanCheck")
    public Result<String> updateDataBaseBuyPlanCheck(@RequestParam(value = "planId") Long planId,
                                                     @RequestParam(value = "state") Integer state,
                                                     @RequestParam(value = "remark") String remark) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                dataBaseBuyPlanService.updateDataBaseBuyPlanCheck(getSid(), planId, state, remark, getUserId()));
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
    @ActionRights(userAction = EnumUserAction.导出, menu = EnumMenu.年度采购计划审核)
    @GetMapping(value = "/exportBuyPlanDataBaseList")
    public ResponseEntity<byte[]> exportBuyPlanDataBaseList(@RequestParam(value = "planId") Long planId,
                                                            @RequestParam(value = "vYear") Integer vYear) {
        super.saveInfoLog();
        super.checkYear(vYear);
        return dataBaseBuyPlanService.exportBuyPlanDataBaseList(getSid(), planId, vYear);
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
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.年度采购计划审核)
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
     * 数据库采购列表 ==》获取数据库下拉框
     *
     * @param planId 采购计划id
     * @return 数据库下拉框
     * @author majuehao
     * @date 2021/12/7 14:23
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.年度采购计划审核)
    @GetMapping(value = "/findBuyPlanDataBaseDropDownList")
    public MultipleResult<IdNameModel> findBuyPlanDataBaseDropDownList(@RequestParam(value = "planId") Long planId) {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>()
                .success(dataBaseBuyPlanService.findBuyPlanDataBaseDropDownList(getSid(), planId));
    }

}
