package com.kcidea.erms.service.user;

import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.domain.user.User;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.user.UserManageModel;
import com.kcidea.erms.model.user.UserModel;

import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/10
 **/
public interface UserService {

    /**
     * 根据帐号查询用户
     *
     * @param account 帐号
     * @return 用户
     * @author huxubin
     * @date 2021/11/11 13:49
     */
    User findOneByAccount(String account);

    /**
     * 保存登录的信息
     *
     * @param user 用户
     * @param ip   ip地址
     * @return 保存的后的用户对象
     * @author huxubin
     * @date 2021/11/10 17:04
     */
    UserModel saveLoginData(User user, String ip);

    /**
     * 人员管理列表
     *
     * @param nickName    姓名
     * @param roleId      角色id
     * @param disableFlag 禁用标识
     * @param sid         学校id
     * @param userId      操作人id
     * @param pageNum     页码
     * @param pageSize    每页数量
     * @return 人员管理列表
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    PageResult<UserManageModel> findUserManageList(String nickName, Long roleId, Long disableFlag, Long sid, Long userId,
                                                   Integer pageNum, Integer pageSize);

    /**
     * 根据学校id，查询角色下拉
     *
     * @param sid 学校id
     * @return 角色下拉列表
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    List<IdNameModel> findRoleSelectListBySid(Long sid);

    /**
     * 查询禁用标识下拉
     *
     * @return 禁用标识下拉列表
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    List<IdNameModel> findDisableFlagSelectList();

    /**
     * 编辑账户禁用状态
     *
     * @param userId      用户id
     * @param disableFlag 禁用标识
     * @param adminId     操作用户
     * @param sid         学校id
     * @return 更新的结果
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    String updateUserDisableFlagByUserIdAdminSid(Long userId, Integer disableFlag, Long adminId, Long sid);

    /**
     * 删除账号
     *
     * @param userId  用户id
     * @param adminId 操作人id
     * @param sid     学校id
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    String deleteUserByUserId(Long userId, Long adminId, Long sid);

    /**
     * 新增用户
     *
     * @param accountName 登录帐号
     * @param password    密码
     * @param nickName    昵称
     * @param roleId      角色id
     * @param email       邮箱地址
     * @param department  部门
     * @param sid         学校id
     * @param adminId     操作人id
     * @return 新增的结果
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    String addUser(String accountName, String password, String nickName, Long roleId, String email,
                   String department, Long sid, Long adminId);

    /**
     * 编辑用户信息回显
     *
     * @param userId 用户id
     * @return 用户信息
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    UserManageModel findOneUser(Long userId);

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
     * @param sid         学校id
     * @param adminId     操作人id
     * @return 编辑的结果
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    String updateUser(Long userId, String accountName, String password, String nickName, Long roleId, String email,
                      String department, Long sid, Long adminId);

    /**
     * 查询学校未禁用的用户下拉集合
     *
     * @param sid 学校id
     * @return 用户下拉集合
     * @author yeweiwei
     * @date 2021/11/17 10:18
     */
    List<IdNameModel> findUserSelectList(Long sid);
}
