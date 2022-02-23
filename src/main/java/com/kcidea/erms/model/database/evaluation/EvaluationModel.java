package com.kcidea.erms.model.database.evaluation;

import com.kcidea.erms.model.database.AttachmentModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/12/1
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EvaluationModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评估id
     */
    @NotNull
    private Long evaluationId;

    /**
     * 评估结果
     */
    @NotNull(message = "请选择评估结果")
    @Range(min = 1, max = 2, message = "请选择正确的评估结果")
    private Integer resultType;

    /**
     * 附件信息集合
     */
    @Valid
    @NotEmpty(message = "请上传评估附件")
    @Size(min = 1, max = 10, message = "数据库评估附件不能超过{max}个")
    private List<AttachmentModel> attachList;

    /**
     * 评估结果说明
     */
    @Length(max = 500, message = "结果说明最大长度为{max}个字符")
    private String remark;
}
