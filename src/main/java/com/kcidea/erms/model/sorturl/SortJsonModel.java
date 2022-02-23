package com.kcidea.erms.model.sorturl;

import lombok.Data;

import java.io.Serializable;

/**
 * @author huxubin
 * @version 1.0
 * @date 2022/1/13
 **/
@Data
public class SortJsonModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应的短链接分组
     */
    private SortGroup group;

    /**
     * 对应的链接信息
     */
    private SortLinkModel link;
}
