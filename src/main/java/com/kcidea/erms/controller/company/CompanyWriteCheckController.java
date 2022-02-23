package com.kcidea.erms.controller.company;

import com.kcidea.erms.aop.ActionRights;
import com.kcidea.erms.common.result.MultipleResult;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.result.Result;
import com.kcidea.erms.controller.common.BaseController;
import com.kcidea.erms.enums.menu.EnumMenu;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.company.AccessUrlCheckModel;
import com.kcidea.erms.model.company.CompanyWriteCheckModel;
import com.kcidea.erms.model.company.ContactPeopleCheckModel;
import com.kcidea.erms.model.company.DataBaseInfoCheckModel;
import com.kcidea.erms.service.common.SelectListService;
import com.kcidea.erms.service.company.CompanyWriteCheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/01/14
 **/
@Slf4j
@RestController
@RequestMapping("/companyWriteCheck")
public class CompanyWriteCheckController extends BaseController {

    @Resource
    private CompanyWriteCheckService companyWriteCheckService;

    @Resource
    private SelectListService selectListService;

    /**
     * 查询数据商填写信息审核列表
     *
     * @param did       数据库
     * @param tableType 类型
     * @param state     审核状态
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @return 数据商填写信息审核列表
     * @author huxubin
     * @date 2021/10/18 10:27
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据商填写信息审核)
    @GetMapping(value = "/findCompanyWriteCheckList")
    public PageResult<CompanyWriteCheckModel> findCompanyWriteCheckList(
            @RequestParam(value = "did") Long did,
            @RequestParam(value = "tableType") Integer tableType,
            @RequestParam(value = "state") Integer state,
            @RequestParam(value = "pageNum") Integer pageNum,
            @RequestParam(value = "pageSize") Integer pageSize) {
        super.saveInfoLog();
        return companyWriteCheckService.findCompanyWriteCheckList(getSid(), did, tableType, state, pageNum, pageSize);
    }

    /**
     * 查询数据商填写审核的类型下拉
     *
     * @return 数据商填写审核的类型下拉
     * @author huxubin
     * @date 2022/1/14 14:36
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据商填写信息审核)
    @GetMapping(value = "/findTableTypeSelect")
    public MultipleResult<IdNameModel> findTableTypeSelect() {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(selectListService.findTableTypeSelect());
    }

    /**
     * 查询数据商填写审核的数据库下拉
     *
     * @return 数据商填写审核的数据库下拉
     * @author huxubin
     * @date 2022/1/14 14:36
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据商填写信息审核)
    @GetMapping(value = "/findDataBaseSelect")
    public MultipleResult<IdNameModel> findDataBaseSelect() {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(selectListService.findCompanyWriteCheckDataBaseSelect(getSid()));
    }

    /**
     * 审核数据商填写信息
     *
     * @param id     填写信息的id
     * @param state  审核状态
     * @param remark 审核说明
     * @return 修改的结果
     * @author majuehao
     * @date 2021/12/21 9:49
     **/
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.数据商填写信息审核)
    @PostMapping(value = "/updateCompanyWriteCheck")
    public Result<String> updateCompanyWriteCheck(@RequestParam(value = "id") Long id,
                                                  @RequestParam(value = "state") Integer state,
                                                  @RequestParam(value = "remark", required = false) String remark) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                companyWriteCheckService.updateCompanyWriteCheck(getSid(), getUserId(), id, state, remark));

    }

    /**
     * 查询数据商填写审核-》访问信息-》审核回显
     *
     * @param id 当前审核的记录id
     * @return 数据商填写审核-》访问信息-》审核回显
     * @author huxubin
     * @date 2022/1/18 11:02
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据商填写信息审核)
    @GetMapping(value = "/findAccessUrlCheck")
    public Result<AccessUrlCheckModel> findAccessUrlCheck(@RequestParam(value = "id") Long id) {
        super.saveInfoLog();
        return new Result<AccessUrlCheckModel>().success(
                companyWriteCheckService.findAccessUrlCheck(getSid(), id));
    }

    /**
     * 查询数据商填写审核-》库商信息(联系人)-》审核回显
     *
     * @param id 当前审核的记录id
     * @return 数据商填写审核-》库商信息(联系人)-》审核回显
     * @author huxubin
     * @date 2022/1/18 11:02
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据商填写信息审核)
    @GetMapping(value = "/findContactPeopleCheck")
    public Result<ContactPeopleCheckModel> findContactPeopleCheck(@RequestParam(value = "id") Long id) {
        super.saveInfoLog();
        return new Result<ContactPeopleCheckModel>().success(
                companyWriteCheckService.findContactPeopleCheck(getSid(), id));
    }

    /**
     * 查询数据商填写审核-》基本信息-》审核回显
     *
     * @param id 当前审核的记录id
     * @return 数据商填写审核-》基本信息-》审核回显
     * @author huxubin
     * @date 2022/1/18 11:02
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据商填写信息审核)
    @GetMapping(value = "/findBaseInfoCheck")
    public Result<DataBaseInfoCheckModel> findBaseInfoCheck(@RequestParam(value = "id") Long id) {
        super.saveInfoLog();
        return new Result<DataBaseInfoCheckModel>().success(
                companyWriteCheckService.findBaseInfoCheck(getSid(), id));
    }

    /**
     * 查询审核记录的状态
     *
     * @param id 当前审核的记录id
     * @return 审核记录的状态
     * @author huxubin
     * @date 2022/1/18 11:02
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据商填写信息审核)
    @GetMapping(value = "/findCompanyWriteState")
    public Result<Integer> findCompanyWriteState(@RequestParam(value = "id") Long id) {
        super.saveInfoLog();
        return new Result<Integer>().success(
                companyWriteCheckService.findCompanyWriteState(getSid(), id));
    }

}
