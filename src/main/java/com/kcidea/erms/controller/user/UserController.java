package com.kcidea.erms.controller.user;

import com.kcidea.erms.aop.ActionRights;
import com.kcidea.erms.common.result.MultipleResult;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.result.Result;
import com.kcidea.erms.controller.common.BaseController;
import com.kcidea.erms.enums.menu.EnumMenu;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.user.UserManageModel;
import com.kcidea.erms.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/10
 **/
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 人员管理列表
     *
     * @param nickName    姓名
     * @param roleId      角色id
     * @param disableFlag 禁用标识
     * @param pageNum     页码
     * @param pageSize    每页数量
     * @return 人员管理列表
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.人员管理)
    @RequestMapping(value = "/findUserManageList", method = RequestMethod.GET)
    public PageResult<UserManageModel> findUserManageList(
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "disableFlag", required = false) Long disableFlag,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        super.saveInfoLog();
        return userService.findUserManageList(nickName, roleId, disableFlag, getSid(), getUserId(), pageNum, pageSize);
    }

    /**
     * 查询角色下拉
     *
     * @return 角色下拉列表
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.人员管理)
    @RequestMapping(value = "/findRoleSelectList", method = RequestMethod.GET)
    public MultipleResult<IdNameModel> findRoleSelectList() {
        super.saveInfoLog();
        MultipleResult<IdNameModel> result = new MultipleResult<>();
        List<IdNameModel> ret = userService.findRoleSelectListBySid(getSid());
        result.success(ret);
        return result;
    }

    /**
     * 查询禁用标识下拉
     *
     * @return 禁用标识下拉列表
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.人员管理)
    @RequestMapping(value = "/findDisableFlagSelectList", method = RequestMethod.GET)
    public MultipleResult<IdNameModel> findDisableFlagSelectList() {
        super.saveInfoLog();
        MultipleResult<IdNameModel> result = new MultipleResult<>();
        List<IdNameModel> ret = userService.findDisableFlagSelectList();
        result.success(ret);
        return result;
    }


    /**
     * 编辑账户禁用状态
     *
     * @param userId      用户id
     * @param disableFlag 禁用标识
     * @return 更新的结果
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.人员管理)
    @RequestMapping(value = "/updateUserDisableFlag", method = RequestMethod.POST)
    public Result<String> findDisableFlagSelectList(@RequestParam(value = "userId") Long userId,
                                                    @RequestParam(value = "disableFlag") Integer disableFlag) {
        super.saveInfoLog();
        Result<String> result = new Result<>();
        result.successMsg(userService.updateUserDisableFlagByUserIdAdminSid(userId, disableFlag, getUserId(), getSid()));
        return result;
    }

    /**
     * 删除账号
     *
     * @param userId 用户id
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    @ActionRights(userAction = EnumUserAction.删除, menu = EnumMenu.人员管理)
    @RequestMapping(value = "/deleteUser", method = RequestMethod.DELETE)
    public Result<String> deleteUser(@RequestParam(value = "userId") Long userId) {
        super.saveInfoLog();
        Result<String> result = new Result<>();
        result.successMsg(userService.deleteUserByUserId(userId, getUserId(), getSid()));
        return result;
    }

    /**
     * 新增用户
     *
     * @param accountName 登录帐号
     * @param password    密码
     * @param nickName    昵称
     * @param roleId      角色id
     * @param email       邮箱地址
     * @param department  部门
     * @return 新增的结果
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    @ActionRights(userAction = EnumUserAction.新增, menu = EnumMenu.人员管理)
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public Result<String> addUser(@RequestParam(value = "accountName") String accountName,
                                  @RequestParam(value = "password") String password,
                                  @RequestParam(value = "nickName") String nickName,
                                  @RequestParam(value = "roleId") Long roleId,
                                  @RequestParam(value = "email", required = false) String email,
                                  @RequestParam(value = "department", required = false) String department) {
        super.saveInfoLog();
        Result<String> result = new Result<>();
        String ret = userService.addUser(accountName, password, nickName, roleId, email,
                department, getSid(), getUserId());
        result.successMsg(ret);
        return result;
    }

    /**
     * 编辑用户信息回显
     *
     * @param userId 用户id
     * @return 用户信息
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.人员管理)
    @RequestMapping(value = "/findOneUser", method = RequestMethod.GET)
    public Result<UserManageModel> findOneUser(@RequestParam(value = "userId") Long userId) {
        super.saveInfoLog();
        Result<UserManageModel> result = new Result<>();
        result.success(userService.findOneUser(userId));
        return result;
    }

    /**
     * 编辑用户
     *
     * @param userId      用户id
     * @param accountName 登录帐号
     * @param password    密码
     * @param nickName    昵称
     * @param roleId      角色id
     * @param email       邮箱地址
     * @param department  部门
     * @return 编辑的结果
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.人员管理)
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public Result<String> updateUser(@RequestParam(value = "userId") Long userId,
                                     @RequestParam(value = "accountName") String accountName,
                                     @RequestParam(value = "password") String password,
                                     @RequestParam(value = "nickName") String nickName,
                                     @RequestParam(value = "roleId") Long roleId,
                                     @RequestParam(value = "email", required = false) String email,
                                     @RequestParam(value = "department", required = false) String department) {
        super.saveInfoLog();
        Result<String> result = new Result<>();
        result.successMsg(userService.updateUser(userId, accountName, password, nickName,
                roleId, email, department, getSid(), getUserId()));
        return result;
    }
}
