package com.kcidea.erms.model.sorturl;

import lombok.Data;

import java.io.Serializable;

/**
 * @author huxubin
 * @version 1.0
 * @date 2022/1/13
 **/
@Data
public class SortGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分组的sid
     */
    private String sid;

    /**
     * 分组的名称
     */
    private String name;

}
