package com.kcidea.erms.controller.fund;

import com.kcidea.erms.aop.ActionRights;
import com.kcidea.erms.common.result.MultipleResult;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.result.Result;
import com.kcidea.erms.controller.common.BaseController;
import com.kcidea.erms.enums.menu.EnumMenu;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.fund.ContractListModel;
import com.kcidea.erms.model.fund.ContractPayListModel;
import com.kcidea.erms.model.fund.PayRemindInfoModel;
import com.kcidea.erms.service.fund.ContractPayService;
import com.kcidea.erms.service.fund.ContractService;
import com.kcidea.erms.service.fund.SchoolBudgetService;
import com.kcidea.erms.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/16
 **/
@Slf4j
@RestController
@RequestMapping("/contract")
public class ContractController extends BaseController {

    @Resource
    private SchoolBudgetService schoolBudgetService;

    @Resource
    private ContractService contractService;

    @Resource
    private ContractPayService contractPayService;

    @Resource
    private UserService userService;

    /**
     * 获取学校预算来源下拉集合
     *
     * @return 预算来源下拉集合
     * @author yeweiwei
     * @date 2021/11/16 13:48
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.账簿管理)
    @GetMapping(value = "/findBudgetSelectList")
    public MultipleResult<IdNameModel> findBudgetSelectList() {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(schoolBudgetService.findBudgetSelectListBySid(getSid()));
    }

    /**
     * 获取带有合同的数据库下拉集合
     *
     * @return 数据库下拉集合
     * @author yeweiwei
     * @date 2021/11/16 13:48
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.账簿管理)
    @GetMapping(value = "/findDatabaseSelectList")
    public MultipleResult<IdNameModel> findDatabaseSelectList() {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(contractService.findDatabaseSelectList(getSid()));
    }

    /**
     * 获取订购类型下拉集合
     *
     * @return 订购类型下拉集合
     * @author yeweiwei
     * @date 2021/11/16 13:48
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.账簿管理)
    @GetMapping(value = "/findOrderTypeSelectList")
    public MultipleResult<IdNameModel> findOrderTypeSelectList() {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(contractService.findOrderTypeSelectList());
    }

    /**
     * 获取支付状态下拉集合
     *
     * @return 支付状态下拉集合
     * @author yeweiwei
     * @date 2021/11/16 13:48
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.账簿管理)
    @GetMapping(value = "/findPayStateSelectList")
    public MultipleResult<IdNameModel> findPayStateSelectList() {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(contractService.findPayStateSelectList());
    }

    /**
     * 分页查询合同
     *
     * @param name        合同名称
     * @param number      合同编号
     * @param did         数据库id
     * @param payStartDay 开始付款日期
     * @param payEndDay   截止付款日期
     * @param budgetId    预算id
     * @param vYear       年份
     * @param orderType   订购类型
     * @param payStates   支付状态
     * @param pageNum     页码
     * @param pageSize    每页数量
     * @return 分页数据
     * @author yeweiwei
     * @date 2021/11/16 19:16
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.账簿管理)
    @GetMapping(value = "/findContractList")
    public PageResult<ContractListModel> findContractList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "number", required = false) String number,
            @RequestParam(value = "did", required = false) Long did,
            @RequestParam(value = "payStartDay", required = false) String payStartDay,
            @RequestParam(value = "payEndDay", required = false) String payEndDay,
            @RequestParam(value = "budgetId", required = false) Long budgetId,
            @RequestParam(value = "vYear", defaultValue = "999") Integer vYear,
            @RequestParam(value = "orderType", defaultValue = "999") Integer orderType,
            @RequestParam(value = "payStates", defaultValue = "999") String payStates,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        super.saveInfoLog();
        super.checkYear(vYear);
        return contractService.findContractList(getSid(), name, number, did, payStartDay, payEndDay, budgetId,
                vYear, orderType, payStates, pageNum, pageSize);
    }

    /**
     * 分页查询合同支付记录
     *
     * @param contractId    合同id
     * @param invoiceNumber 发票编号
     * @param pageNum       页码
     * @param pageSize      每页数量
     * @return 合同支付记录
     * @author yeweiwei
     * @date 2021/11/16 19:53
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.账簿管理)
    @GetMapping(value = "/findContractPayList")
    public PageResult<ContractPayListModel> findContractPayList(
            @RequestParam(value = "contractId") Long contractId,
            @RequestParam(value = "invoiceNumber", required = false) String invoiceNumber,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        super.saveInfoLog();
        return contractPayService.findContractPayList(getSid(), contractId, invoiceNumber, pageNum, pageSize);
    }


    /**
     * 查询到期提醒
     *
     * @return 到期提醒
     * @author yeweiwei
     * @date 2021/11/17 9:55
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.账簿管理)
    @GetMapping(value = "/findPayRemind")
    public Result<PayRemindInfoModel> findPayRemind() {
        super.saveInfoLog();
        return new Result<PayRemindInfoModel>().success(contractPayService.findPayRemind(getSid()));
    }

    /**
     * 查询学校未禁用的用户下拉集合
     *
     * @return 用户下拉集合
     * @author yeweiwei
     * @date 2021/11/17 10:18
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.账簿管理)
    @GetMapping(value = "/findUserSelectList")
    public MultipleResult<IdNameModel> findUserSelectList() {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(userService.findUserSelectList(getSid()));
    }

    /**
     * 设置到期提醒
     *
     * @return 设置的结果
     * @author yeweiwei
     * @date 2021/11/17 9:55
     */
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.账簿管理)
    @PostMapping(value = "/updatePayRemind")
    public Result<String> updatePayRemind(@RequestBody @Valid PayRemindInfoModel payRemindInfo) {
        super.saveInfoLog();
        return new Result<String>().success(contractPayService.updatePayRemind(getSid(), payRemindInfo, getUserId()));
    }

    /**
     * 根据合同id删除
     *
     * @param contractId 合同id
     * @return 删除的结果
     * @author yeweiwei
     * @date 2021/11/17 19:05
     */
    @ActionRights(userAction = EnumUserAction.删除, menu = EnumMenu.账簿管理)
    @DeleteMapping(value = "/deleteContract")
    public Result<String> deleteContract(@RequestParam(value = "contractId") Long contractId) {
        super.saveInfoLog();
        return new Result<String>().success(contractService.deleteContract(getSid(), contractId));
    }

    /**
     * 导出合同列表
     *
     * @param name        合同名称
     * @param number      合同编号
     * @param did         数据库id
     * @param payStartDay 开始付款日期
     * @param payEndDay   截止付款日期
     * @param budgetId    预算id
     * @param vYear       年份
     * @param orderType   订购类型
     * @param payStates   支付状态
     * @return 导出的文件
     * @author yeweiwei
     * @date 2021/11/17 20:14
     */
    @ActionRights(userAction = EnumUserAction.导出, menu = EnumMenu.账簿管理)
    @GetMapping(value = "/exportContractList")
    public ResponseEntity<byte[]> exportContractList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "number", required = false) String number,
            @RequestParam(value = "did", required = false) Long did,
            @RequestParam(value = "payStartDay", required = false) String payStartDay,
            @RequestParam(value = "payEndDay", required = false) String payEndDay,
            @RequestParam(value = "budgetId", required = false) Long budgetId,
            @RequestParam(value = "vYear", defaultValue = "999") Integer vYear,
            @RequestParam(value = "orderType", defaultValue = "999") Integer orderType,
            @RequestParam(value = "payStates", defaultValue = "999") String payStates) {
        super.saveInfoLog();
        super.checkYear(vYear);
        return contractService.exportContractList(getSid(), name, number, did, payStartDay, payEndDay,
                budgetId, vYear, orderType, payStates);
    }
}
