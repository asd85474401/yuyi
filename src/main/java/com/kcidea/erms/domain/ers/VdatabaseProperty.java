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
public class VdatabaseProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 对应的自定义学校id
     */
    private Long sId;

    /**
     * 属性名
     */
    private String name;

    /**
     * 属性类型（语言、类型）
     */
    private Integer type;

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

    public void create(Long sId, String name, Integer type, Long createdBy, LocalDateTime createdTime) {
        this.sId = sId;
        this.name = name;
        this.type = type;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
    }
}
