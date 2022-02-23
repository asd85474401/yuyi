package com.kcidea.erms.model.task;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.kcidea.erms.enums.task.EnumTaskResult;
import lombok.Data;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/29
 **/
@Data
public class DatabaseInfoExcelModel {

    /**
     * 数据库
     */
    @ExcelProperty(value = "数据库")
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
     * 所在地区
     **/
    @ExcelProperty(value = "所在地区")
    private String area;

    /**
     * 是否全文
     **/
    @ExcelProperty(value = "是否全文")
    private String fulltextFlag;

    /**
     * 资源类型
     */
    @ExcelProperty(value = "资源类型")
    private String properties;

    /**
     * 数据库性质
     */
    @ExcelProperty(value = "数据库性质")
    private String natureType;

    /**
     * 导入结果
     */
    @ExcelProperty(value = "导入结果")
    private String result;

    /**
     * 失败原因
     */
    @ExcelProperty(value = "失败原因")
    @ColumnWidth(value = 50)
    private String errorMessage;

    public void createResult(EnumTaskResult result, String errorMessage) {
        this.result = result.getValue();
        this.errorMessage = errorMessage;
    }
}
