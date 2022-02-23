package com.kcidea.erms.model.database.stopinfo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kcidea.erms.model.database.AttachmentDontCheckModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/24
 **/
@Data
public class DatabaseStopInfoModel implements Serializable {

    /**
     * 主键id
     */
    @ExcelIgnore
    private Long id;

    /**
     * 数据库id
     */
    @NotNull
    @ExcelIgnore
    private Long dId;

    /**
     * 年份
     */
    @NotNull
    @ExcelIgnore
    private Integer vyear;

    /**
     * 停订日期
     */
    @NotNull
    @ExcelIgnore
    private LocalDate stopDay;

    /**
     * 停订日期
     */
    @ExcelProperty(value = "停订时间")
    private String stopDayStr;

    /**
     * 停订原因
     */
    @NotBlank
    @Length(max = 500)
    @ExcelProperty(value = "停订原因")
    private String remark;

    /**
     * 附件列表
     */
    @ExcelIgnore
    private List<AttachmentDontCheckModel> attachmentModelList;

    /**
     * 数据库名称
     */
    @ExcelProperty(value = "数据库")
    private String databaseName;

    /**
     * 数据库属语种
     */
    @ExcelProperty(value = "语种")
    private String language;

    /**
     * 数据库类型名称
     */
    @ExcelProperty(value = "资源类型")
    private String types;

    /**
     * 订购年份
     */
    @ExcelProperty(value = "订购年份")
    private String orderYears;

    /**
     * 附件
     */
    @ExcelProperty(value = "附件")
    private String attachmentUrl;

    private static final long serialVersionUID = 1L;
}
