package com.kcidea.erms.model.database;

import lombok.Data;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/25
 **/
@Data
public class AttachmentDontCheckModel {

    private static final long serialVersionUID = 1L;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    public AttachmentDontCheckModel create(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;

        return this;
    }
}
