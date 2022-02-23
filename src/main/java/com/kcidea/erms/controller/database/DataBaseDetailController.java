package com.kcidea.erms.controller.database;

import com.kcidea.erms.aop.ActionRights;
import com.kcidea.erms.common.result.MultipleResult;
import com.kcidea.erms.common.result.Result;
import com.kcidea.erms.controller.common.BaseController;
import com.kcidea.erms.enums.menu.EnumMenu;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.database.detail.DataBaseDetailModel;
import com.kcidea.erms.model.database.detail.DataBaseTitleDetailModel;
import com.kcidea.erms.model.database.evaluation.DatabaseEvaluationInfoModel;
import com.kcidea.erms.service.database.DataBaseDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/30
 **/
@Slf4j
@RestController
@RequestMapping("/databaseDetail")
public class DataBaseDetailController extends BaseController {

    @Resource
    private DataBaseDetailService dataBaseDetailService;

    /**
     * 查询数据库标题栏信息
     *
     * @param did 数据库id
     * @return 数据库标题栏信息
     * @author majuehao
     * @date 2021/11/30 14:40
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库详情)
    @GetMapping(value = "/findDataBaseTitleDetail")
    public Result<DataBaseTitleDetailModel> findDataBaseTitleDetail(@RequestParam(value = "did") Long did) {
        super.saveInfoLog();
        return new Result<DataBaseTitleDetailModel>()
                .success(dataBaseDetailService.findDataBaseTitleDetail(getSid(), did));
    }

    /**
     * 查询数据库信息
     *
     * @param did 数据库id
     * @return 数据库数据库信息
     * @author majuehao
     * @date 2021/11/30 14:40
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库详情)
    @GetMapping(value = "/findDataBaseDetail")
    public Result<DataBaseDetailModel> findDataBaseDetail(@RequestParam(value = "did") Long did) {
        super.saveInfoLog();
        return new Result<DataBaseDetailModel>().success(dataBaseDetailService.findDataBaseDetail(getSid(), did));
    }

    /**
     * 查询数据库历年评估记录
     *
     * @param did 数据库id
     * @return 历年评估记录
     * @author majuehao
     * @date 2021/11/30 14:40
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库详情)
    @GetMapping(value = "/findDataBaseEvaluationList")
    public MultipleResult<DatabaseEvaluationInfoModel> findDataBaseEvaluationList(@RequestParam(value = "did") Long did) {
        super.saveInfoLog();
        return new MultipleResult<DatabaseEvaluationInfoModel>()
                .success(dataBaseDetailService.findDataBaseEvaluationList(getSid(), did));
    }

}
