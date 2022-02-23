package com.kcidea.erms.domain.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/1/12 16:59
 **/
@Data
@Accessors(chain = true)
public class DatabaseAccessUrl implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 学校id
     */
    private Long sId;

    /**
     * 数据库id
     */
    private Long dId;

    /**
     * 访问类型
     */
    private String accessType;

    /**
     * 登录方式
     */
    private String loginType;

    /**
     * 链接
     */
    private String url;

    /**
     * 访问提示
     */
    private String accessTips;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    private Long updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    private static final long serialVersionUID = 1L;

    public DatabaseAccessUrl create(Long id, Long sId, Long dId, String accessType, String loginType, String url, String accessTips, String remark, Long createdBy, LocalDateTime createdTime, Long updatedBy, LocalDateTime updatedTime) {
        this.id = id;
        this.sId = sId;
        this.dId = dId;
        this.accessType = accessType;
        this.loginType = loginType;
        this.url = url;
        this.accessTips = accessTips;
        this.remark = remark;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        this.updatedBy = updatedBy;
        this.updatedTime = updatedTime;
        return this;
    }
}