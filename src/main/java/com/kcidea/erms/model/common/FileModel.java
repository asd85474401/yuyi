package com.kcidea.erms.model.common;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/6/2
 **/
@Data
@Accessors(chain = true)
public class FileModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件原名称
     */
    private String originalName;

    /**
     * 文件新名称
     */
    private String newName;

    /**
     * 文件存储路径
     */
    private String path;

    /**
     * 文件url
     */
    private String url;
}
