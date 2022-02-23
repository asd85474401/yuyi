package com.kcidea.erms.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kcidea.erms.domain.user.Role;
import com.kcidea.erms.model.user.RoleInfoModel;
import com.kcidea.erms.model.common.IdNameModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/10
 **/
public interface RoleDao extends BaseMapper<Role> {
    /**
     * 按条件分页检索角色列表
     *
     * @param page 分页数据
     * @param sid  学校id
     * @param name 角色名称
     * @return 角色列表
     * @author yeweiwei
     * @date 2021/11/12 11:19
     */
    List<RoleInfoModel> findPageListByName(@Param("page") Page<RoleInfoModel> page, @Param("sid") Long sid, @Param("name") String name);

    /**
     * 根据学校id、角色名称查询一个角色
     *
     * @param sid  学校id
     * @param name 角色名称
     * @return 角色
     * @author yeweiwei
     * @date 2021/11/12 14:57
     */
    Role findOneBySidName(@Param("sid") Long sid, @Param("name") String name);

    /**
     * 删除角色
     *
     * @param id  角色id
     * @param sid 学校id
     * @author yeweiwei
     * @date 2021/11/12 18:01
     */
    void deleteByIdSid(@Param("id") Long id, @Param("sid") Long sid);

    /**
     * 根据id查角色名
     *
     * @param id 角色id
     * @return 角色名
     * @author majuehao
     * @date 2021/11/12 13:36
     **/
    String findNameById(@Param("id") Long id);

    /**
     * 根据学校id，查询角色下拉
     *
     * @param sid 学校id
     * @return 角色下拉列表
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    List<IdNameModel> findIdNameListBySid(@Param("sid") Long sid);
}
