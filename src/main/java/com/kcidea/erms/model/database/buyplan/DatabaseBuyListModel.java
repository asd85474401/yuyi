package com.kcidea.erms.model.database.buyplan;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/12/7
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DatabaseBuyListModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据库采购id
     */
    @ExcelIgnore
    private Long id;

    /**
     * 数据库名称
     */
    @ExcelIgnore
    private String dName;

    /**
     * 采购计划名称
     */
    @ExcelIgnore
    private String title;

    /**
     * 年份
     */
    @ExcelIgnore
    private Integer vyear;

    /**
     * 资源类型
     */
    @ExcelIgnore
    private String properties;

    /**
     * 覆盖学科
     */
    @ExcelIgnore
    private String subjects;

    /**
     * 数据库性质
     */
    @ExcelIgnore
    private Integer natureType;

    /**
     * 数据库性质
     */
    @ExcelIgnore
    private String natureTypeStr;

    /**
     * 文献类型
     */
    @ExcelIgnore
    private String resourceType;

    /**
     * 预计金额（人名币/万元）
     */
    @ExcelIgnore
    private BigDecimal price;

    /**
     * 采购类型
     */
    @ExcelIgnore
    private Integer buyType;

    /**
     * 采购类型
     */
    @ExcelIgnore
    private String buyTypeStr;

    /**
     * 审核结果
     */
    @ExcelIgnore
    private Integer state;

    /**
     * 审核结果
     */
    @ExcelIgnore
    private String stateStr;

    /**
     * 审核结果
     */
    @ExcelIgnore
    private String stateName;

    /**
     * 数据库id
     */
    @ExcelIgnore
    private Long did;

    /**
     * 数据库
     */
    @ColumnWidth(value = 21)
    @ExcelProperty(value = {"数据库"})
    private String name;

    /**
     * 语种
     */
    @ColumnWidth(value = 6)
    @ExcelProperty(value = {"语种"})
    private String language;

    /**
     * 资源类型
     */
    @ColumnWidth(value = 12)
    @ExcelProperty(value = {"资源类型"})
    private String type;

    /**
     * 是否全文
     */
    @ColumnWidth(value = 12)
    @ExcelProperty(value = {"是否全文"})
    private String fullTextFlag;

    /**
     * 订购状态
     */
    @ColumnWidth(value = 12)
    @ExcelProperty(value = {"订购状态"})
    private String orderType;

    /**
     * 供应商
     */
    @ColumnWidth(value = 21)
    @ExcelProperty(value = {"供应商"})
    private String company;

    /**
     * 代理商
     */
    @ColumnWidth(value = 21)
    @ExcelProperty(value = {"代理商"})
    private String agent;

    /**
     * 预计金额(人民币/万元)
     */
    @ColumnWidth(value = 13)
    @ExcelProperty(value = {"预计金额(人民币/万元)"})
    private String priceStr;

    /**
     * 采购原因
     */
    @ColumnWidth(value = 30)
    @ExcelProperty(value = {"采购原因"})
    private String remark;

    /**
     * 审核结果
     */
    @ColumnWidth(value = 12)
    @ExcelProperty(value = {"审核结果"})
    private String checkResult;

    /**
     * 数据库数量
     */
    @ExcelIgnore
    private Integer databaseCount;

    /**
     * 预算金额(人民币/万元)
     */
    @ExcelIgnore
    private BigDecimal money;
}
