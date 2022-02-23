package com.kcidea.erms.model.database.detail;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/25
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DatabaseInfoModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据库id
     */
    private Long did;

    /**
     * 数据库名称
     */
    @NotBlank(message = "请输入数据库名称")
    @Length(max = 500, message = "数据库名称最大长度为{max}个字符")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5A-Za-z].*$", message = "数据库名称只能以字母或汉字开头")
    private String name;

    /**
     * 数据库语种
     */
    @NotNull(message = "请选择数据库语种")
    @Range(min = 4, max = 5, message = "数据库语种只能是中文或外文")
    private Long languageId;

    /**
     * 数据库纸电
     */
    @NotNull(message = "请选择数据库纸电")
    @Range(min = 0, max = 1, message = "数据库语种只能是电子或纸本")
    private Integer paperFlag;

    /**
     * 数据库属性
     */
    @NotEmpty(message = "请选择数据库资源类型")
    private List<Long> propertyIdList;

    /**
     * 属性名称
     */
    private String properties;

    public DatabaseInfoModel create(String name, Long languageId, Integer paperFlag, List<Long> propertyIdList) {
        this.name = name;
        this.languageId = languageId;
        this.paperFlag = paperFlag;
        this.propertyIdList = propertyIdList;

        return this;
    }

    public DatabaseInfoModel create(Long did, String name, Long languageId, Integer paperFlag, List<Long> propertyIdList) {
        this.did = did;
        this.name = name;
        this.languageId = languageId;
        this.paperFlag = paperFlag;
        this.propertyIdList = propertyIdList;

        return this;
    }
}
