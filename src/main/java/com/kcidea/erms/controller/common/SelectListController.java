package com.kcidea.erms.controller.common;

import com.kcidea.erms.aop.LoginCheck;
import com.kcidea.erms.common.result.MultipleResult;
import com.kcidea.erms.enums.database.EnumCheckState;
import com.kcidea.erms.enums.user.EnumLoginAction;
import com.kcidea.erms.model.common.DropDownModel;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.service.common.SelectListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/01/12
 **/
@Slf4j
@RestController
public class SelectListController extends BaseController {

    @Resource
    protected SelectListService selectListService;

    /**
     * 查询系统默认的数据库属性集合
     *
     * @return 数据库数据库属性集合
     * @author yeweiwei
     * @date 2021/11/24 19:13
     */
    @LoginCheck(action = EnumLoginAction.Normal)
    @GetMapping(value = "/findPropertySelectList")
    public MultipleResult<IdNameModel> findPropertySelectList() {
        this.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(selectListService.findPropertySelectList());
    }

    /**
     * 查询数据商集合
     *
     * @return 数据商集合
     * @author yeweiwei
     * @date 2021/11/24 19:45
     */
    @LoginCheck(action = EnumLoginAction.Normal)
    @GetMapping(value = "/findCompanySelectList")
    public MultipleResult<IdNameModel> findCompanySelectList() {
        this.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(selectListService.findCompanySelectList(getSid()));
    }

    /**
     * 查询代理商集合
     *
     * @return 代理商集合
     * @author yeweiwei
     * @date 2021/11/24 19:45
     */
    @LoginCheck(action = EnumLoginAction.Normal)
    @GetMapping(value = "/findAgentSelectList")
    public MultipleResult<IdNameModel> findAgentSelectList() {
        this.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(selectListService.findAgentSelectList(getSid()));
    }

    /**
     * 查询学科覆盖下拉
     *
     * @return 学科覆盖集合
     * @author majuehao
     * @date 2022/1/6 14:35
     **/
    @LoginCheck(action = EnumLoginAction.Normal)
    @GetMapping(value = "/findSubjectSelectList")
    public MultipleResult<DropDownModel> findSubjectSelectList() {
        this.saveInfoLog();
        return new MultipleResult<DropDownModel>().success(selectListService.findSubjectSelectList());
    }

    /**
     * 查询审核人员专用的审核状态下拉内容
     *
     * @return 审核人员专用的审核状态下拉内容
     * @author huxubin
     * @date 2022/1/14 14:35
     **/
    @LoginCheck(action = EnumLoginAction.Normal)
    @GetMapping(value = "/findCheckStateSelectByCheckUser")
    public MultipleResult<IdNameModel> findCheckStateSelectByCheckUser() {
        this.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(selectListService.findCheckStateSelectList(true));
    }

    /**
     * 查询通用的审核状态下拉内容
     *
     * @return 通用的审核状态下拉内容
     * @author huxubin
     * @date 2022/1/14 14:35
     **/
    @LoginCheck(action = EnumLoginAction.Normal)
    @GetMapping(value = "/findCheckStateSelectByNormalUser")
    public MultipleResult<IdNameModel> findCheckStateSelectByNormalUser() {
        this.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(selectListService.findCheckStateSelectList(false));
    }
}
