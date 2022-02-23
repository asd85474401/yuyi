package com.kcidea.erms.model.fund;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/16
 **/
@Data
public class YearBudgetModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 年份
     */
    private int vYear;

    /**
     * 申报预算金额
     */
    private BigDecimal declarePrice;

    /**
     * 批复预算金额
     */
    private BigDecimal price;

    public YearBudgetModel create(int vYear, BigDecimal declarePrice, BigDecimal price) {
        this.vYear = vYear;
        this.declarePrice = declarePrice;
        this.price = price;
        return this;
    }
}
