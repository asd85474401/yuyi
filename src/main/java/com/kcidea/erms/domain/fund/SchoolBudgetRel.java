package com.kcidea.erms.domain.fund;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/15
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SchoolBudgetRel implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 学校id
     */
    private Long sId;

    /**
     * 预算id
     */
    private Long budgetId;

    /**
     * 年份
     */
    private Integer vyear;

    /**
     * 申报预算
     */
    private BigDecimal declarePrice;

    /**
     * 总经费
     */
    private BigDecimal totalPrice;

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

    public SchoolBudgetRel create(Long sId, Long budgetId, Integer vyear, BigDecimal declarePrice,
                                  BigDecimal totalPrice, Long createdBy, LocalDateTime createdTime) {
        this.sId = sId;
        this.budgetId = budgetId;
        this.vyear = vyear;
        this.declarePrice = declarePrice;
        this.totalPrice = totalPrice;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        return this;
    }

    public SchoolBudgetRel update(Long id, Long sId, Long budgetId, Integer vyear, BigDecimal declarePrice,
                                  BigDecimal totalPrice, Long updatedBy, LocalDateTime updatedTime) {
        this.id = id;
        this.sId = sId;
        this.budgetId = budgetId;
        this.vyear = vyear;
        this.declarePrice = declarePrice;
        this.totalPrice = totalPrice;
        this.updatedBy = updatedBy;
        this.updatedTime = updatedTime;
        return this;
    }
}
