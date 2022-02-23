package com.kcidea.erms.model.database.detail;

import lombok.Data;

import java.io.Serializable;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/09
 **/
@Data
public class DatabaseUsingStatisticsModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 手动获取方式
     */
    private String downloadType;

    /**
     * 手动下载地址
     */
    private String downloadUrl;

    /**
     * 手动下载帐号
     */
    private String downloadAccount;

    /**
     * 手动下载密码
     */
    private String downloadPassword;
}
