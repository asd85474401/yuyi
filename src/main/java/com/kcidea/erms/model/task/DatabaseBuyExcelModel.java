package com.kcidea.erms.model.task;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/12/8
 **/
@Data
public class DatabaseBuyExcelModel {
    /**
     * 数据库名称
     */
    @ExcelProperty(value = "数据库名称")
    private String name;

    /**
     * 语种
     */
    @ExcelProperty(value = "语种")
    private String language;

    /**
     * 纸电
     */
    @ExcelProperty(value = "纸电")
    private String paper;

    /**
     * 资源类型
     */
    @ExcelProperty(value = "资源类型")
    private String properties;

    /**
     * 数据库供应商
     */
    @ExcelProperty(value = "数据库供应商")
    private String company;

    /**
     * 数据库代理商
     */
    @ExcelProperty(value = "数据库代理商")
    private String agent;

    /**
     * 数据库链接地址
     */
    @ExcelProperty(value = "数据库链接地址")
    private String url;

    /**
     * 订购类型(新订、续订、买断)
     */
    @ExcelProperty(value = "订购类型(新订、续订、买断)")
    private String orderType;

    /**
     * 预计金额(人民币)
     */
    @ExcelProperty(value = "预计金额(人民币)")
    private String price;

    /**
     * 数据库性质
     */
    @ExcelProperty(value = "数据库性质")
    private String natureType;

    /**
     * 学科覆盖(选择的维度到教育部一级学科)
     */
    @ExcelProperty(value = "学科覆盖(选择的维度到教育部一级学科)")
    private String subjects;

    /**
     * 使用起止年
     */
    @ExcelProperty(value = "使用起止年")
    private String useYear;

    /**
     * 文献类型（期刊全文、图书全文重点标明）
     */
    @ExcelProperty(value = "文献类型（期刊全文、图书全文重点标明）")
    private String resourceType;

    // 以下不是必填项

    /**
     * 供应商联系人
     */
    @ExcelProperty(value = "供应商联系人")
    private String companyAttention;

    /**
     * 供应商联系人职位
     */
    @ExcelProperty(value = "供应商联系人职位")
    private String companyAttentionJob;

    /**
     * 供应商联系人职位
     */
    @ExcelProperty(value = "供应商联系人电话")
    private String companyAttentionPhone;

    /**
     * 供应商联系人职位
     */
    @ExcelProperty(value = "供应商联系人邮箱")
    private String companyAttentionMail;

    /**
     * 供应商联系人职位
     */
    @ExcelProperty(value = "代理商联系人")
    private String agentAttention;

    /**
     * 供应商联系人职位
     */
    @ExcelProperty(value = "代理商联系人职位")
    private String agentAttentionJob;

    /**
     * 供应商联系人职位
     */
    @ExcelProperty(value = "代理商联系人电话")
    private String agentAttentionPhone;

    /**
     * 供应商联系人职位
     */
    @ExcelProperty(value = "代理商联系人邮箱")
    private String agentAttentionMail;
}
