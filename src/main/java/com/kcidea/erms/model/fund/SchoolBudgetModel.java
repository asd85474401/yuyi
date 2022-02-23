package com.kcidea.erms.model.fund;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/15
 **/
@Data
public class SchoolBudgetModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 预算id
     */
    private Long budgetId;

    /**
     * 预算来源
     */
    private String name;

    /**
     * 年份
     */
    private Integer vYear;

    /**
     * 申报预算
     */
    private BigDecimal declarePrice;

    /**
     * 总金额
     */
    private BigDecimal totalPrice;

    /**
     * 已支付金额
     */
    private BigDecimal paidPrice;

    /**
     * 待支付金额
     */
    private BigDecimal unpaidPrice;

    /**
     * 剩余金额
     */
    private BigDecimal surplusPrice;
}
