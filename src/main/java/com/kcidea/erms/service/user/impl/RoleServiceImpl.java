package com.kcidea.erms.service.user.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.dao.menu.RoleMenuRelDao;
import com.kcidea.erms.dao.user.RoleDao;
import com.kcidea.erms.dao.user.UserDao;
import com.kcidea.erms.domain.user.Role;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.user.RoleInfoModel;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.user.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/11
 **/
@Slf4j
@Service
public class RoleServiceImpl extends BaseService implements RoleService {

    @Resource
    private RoleDao roleDao;

    @Resource
    private UserDao userDao;

    @Resource
    private RoleMenuRelDao roleMenuRelDao;

    /**
     * 根据条件检索角色列表
     *
     *
     * @param sid      学校id
     * @param name     角色名称
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 角色列表
     * @author yeweiwei
     * @date 2021/11/12 11:08
     */
    @Override
    public PageResult<RoleInfoModel> findRoleList(Long sid, String name, Integer pageNum, Integer pageSize) {
        //校验分页参数
        super.checkPageParam(pageNum, pageSize);
        super.checkSid(sid);
        //查询角色列表
        Page<RoleInfoModel> page = new Page<>(pageNum, pageSize);
        List<RoleInfoModel> list = roleDao.findPageListByName(page, sid, name);
        for (RoleInfoModel roleInfoModel : list) {
            if (!"超级管理员".equals(roleInfoModel.getName())){
                roleInfoModel.setOperationFlag(true);
            }
        }
        PageResult<RoleInfoModel> result = new PageResult<>();
        result.success(list, page.getTotal());
        return result;
    }

    /**
     * 新增角色
     *
     * @param roleInfoModel 角色信息
     * @param sid           学校id
     * @param userId        用户id
     * @return 新增的结果
     * @author yeweiwei
     * @date 2021/11/12 13:47
     */
    @Override
    public String addRole(RoleInfoModel roleInfoModel, Long sid, Long userId) {
        super.checkSid(sid);
        //检查名称是否重复
        String name = roleInfoModel.getName();
        Role role = roleDao.findOneBySidName(sid, name);
        if (null != role){
            throw new CustomException(Vm.EXIST_NAME);
        }
        role = new Role().create(sid, name, roleInfoModel.getRemark(), userId, LocalDateTime.now(), EnumUserAction.新增);
        roleDao.insert(role);

        return Vm.INSERT_SUCCESS;
    }

    /**
     * 更新角色
     *
     * @param roleInfoModel 角色信息
     * @param sid           学校id
     * @param userId        用户id
     * @return 更新的结果
     * @author yeweiwei
     * @date 2021/11/12 17:50
     */
    @Override
    public String updateRole(RoleInfoModel roleInfoModel, Long sid, Long userId) {
        super.checkSid(sid);
        //更新，id不能为空
        Long id = roleInfoModel.getId();
        if (null == id){
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        //检查名称是否重复
        String name = roleInfoModel.getName();
        Role role = roleDao.findOneBySidName(sid, name);
        if (null != role && !role.getId().equals(id)){
            throw new CustomException(Vm.EXIST_NAME);
        }
        role = new Role().create(sid, name, roleInfoModel.getRemark(), userId, LocalDateTime.now(), EnumUserAction.修改);
        role.setId(id);
        roleDao.updateById(role);

        return Vm.UPDATE_SUCCESS;
    }

    /**
     * 删除角色
     *
     * @param id  角色id
     * @param sid 学校id
     * @return 删除结果
     * @author yeweiwei
     * @date 2021/11/12 17:59
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteRole(Long id, Long sid) {
        super.checkSid(sid);
        //判断该角色是否有对应的用户
        Integer count = userDao.findCountByRoleId(id);
        if (count > 0){
            throw new CustomException("很抱歉，存在此角色对应的用户，请先删除拥有此角色的用户！");
        }

        //删除角色对应的权限
        roleMenuRelDao.deleteByRoleId(id);

        roleDao.deleteByIdSid(id, sid);

        return Vm.DELETE_SUCCESS;
    }
}
