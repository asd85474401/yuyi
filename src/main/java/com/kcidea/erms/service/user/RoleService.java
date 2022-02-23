package com.kcidea.erms.service.user;

import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.model.user.RoleInfoModel;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/11
 **/
public interface RoleService {
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
     * @date 2021/11/12 11:07
     */
    PageResult<RoleInfoModel> findRoleList(Long sid, String name, Integer pageNum, Integer pageSize);

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
    String addRole(RoleInfoModel roleInfoModel, Long sid, Long userId);

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
    String updateRole(RoleInfoModel roleInfoModel, Long sid, Long userId);

    /**
     * 删除角色
     *
     * @param id  角色id
     * @param sid 学校id
     * @return 删除结果
     * @author yeweiwei
     * @date 2021/11/12 17:59
     */
    String deleteRole(Long id, Long sid);
}
