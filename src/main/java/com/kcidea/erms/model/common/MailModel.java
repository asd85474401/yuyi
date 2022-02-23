package com.kcidea.erms.model.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/17
 **/
@Data
public class MailModel  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 内容
     */
    private String content;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 标题
     */
    private String title;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 收件人
     */
    private String toMail;

    /**
     * 数据
     */
    private Map<String, Object> paramsMap;

    /**
     * 模板
     */
    private String template;
}
