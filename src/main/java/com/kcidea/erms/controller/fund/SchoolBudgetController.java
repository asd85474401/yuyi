package com.kcidea.erms.controller.fund;

import com.kcidea.erms.aop.ActionRights;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.result.Result;
import com.kcidea.erms.controller.common.BaseController;
import com.kcidea.erms.enums.menu.EnumMenu;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.fund.SchoolBudgetInfoModel;
import com.kcidea.erms.model.fund.SchoolBudgetModel;
import com.kcidea.erms.service.fund.SchoolBudgetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/15
 **/
@Slf4j
@RestController
@RequestMapping("/schoolBudget")
public class SchoolBudgetController extends BaseController {

    @Resource
    private SchoolBudgetService schoolBudgetService;

    /**
     * 分页查询预算列表
     *
     * @param budgetName 预算来源
     * @param vYear      年份
     * @param pageNum    页码
     * @param pageSize   每页数量
     * @return 预算列表
     * @author majuehao
     * @date 2021/11/15 14:35
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.预算管理)
    @GetMapping(value = "/findBudgetList")
    public PageResult<SchoolBudgetModel> findBudgetList(
            @RequestParam(value = "budgetName", required = false) String budgetName,
            @RequestParam(value = "vYear") Integer vYear,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        super.saveInfoLog();
        checkYear(vYear);
        return schoolBudgetService.findBudgetListByNameYearSidPage(getSid(), budgetName, vYear, pageNum, pageSize);
    }

    /**
     * 导出预算列表
     *
     * @param budgetName 预算来源
     * @param vYear      年份
     * @return 预算列表
     * @author majuehao
     * @date 2021/11/15 14:35
     **/
    @ActionRights(userAction = EnumUserAction.导出, menu = EnumMenu.预算管理)
    @GetMapping(value = "/exportBudgetList")
    public ResponseEntity<byte[]> exportBudgetList(@RequestParam(value = "budgetName", required = false) String budgetName,
                                                   @RequestParam(value = "vYear") Integer vYear) {
        super.saveInfoLog();
        checkYear(vYear);
        return schoolBudgetService.exportBudgetList(getSid(), budgetName, vYear);
    }

    /**
     * 新增学校预算来源
     *
     * @param schoolBudgetInfoModel 学校预算来源信息
     * @return 新增的结果
     * @author majuehao
     * @date 2021/11/15 16:15
     **/
    @ActionRights(userAction = EnumUserAction.新增, menu = EnumMenu.预算管理)
    @PostMapping(value = "/addSchoolBudget")
    public Result<String> addSchoolBudget(@RequestBody @Valid
                                                  SchoolBudgetInfoModel schoolBudgetInfoModel) {
        super.saveInfoLog();
        return new Result<String>().successMsg(schoolBudgetService.addSchoolBudget(getSid(), schoolBudgetInfoModel,
                getUserId()));
    }

    /**
     * 编辑预算回显
     *
     * @param budgetRelId 预算id
     * @return 预算信息
     * @author majuehao
     * @date 2021/11/15 16:15
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.预算管理)
    @GetMapping(value = "/findOneByBudgetRelId")
    public Result<SchoolBudgetInfoModel> findOneByBudgetRelId(@RequestParam("budgetRelId") Long budgetRelId) {
        super.saveInfoLog();
        return new Result<SchoolBudgetInfoModel>().success(schoolBudgetService.findOneByBudgetRelId(getSid(),
                budgetRelId));
    }


    /**
     * 编辑预算
     *
     * @param schoolBudgetInfoModel 预算详情
     * @return 编辑的结果
     * @author majuehao
     * @date 2022/2/14 9:28
     **/
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.预算管理)
    @PostMapping(value = "/updateBudget")
    public Result<String> updateBudget(@RequestBody @Valid SchoolBudgetInfoModel schoolBudgetInfoModel) {
        super.saveInfoLog();
        return new Result<String>().successMsg(schoolBudgetService.updateBudget(getSid(), getUserId(),
                schoolBudgetInfoModel));
    }

    /**
     * 删除预算
     *
     * @param budgetRelId 预算id
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/15 16:15
     **/
    @ActionRights(userAction = EnumUserAction.删除, menu = EnumMenu.预算管理)
    @DeleteMapping(value = "/deleteBudget")
    public Result<String> deleteBudget(@RequestParam(value = "budgetRelId") Long budgetRelId) {
        super.saveInfoLog();
        return new Result<String>().successMsg(schoolBudgetService.deleteBudget(getSid(), budgetRelId));
    }

}
