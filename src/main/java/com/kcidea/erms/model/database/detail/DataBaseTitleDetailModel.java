package com.kcidea.erms.model.database.detail;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/30
 **/
@Data
@Accessors(chain = true)
@ExcelIgnoreUnannotated
public class DataBaseTitleDetailModel implements Serializable {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 数据库id
     */
    @ExcelIgnore
    private Long dId;


    /**
     * 数据库名称
     */
    @ExcelIgnore
    private String dName;


    /**
     * 供应商id
     */
    @ExcelIgnore
    private Long companyId;

    /**
     * 资源类型
     */
    @ExcelIgnore
    private String properties;

    /**
     * 代理商id
     */
    @ExcelIgnore
    private Long agentId;

    /**
     * 学科门类
     */
    @ExcelIgnore
    private String subjectCategories;

    /**
     * 资源类型
     */
    @ExcelIgnore
    private String typeList;

    /**
     * 一级学科
     */
    @ExcelIgnore
    private String subjectList;

    /**
     * apiKey
     */
    @ExcelIgnore
    private String apiKey;

    /**
     * 子库数量
     */
    @ExcelIgnore
    private Integer sonCount;

    /**
     * 是否有子库的标识，1=有子库，0=无子库
     */
    @ExcelIgnore
    private Integer icon;

    /**
     * 数据库名称
     */
    @ExcelProperty(value = "数据库", index = 0)
    @ColumnWidth(value = 28)
    private String name;

    /**
     * 层级
     */
    @ExcelProperty(value = "层级", index = 1)
    @ColumnWidth(value = 5)
    private String level;

    /**
     * 子库数量
     */
    @ExcelProperty(value = "子库数量", index = 2)
    @ColumnWidth(value = 5)
    private String sonCountStr;

    /**
     * 语种
     */
    @ExcelProperty(value = "语种", index = 3)
    @ColumnWidth(value = 5)
    private String language;

    /**
     * 纸电
     */
    @ExcelProperty(value = "纸电", index = 4)
    @ColumnWidth(value = 5)
    private String paperFlag;

    /**
     * 所在地区
     */
    @ExcelProperty(value = "所在地区", index = 5)
    @ColumnWidth(value = 10)
    private String area;

    /**
     * 是否全文
     */
    @ExcelProperty(value = "是否全文", index = 6)
    @ColumnWidth(value = 3)
    private String fulltextFlag;

    /**
     * 资源类型
     */
    @ExcelProperty(value = "资源类型", index = 7)
    @ColumnWidth(value = 31)
    private String types;

    /**
     * 数据库性质
     */
    @ExcelProperty(value = "数据库性质", index = 8)
    @ColumnWidth(value = 15)
    private String natureType;

    /**
     * 访问信息
     */
    @ExcelProperty(value = "访问信息", index = 9)
    @ColumnWidth(value = 147)
    private String accessInfo;

    /**
     * 供应商
     */
    @ExcelProperty(value = "供应商", index = 10)
    @ColumnWidth(value = 22)
    private String companyName;

    /**
     * 供应商联系人
     */
    @ExcelProperty(value = "供应商联系人", index = 11)
    @ColumnWidth(value = 166)
    private String companyPeople;

    /**
     * 代理商
     */
    @ExcelProperty(value = "代理商", index = 12)
    @ColumnWidth(value = 22)
    private String agentName;

    /**
     * 代理商联系人
     */
    @ExcelProperty(value = "代理商联系人", index = 13)
    @ColumnWidth(value = 166)
    private String agentPeople;

    /**
     * 学科门类
     */
    @ExcelProperty(value = "学科门类", index = 14)
    @ColumnWidth(value = 23)
    private String subjectCategory;

    /**
     * 一级学科
     */
    @ExcelProperty(value = "一级学科", index = 15)
    @ColumnWidth(value = 23)
    private String subjects;

    /**
     * 数据时间
     */
    @ExcelProperty(value = "数据时间", index = 16)
    @ColumnWidth(value = 15)
    private String dataTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间", index = 17)
    @ColumnWidth(value = 24)
    private String updatedTime;

    /**
     * 资源总量
     */
    @ExcelProperty(value = "资源总量", index = 18)
    @ColumnWidth(value = 16)
    private String totalResource;

    /**
     * 资源容量
     */
    @ExcelProperty(value = "资源容量", index = 19)
    @ColumnWidth(value = 16)
    private String resourceCapacity;

    /**
     * 更新频率
     */
    @ExcelProperty(value = "更新频率", index = 20)
    @ColumnWidth(value = 16)
    private String updateFrequency;

    /**
     * 检索功能
     */
    @ExcelProperty(value = "检索功能", index = 21)
    @ColumnWidth(value = 16)
    private String search;

    /**
     * 并发数
     */
    @ExcelProperty(value = "并发数", index = 22)
    @ColumnWidth(value = 16)
    private String concurrency;

    /**
     * 使用指南
     */
    @ExcelProperty(value = "使用指南", index = 23)
    @ColumnWidth(value = 98)
    private String attachments;

    /**
     * 内容简介
     */
    @ExcelProperty(value = "内容简介", index = 24)
    @ColumnWidth(value = 255)
    private String introduce;

    private static final long serialVersionUID = 1L;

    public DataBaseTitleDetailModel create(Long dId, String language, String companyName, String area, String natureType,
                                           String agentName, String fulltextFlag, String types, String subjectCategory,
                                           String subjects, Integer sonCount, Integer icon, String sonCountStr,
                                           String updatedTime, String dataTime, String totalResource, String resourceCapacity,
                                           String updateFrequency, String search, String concurrency,
                                           String attachments, String introduce, String paperFlag) {
        this.dId = dId;
        this.language = language;
        this.companyName = companyName;
        this.area = area;
        this.natureType = natureType;
        this.agentName = agentName;
        this.fulltextFlag = fulltextFlag;
        this.types = types;
        this.subjectCategory = subjectCategory;
        this.subjects = subjects;
        this.sonCount = sonCount;
        this.icon = icon;
        this.sonCountStr = sonCountStr;
        this.updatedTime = updatedTime;
        this.dataTime = dataTime;
        this.totalResource = totalResource;
        this.resourceCapacity = resourceCapacity;
        this.updateFrequency = updateFrequency;
        this.search = search;
        this.concurrency = concurrency;
        this.attachments = attachments;
        this.introduce = introduce;
        this.paperFlag = paperFlag;

        return this;
    }
}
