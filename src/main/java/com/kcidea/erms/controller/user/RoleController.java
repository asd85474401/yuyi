package com.kcidea.erms.controller.user;

import com.kcidea.erms.aop.ActionRights;
import com.kcidea.erms.common.result.MultipleResult;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.result.Result;
import com.kcidea.erms.controller.common.BaseController;
import com.kcidea.erms.enums.menu.EnumMenu;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.menu.MenuModel;
import com.kcidea.erms.model.menu.RoleMenuRelModel;
import com.kcidea.erms.model.user.RoleInfoModel;
import com.kcidea.erms.service.menu.RoleMenuRelService;
import com.kcidea.erms.service.user.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/11
 **/
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    @Resource
    private RoleMenuRelService roleMenuRelService;

    /**
     * 根据条件检索角色列表
     *
     * @param name     角色名称
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 角色列表
     * @author yeweiwei
     * @date 2021/11/12 11:06
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.角色管理)
    @GetMapping(value = "/findRoleList")
    public PageResult<RoleInfoModel> findRoleList(@RequestParam(value = "name", required = false)String name,
                                                  @RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
                                                  @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize){
        super.saveInfoLog();
        return roleService.findRoleList(getSid(), name, pageNum, pageSize);
    }

    /**
     * 新增角色
     *
     * @param roleInfoModel 角色信息
     * @return 新增结果
     * @author yeweiwei
     * @date 2021/11/12 13:32
     */
    @ActionRights(userAction = EnumUserAction.新增, menu = EnumMenu.角色管理)
    @PostMapping(value = "/addRole")
    public Result<String> addRole(@RequestBody @Valid RoleInfoModel roleInfoModel) {
        super.saveInfoLog();
        Long sid = getSid();
        Long userId = getUserId();
        return new Result<String>().successMsg(roleService.addRole(roleInfoModel, sid, userId));
    }

    /**
     * 修改角色
     *
     * @param roleInfoModel 角色信息
     * @return 修改结果
     * @author yeweiwei
     * @date 2021/11/12 13:32
     */
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.角色管理)
    @PostMapping(value = "/updateRole")
    public Result<String> updateRole(@RequestBody @Valid RoleInfoModel roleInfoModel) {
        super.saveInfoLog();
        Long sid = getSid();
        Long userId = getUserId();
        return new Result<String>().successMsg(roleService.updateRole(roleInfoModel, sid, userId));
    }

    /**
     * 删除角色
     *
     * @param id 角色id
     * @return 删除结果
     * @author yeweiwei
     * @date 2021/11/12 18:02
     */
    @ActionRights(userAction = EnumUserAction.删除, menu = EnumMenu.角色管理)
    @DeleteMapping(value = "/deleteRole")
    public Result<String> deleteRole(@RequestParam(value = "id") Long id) {
        super.saveInfoLog();
        Long sid = getSid();
        return new Result<String>().successMsg(roleService.deleteRole(id, sid));
    }

    /**
     * 查询所有菜单和角色对这些菜单的操作权限
     *
     * @param roleId 角色id
     * @return 菜单和角色操作权限集合
     * @author yeweiwei
     * @date 2021/11/12 18:30
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.角色管理)
    @GetMapping(value = "/findMenuListAndRoleRight")
    public MultipleResult<MenuModel> findMenuListAndRoleRight(@RequestParam(value = "roleId") Long roleId){
        super.saveInfoLog();
        return new MultipleResult<MenuModel>().success(roleMenuRelService.findMenuListAndRoleRight(roleId));
    }

    /**
     * 编辑用户权限
     *
     * @param roleMenuRelModel 用户权限集合
     * @return 编辑的结果
     * @author yeweiwei
     * @date 2021/6/2 11:04
     */
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.角色管理)
    @PostMapping(value = "/updateRoleMenuRel")
    public Result<String> updateRoleMenuRel(@RequestBody @Valid RoleMenuRelModel roleMenuRelModel){
        super.saveInfoLog();
        Long userId = getUserId();
        return new Result<String>().successMsg(roleMenuRelService.updateRoleMenuRel(roleMenuRelModel, userId));
    }
}
