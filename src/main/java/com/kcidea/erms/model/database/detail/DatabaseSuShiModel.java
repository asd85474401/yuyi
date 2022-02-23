package com.kcidea.erms.model.database.detail;

import lombok.Data;

import java.io.Serializable;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/09
 **/
@Data
public class DatabaseSuShiModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Sushi地址
     */
    private String sushiAddress;

    /**
     * requestor id
     */
    private String requestorId;

    /**
     * customer id
     */
    private String customerId;

    /**
     * api key
     */
    private String apiKey;
}
