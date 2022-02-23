package com.kcidea.erms.domain.fund;

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
 * @date 2021/11/17
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PayRemindRel implements Serializable {

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
     * 需要提醒的用户id
     */
    private Long userId;

    /**
     * 禁用标识，0=未禁用 1=已禁用
     */
    private Integer disableFlag;

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

    public PayRemindRel create(Long sId, Long userId, Integer disableFlag, Long createdBy, LocalDateTime createdTime) {
        this.sId = sId;
        this.userId = userId;
        this.disableFlag = disableFlag;
        this.createdBy = createdBy;
        this.createdTime = createdTime;

        return this;
    }
}
