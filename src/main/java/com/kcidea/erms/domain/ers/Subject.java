package com.kcidea.erms.domain.ers;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.kcidea.erms.common.constant.Constant;
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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 学科名字
     */
    private String name;

    /**
     * 父级学科的名字
     */
    private Long parentId;

    /**
     * 学科类别
     */
    private Long categoryType;

    /**
     * 学科排序
     */
    private Integer sort;

    /**
     * 学科等级
     */
    private Integer treeLevel;

    /**
     * 学科门类
     */
    private Long subjectCategory;

    /**
     * 备注
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


    public Subject createComprehensive() {
        this.name = Constant.Subject.COMPREHENSIVE_SUBJECT_STRING;
        this.id = Constant.Subject.COMPREHENSIVE_SUBJECT_LONG;
        this.subjectCategory = Constant.Subject.COMPREHENSIVE_SUBJECT_LONG;
        this.treeLevel = 0;
        this.parentId = 0L;
        return this;
    }
}
