package com.kcidea.erms.model.fund;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/15
 **/
@Data
public class SchoolBudgetExportModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预算来源id
     */
    @ExcelIgnore
    private Long budgetId;

    /**
     * 预算来源
     */
    @ColumnWidth(value = 22)
    @ExcelProperty(value = {"预算来源"})
    private String name;

    /**
     * 申报预算
     */
    @ColumnWidth(value = 24)
    @ExcelProperty(value = {"申报预算金额(万元)"})
    private String declarePrice;

    /**
     * 批复预算金额
     */
    @ColumnWidth(value = 24)
    @ExcelProperty(value = {"批复预算金额(万元)"})
    private String totalPrice;

    /**
     * 已支付金额
     */
    @ColumnWidth(value = 22)
    @ExcelProperty(value = {"已支付金额(万元)"})
    private String paidPrice;

    /**
     * 待支付金额
     */
    @ColumnWidth(value = 22)
    @ExcelProperty(value = {"待支付金额(万元)"})
    private String unpaidPrice;

    /**
     * 剩余金额
     */
    @ColumnWidth(value = 22)
    @ExcelProperty(value = {"剩余金额(万元)"})
    private String surplusPrice;
}
