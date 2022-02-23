package com.kcidea.erms.model.database;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/30
 **/
@Data
public class AttachmentModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件名
     */
    @NotBlank(message = "请上传评估附件")
    private String fileName;

    /**
     * 文件路径
     */
    @NotBlank(message = "请上传评估附件")
    private String filePath;

    public AttachmentModel create(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;

        return this;
    }
}
