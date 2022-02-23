package com.kcidea.erms.model.database.info;

import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.model.database.AttachmentDontCheckModel;
import com.kcidea.erms.model.database.detail.DatabaseInfoModel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/1/5 13:40
 **/
@Data
@Accessors(chain = true)
public class DatabaseInfoInsertModel implements Serializable {

    /**
     * 数据库基本信息id
     */
    private Long infoId;

    /**
     * 数据库id
     */
    @NotNull(message = Vm.ERROR_PARAMS)
    private Long did;

    /**
     * 数据库信息
     */
    @Valid
    private DatabaseInfoModel databaseInfoModel;

    /**
     * 地区
     */
    @NotBlank(message = "请输入地区")
    @Length(max = 200, message = "地区最大长度为{max}个字符")
    private String area;

    /**
     * 全文标识（0=不是 1=是）
     */
    @NotNull(message = "请选择数据库全文种类")
    @Range(min = 0, max = 1, message = "数据库全文只能是是或者否")
    private Integer fulltextFlag;

    /**
     * 数据库性质
     */
    @NotBlank(message = "请选择数据库性质")
    @Length(max = 200, message = "数据库性质最大长度为{max}个字符")
    private String natureType;

    /**
     * 学科覆盖
     */
    @NotEmpty(message = "请至少选择一个数据库学科覆盖")
    private List<Long> subjectList;

    /**
     * 数据时间
     */
    @NotBlank(message = "请输入数据库数据时间")
    @Length(max = 150, message = "数据时间最大长度为{max}个字符")
    private String dataTime;

    /**
     * 供应商id
     */
    private Long companyId;

    /**
     * 供应商
     */
    @NotBlank(message = "请选择供应商")
    @Length(max = 100, message = "供应商名称最大长度为{max}个字符")
    private String companyName;

    /**
     * 代理商id
     */
    private Long agentId;

    /**
     * 代理商
     */
    @Length(max = 150, message = "代理商名称最大长度为{max}个字符")
    private String agentName;

    /**
     * 资源总量
     */
    @NotBlank(message = "请输入数据库资源总量")
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
    @NotBlank(message = "请输入数据库更新频率")
    private String updateFrequency;

    /**
     * 检索功能
     */
    @NotBlank(message = "请输入数据库检索功能")
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
    @Size(max = 10, message = "数据库使用指南不能超过{max}个")
    private List<AttachmentDontCheckModel> attachList;

    /**
     * 内容简介
     */
    @NotBlank(message = "请输入数据库内容简介")
    @Length(max = 5000, message = "内容简介最大长度为{max}个字符")
    private String introduce;

    /**
     * 一级学科
     **/
    private String oneSubject;

    /**
     * 门类学科
     **/
    private String categorySubject;

    private static final long serialVersionUID = 1L;

    public DatabaseInfoInsertModel importFile(String area, Integer fulltextFlag, String natureType) {
        this.area = area;
        this.fulltextFlag = fulltextFlag;
        this.natureType = natureType;
        return this;
    }
}