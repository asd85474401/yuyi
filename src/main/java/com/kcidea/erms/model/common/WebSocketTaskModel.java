package com.kcidea.erms.model.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/29
 **/
@Data
public class WebSocketTaskModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 年份
     */
    private Integer vYear;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 是否覆盖数据， 1=覆盖，0=不覆盖
     */
    private Integer coverFlag;
}
