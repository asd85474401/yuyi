package com.kcidea.erms.model.user;

import com.kcidea.erms.domain.user.User;
import com.kcidea.erms.model.menu.MenuModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/10
 **/
@Data
public class UserModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识id
     */
    private Long id;

    /**
     * 学校id
     */
    private Long sid;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 帐号
     */
    private String accountName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 登录标识token
     */
    private String token;

    /**
     * uuid
     */
    private String uuid;

    /**
     * 最近登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 本次登录时间
     */
    private LocalDateTime nowLoginTime;

    /**
     * 登录的ip
     */
    private String ipAddress;

    /**
     * 菜单的列表
     */
    private List<MenuModel> menuList;

    /**
     * 学校开始年份
     */
    private Integer startYear;

    /**
     * 快速创建对象
     *
     * @param user         用户
     * @param token        token
     * @param nowLoginTime 当前登录时间
     * @param ip           ip地址
     * @return 创建好的对象
     */
    public UserModel create(User user, String token, LocalDateTime nowLoginTime, String ip) {
        this.id = user.getId();
        this.sid = user.getSId();
        this.roleId = user.getRoleId();
        this.accountName = user.getAccountName();
        this.nickName = user.getNickName();
        this.lastLoginTime = user.getLastLoginTime();
        this.nowLoginTime = nowLoginTime;
        this.token = token;
        this.ipAddress = ip;
        return this;
    }
}
