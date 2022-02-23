package com.kcidea.erms.model.task;

import com.alibaba.excel.annotation.ExcelProperty;
import com.kcidea.erms.enums.task.EnumTaskResult;
import lombok.Data;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/21
 **/
@Data
public class FeedbackExcelModel {

    /**
     * 反馈人
     */
    @ExcelProperty(value = "反馈人")
    private String name;

    /**
     * 用户身份
     */
    @ExcelProperty(value = "用户身份")
    private String identity;

    /**
     * 所属单位
     */
    @ExcelProperty(value = "所属单位")
    private String unit;

    /**
     * 联系邮箱
     */
    @ExcelProperty(value = "联系邮箱")
    private String email;

    /**
     * 联系电话
     */
    @ExcelProperty(value = "联系电话")
    private String phone;

    /**
     * 反馈时间
     */
    @ExcelProperty(value = "反馈时间")
    private String feedbackTime;

    /**
     * 反馈类型
     */
    @ExcelProperty(value = "反馈类型")
    private String type;

    /**
     * 数据库
     */
    @ExcelProperty(value = "数据库")
    private String databaseName;

    /**
     * 反馈标题
     */
    @ExcelProperty(value = "反馈标题")
    private String feedbackTitle;

    /**
     * 反馈内容
     */
    @ExcelProperty(value = "反馈内容")
    private String feedbackContent;

    /**
     * 回复人
     */
    @ExcelProperty(value = "回复人")
    private String answerName;

    /**
     * 回复时间
     */
    @ExcelProperty(value = "回复时间")
    private String answerTime;

    /**
     * 回复内容
     */
    @ExcelProperty(value = "回复内容")
    private String answerContent;

    /**
     * 导入结果
     */
    @ExcelProperty(value = "导入结果")
    private String result;

    /**
     * 失败原因
     */
    @ExcelProperty(value = "失败原因")
    private String errorMessage;

    public void createResult(EnumTaskResult result, String errorMessage) {
        this.result = result.getValue();
        this.errorMessage = errorMessage;
    }
}
