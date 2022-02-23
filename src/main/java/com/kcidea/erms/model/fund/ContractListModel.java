package com.kcidea.erms.model.fund;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/16
 **/
@Data
public class ContractListModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合同id
     */
    @ExcelIgnore
    private Long contractId;

    /**
     * 合同名称
     */
    @ExcelProperty(value = "合同名称")
    @ColumnWidth(value = 12)
    private String contractName;

    /**
     * 合同编号
     */
    @ExcelProperty(value = "合同编号")
    @ColumnWidth(value = 12)
    private String number;

    /**
     * 数据库名称集合
     */
    @ExcelIgnore
    private List<String> dbNameList;

    /**
     * 数据库名称，用逗号分割
     */
    @ExcelProperty(value = "数据库")
    @ColumnWidth(value = 22)
    private String databaseName;

    /**
     * 订购类型
     */
    @ExcelProperty(value = "订购类型")
    @ColumnWidth(value = 12)
    private String orderType;

    /**
     * 合同金额（人名币）
     */
    @ExcelProperty(value = "合同金额（人民币/万元）")
    @ColumnWidth(value = 30)
    private String rmbPrice;

    /**
     * 付款开始日期
     */
    @ExcelProperty(value = "付款开始日期")
    @ColumnWidth(value = 22)
    private String payStartDay;

    /**
     * 付款截止日期
     */
    @ExcelProperty(value = "付款截止日期")
    @ColumnWidth(value = 22)
    private String payEndDay;

    /**
     * 经费来源
     */
    @ExcelProperty(value = "经费来源")
    @ColumnWidth(value = 12)
    private String budgetName;

    /**
     * 支付状态
     */
    @ExcelIgnore
    private Integer payState;

    /**
     * 支付状态
     */
    @ExcelProperty(value = "支付状态")
    @ColumnWidth(value = 12)
    private String payStateStr;

    /**
     * 已支付金额
     */
    @ExcelProperty(value = "已支付金额（万元）")
    @ColumnWidth(value = 25)
    private String paidPrice;

    /**
     * 待支付金额
     */
    @ExcelProperty(value = "待支付金额（万元）")
    @ColumnWidth(value = 25)
    private String unPaidPrice;
}
