package com.kcidea.erms.model.company;

import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.model.database.AttachmentDontCheckModel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/1/5 13:40
 **/
@Data
@Accessors(chain = true)
public class TotalDatabaseInfoModel implements Serializable {

    /**
     * apiKey
     */
    @NotBlank(message = Vm.ERROR_PARAMS)
    private String apiKey;

    /**
     * 数据库id
     */
    private Long did;

    /**
     * 总库id
     */
    private Long totalDid;

    /**
     * 是否暂存   1=暂存，0=不暂存
     **/
    private Integer tempSaveFlag;

    /**
     * 数据库名称
     */
    @Length(max = 500, message = "数据库名称最大长度为{max}个字符")
    @Pattern(regexp = "^$|[\\u4e00-\\u9fa5A-Za-z].*$", message = "数据库名称只能以字母或汉字开头")
    private String name;

    /**
     * 数据库语种
     */
    @Range(min = 4, max = 5, message = "数据库语种只能是中文或外文")
    private Long language;

    /**
     * 地区
     */
    @Length(max = 200, message = "地区最大长度为{max}个字符")
    private String area;

    /**
     * 数据库纸电标识（0=不是 1=是）
     */
    @Range(min = 0, max = 1, message = "数据库语种只能是电子或纸本")
    private Integer paperFlag;

    /**
     * 全文标识（0=不是 1=是）
     */
    @Range(min = 0, max = 1, message = "数据库全文只能是是或者否")
    private Integer fulltextFlag;

    /**
     * 数据库性质
     */
    @Length(max = 200, message = "数据库性质最大长度为{max}个字符")
    private String natureType;

    /**
     * 资源类型
     */
    private List<Long> typeList;

    /**
     * 资源类型名称
     */
    private String typeNameList;

    /**
     * 学科覆盖
     */
    private List<Long> subjectList;

    /**
     * 学科门类名称
     */
    private String subjectCategoryNameList;

    /**
     * 一级学科名称
     */
    private String subjectOneNameList;

    /**
     * 数据时间
     */
    @Length(max = 150, message = "数据时间最大长度为{max}个字符")
    private String dataTime;

    /**
     * 供应商
     */
    @Length(max = 150, message = "供应商最大长度为{max}个字符")
    private String companyName;

    /**
     * 代理商
     */
    @Length(max = 150, message = "代理商最大长度为{max}个字符")
    private String agentName;

    /**
     * 资源总量
     */
    @Length(max = 150, message = "资源总量最大长度为{max}个字符")
    private String totalResource;

    /**
     * 资源容量
     */
    @Length(max = 100, message = "资源容量最大长度为{max}个字符")
    private String resourceCapacity;

    /**
     * 更新频率
     */
    private String updateFrequency;

    /**
     * 检索功能
     */
    private String search;

    /**
     * 并发数
     */
    @Length(max = 150, message = "并发数最大长度为{max}个字符")
    private String concurrency;

    /**
     * 使用指南
     */
    @Valid
    @Size(max = 10, message = "数据库使用指南不能超过{max}个字符")
    private List<AttachmentDontCheckModel> attachList;

    /**
     * 内容简介
     */
    @Length(max = 5000, message = "内容简介最大长度为{max}个字符")
    private String introduce;

    private static final long serialVersionUID = 1L;
}