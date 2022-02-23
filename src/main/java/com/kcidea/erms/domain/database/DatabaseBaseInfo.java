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
 * @date 2022/1/5 13:40
 **/
@Data
@Accessors(chain = true)
public class DatabaseBaseInfo implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    protected Long id;

    /**
     * 学校id
     */
    private Long sId;

    /**
     * 数据库id
     */
    private Long dId;

    /**
     * 地区
     */
    private String area;

    /**
     * 全文标识（0=不是 1=是）
     */
    private Integer fulltextFlag;

    /**
     * 数据库性质
     */
    private String natureType;

    /**
     * 数据时间
     */
    private String dataTime;

    /**
     * 供应商id
     */
    private Long companyId;

    /**
     * 代理商id
     */
    private Long agentId;

    /**
     * 资源总量
     */
    private String totalResource;

    /**
     * 资源容量
     */
    private String resourceCapacity;

    /**
     * 更新频率
     */
    private String updateFrequency;

    /**
     * 检索功能
     */
    private String search;

    /**
     * 并发数
     */
    private String concurrency;

    /**
     * 内容简介
     */
    private String introduce;

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

    public DatabaseBaseInfo create(Long sId, Long dId, Long createdBy, LocalDateTime createdTime) {
        this.sId = sId;
        this.dId = dId;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        return this;
    }
}