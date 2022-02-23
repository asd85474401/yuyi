package com.kcidea.erms.domain.fund;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/15
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SchoolBudget implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 来源名称
     */
    private String name;

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

    public SchoolBudget create(Long sId, String name, Long createdBy, LocalDateTime createdTime) {
        this.sId = sId;
        this.name = name;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        return this;
    }

    public SchoolBudget update(Long id, Long sId, String name, Long updatedBy, LocalDateTime updatedTime) {
        this.id = id;
        this.sId = sId;
        this.name = name;
        this.updatedBy = updatedBy;
        this.updatedTime = updatedTime;
        return this;
    }
}
