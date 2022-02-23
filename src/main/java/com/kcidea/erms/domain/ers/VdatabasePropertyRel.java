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
public class VdatabasePropertyRel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 数据库id
     */
    private Long dId;

    /**
     * 语种id
     */
    private Long languageId;

    /**
     * 属性id
     */
    private Long propertyId;

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

    /**
     * 属性排序标志（越小越关联）
     */
    private Integer rank;

    /**
     * 纸本标志(是为纸本，否为电子)
     */
    private Integer paperFlag;

    /**
     * 自定义学校id
     */
    private Long sId;

    /**
     * 属性的来源 0=默认 1=子库关联插入
     */
    private Integer childInsert;

    public VdatabasePropertyRel create(Long sId, Long dId, Long languageId, Long propertyId, Integer paperFlag,
                                       Long createdBy, LocalDateTime createdTime) {
        this.sId = sId;
        this.dId = dId;
        this.languageId = languageId;
        this.propertyId = propertyId;
        this.paperFlag = paperFlag;
        this.createdBy = createdBy;
        this.createdTime = createdTime;

        return this;
    }
}
