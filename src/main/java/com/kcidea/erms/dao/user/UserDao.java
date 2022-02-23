package com.kcidea.erms.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kcidea.erms.domain.user.User;
import com.kcidea.erms.model.user.UserManageModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/10
 **/
public interface UserDao extends BaseMapper<User> {

    /**
     * 根据帐号查询用户
     *
     * @param account 帐号
     * @return 查询的结果
     * @author huxubin
     * @date 2021/11/10 16:31
     */
    User findOneByAccount(@Param("account") String account);

    /**
     * 根据姓名、角色id、禁用标识、学校id，分页查询人员管理列表
     *
     * @param nickName    姓名
     * @param roleId      角色id
     * @param disableFlag 禁用标识
     * @param sid         学校id
     * @param page        分页
     * @return 人员管理列表
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    List<UserManageModel> findListByNickNameRoleIdDisableFlagSidPage(@Param("nickName") String nickName,
                                                                     @Param("roleId") Long roleId,
                                                                     @Param("disableFlag") Long disableFlag,
                                                                     @Param("sid") Long sid,
                                                                     @Param("page") Page<UserManageModel> page);


    /**
     * 根据用户id，查询用户数量
     *
     * @param sid         学校id
     * @param accountName 帐号
     * @return 用户数量
     * @author majuehao
     * @date 2021/11/12 15:28
     **/
    int findCountBySidAccountName(@Param("sid") Long sid, @Param("accountName") String accountName);

    /**
     * 根据角色id查询用户数量
     *
     * @param roleId 角色id
     * @return 用户数量
     * @author yeweiwei
     * @date 2021/11/12 19:21
     */
    Integer findCountByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询学校用户集合
     *
     * @param sid         学校id
     * @param disableFlag 禁用标识
     * @return 学校用户集合
     * @author yeweiwei
     * @date 2021/11/17 10:20
     */
    List<User> findListBySidDisableFlag(@Param("sid") Long sid, @Param("disableFlag") Integer disableFlag);

    /**
     * 根据学校id和用户id，查询用户详情
     *
     * @param sid    学校id
     * @param userId 用户id
     * @return 用户详情
     * @author majuehao
     * @date 2021/11/22 16:35
     **/
    User findOneByUserIdSid(@Param("sid") Long sid, @Param("userId") Long userId);
}
