package com.kcidea.erms.domain.database;

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
public class DatabaseEvaluation implements Serializable {

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
     * 评估年份
     */
    private Integer vyear;

    /**
     * 订购类型
     */
    private Integer orderType;

    /**
     * 评估时间
     */
    private LocalDateTime time;

    /**
     * 评估结果(0=尚未评估 1=通过 2=未通过)
     */
    private Integer resultType;

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

    public DatabaseEvaluation create(Long sId, Long dId, Integer vyear, Integer orderType, Integer resultType,
                                     Long createdBy, LocalDateTime createdTime) {
        this.sId = sId;
        this.dId = dId;
        this.vyear = vyear;
        this.orderType = orderType;
        this.resultType = resultType;
        this.createdBy = createdBy;
        this.createdTime = createdTime;

        return this;
    }
}
