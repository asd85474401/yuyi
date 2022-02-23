package com.kcidea.erms.model.database.evaluation;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kcidea.erms.model.database.AttachmentModel;
import com.kcidea.erms.model.database.detail.DatabaseInfoModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/25
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DatabaseEvaluationInfoModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ExcelIgnore
    private Long id;

    /**
     * 数据库评估id
     */
    @ExcelIgnore
    private Long evaluationId;

    /**
     * 数据库id
     */
    @ExcelIgnore
    @NotNull(message = "请选择数据库")
    private Long did;

    /**
     * 数据库信息
     */
    @ExcelIgnore
    @Valid
    private DatabaseInfoModel databaseInfoModel;

    /**
     * 年份
     */
    @ExcelIgnore
    @NotNull(message = "请选择年份")
    private Integer vyear;


    /**
     * 订购类型
     */
    @ExcelIgnore
    @NotNull(message = "请选择数据库订购类型")
    private Integer orderType;


    /**
     * 学科覆盖
     */
    @ExcelIgnore
    @NotEmpty(message = "请至少选择一个数据库学科覆盖")
    private List<Long> subjectList;

    /**
     * 供应商id
     */
    @ExcelIgnore
    private Long companyId;

    /**
     * 供应商名称
     */
    @NotBlank(message = "请选择供应商")
    @Length(max = 100, message = "供应商名称最大长度为{max}个字符")
    @ExcelProperty(value = "数据库供应商",index = 3)
    private String companyName;

    /**
     * 代理商id
     */
    @ExcelIgnore
    private Long agentId;

    /**
     * 代理商名称
     */
    @NotBlank(message = "请选择代理商")
    @Length(max = 150, message = "代理商名称最大长度为{max}个字符")
    @ExcelProperty(value = "数据库代理商",index = 4)
    private String agentName;

    /**
     * 登录方式
     */
    @ExcelIgnore
    @NotNull(message = "请选择登录方式")
    private Integer loginType;

    /**
     * 登录方式
     */
    @ExcelIgnore
    private String loginTypeName;

    /**
     * 数据时间
     */
    @NotNull(message = "请输入数据时间")
    @Length(max = 150, message = "数据时间最大长度为{max}个字符")
    @ExcelProperty(value = "数据时间",index = 5)
    private String dataTime;

    /**
     * 访问提示
     */
    @NotBlank(message = "请输入访问提示")
    @Length(max = 150, message = "访问提示最大长度为{max}个字符")
    @ExcelIgnore
    private String accessTips;

    /**
     * 学科名称
     */
    @ExcelIgnore
    private String subjects;

    /**
     * 是否全文
     */
    @ExcelIgnore
    @NotNull(message = "请选择是否全文")
    @Range(min = 0, max = 1, message = "全文只能选择是或否")
    private Integer fulltextFlag;

    /**
     * 是否全文
     */
    @ExcelProperty(value = "是否全文",index = 6)
    private String fulltextFlagName;

    /**
     * 评估结果(0=尚未评估 1=通过 2=未通过)
     */
    @ExcelIgnore
    private Integer resultType;

    /**
     * 评估结果名称
     */
    @ExcelProperty(value = "评估结果",index = 9)
    private String resultTypeName;

    /**
     * 评估结果
     */
    @ExcelIgnore
    private String resultTypeStr;

    /**
     * 附件列表
     */
    @ExcelIgnore
    private List<AttachmentModel> attachmentModelList;

    /**
     * 评估附件
     */
    @ExcelProperty(value = "评估附件",index = 10)
    private String resultAttachment;

    /**
     * 说明
     */
    @ExcelIgnore
    private String remark;

    /**
     * 数据库名称
     */
    @ExcelProperty(value = "数据库名称",index = 0)
    private String name;

    /**
     * 数据库名称
     */
    @ExcelIgnore
    private String dName;

    /**
     * 语种
     */
    @ExcelProperty(value = "语种",index = 1)
    private String language;

    /**
     * 资源类型
     */
    @ExcelProperty(value = "资源类型",index = 2)
    private String properties;

    /**
     * 学科门类
     */
    @ExcelProperty(value = "学科门类",index = 7)
    private String subjectCategory;

    /**
     * 一级学科
     */
    @ExcelProperty(value = "一级学科",index = 8)
    private String subjectOne;

    /**
     * 学校id
     */
    @ExcelIgnore
    private Long sid;

    /**
     * 评估时间
     */
    @ExcelIgnore
    private LocalDateTime time;

    public DatabaseEvaluationInfoModel create(DatabaseInfoModel databaseInfoModel, Integer vyear, Integer orderType,
                                              List<Long> subjectList, Long companyId, Long agentId, Integer loginType,
                                              String dataTime, String accessTips) {
        this.databaseInfoModel = databaseInfoModel;
        this.vyear = vyear;
        this.orderType = orderType;
        this.subjectList = subjectList;
        this.companyId = companyId;
        this.agentId = agentId;
        this.loginType = loginType;
        this.dataTime = dataTime;
        this.accessTips = accessTips;

        return this;
    }

    public DatabaseEvaluationInfoModel export(String name, String language, String properties, String accessTips,
                                              String subjectCategory, String subjectOne,
                                              String dataTime, String companyName, String agentName,
                                              String loginTypeName, String resultTypeName, String resultAttachment,
                                              String fulltextFlagName) {
        this.name = name;
        this.language = language;
        this.properties = properties;
        this.accessTips = accessTips;
        this.subjectCategory = subjectCategory;
        this.subjectOne = subjectOne;
        this.dataTime = dataTime;
        this.companyName = companyName;
        this.agentName = agentName;
        this.loginTypeName = loginTypeName;
        this.resultTypeName = resultTypeName;
        this.resultAttachment = resultAttachment;
        this.fulltextFlagName = fulltextFlagName;

        return this;
    }
}
