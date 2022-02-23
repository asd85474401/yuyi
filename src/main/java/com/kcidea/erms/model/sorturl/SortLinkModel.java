package com.kcidea.erms.model.sorturl;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author huxubin
 * @version 1.0
 * @date 2022/1/13
 **/
@Data
public class SortLinkModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 短链接的名称
     */
    private String name;

    /**
     * 原始链接
     */
    @JSONField(name = "origin_url")
    private String originUrl;

    /**
     * 短链接
     */
    @JSONField(name = "url")
    private String sortUrl;
}
