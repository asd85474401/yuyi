package com.kcidea.erms.domain.ers;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/24
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SchoolDatabaseSubjectRel implements Serializable {

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
     * 数据库id
     */
    private Long dId;

    /**
     * 学科id
     */
    private Long subjectId;

    /**
     * 学科门类
     */
    private Long subjectCategory;

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

    public SchoolDatabaseSubjectRel create(Long sId, Long dId, Long subjectId, Long subjectCategory, Long createdBy,
                                           LocalDateTime createdTime) {
        this.sId = sId;
        this.dId = dId;
        this.subjectId = subjectId;
        this.subjectCategory = subjectCategory;
        this.createdBy = createdBy;
        this.createdTime = createdTime;

        return this;
    }
}