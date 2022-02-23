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
 * @date 2022/1/12 9:17
 **/
@Data
@Accessors(chain = true)
public class DatabaseLevel implements Serializable {
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
     * 总库标识（0=子库 1=总库）
     */
    private Integer totalFlag;

    /**
     * 总库的id
     */
    private Long totalDid;

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

    public DatabaseLevel create(Long sId, Long dId, Integer totalFlag, Long totalDid, String remark, Long createdBy, LocalDateTime createdTime) {
        this.sId = sId;
        this.dId = dId;
        this.totalFlag = totalFlag;
        this.totalDid = totalDid;
        this.remark = remark;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        return this;
    }

    public DatabaseLevel update(Integer totalFlag, Long totalDid, Long updatedBy, LocalDateTime updatedTime) {
        this.totalFlag = totalFlag;
        this.totalDid = totalDid;
        this.updatedBy = updatedBy;
        this.updatedTime = updatedTime;
        return this;
    }
}