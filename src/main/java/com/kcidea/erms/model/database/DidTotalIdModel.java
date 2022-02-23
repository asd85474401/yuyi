package com.kcidea.erms.model.database;

import lombok.Data;

import java.io.Serializable;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/10/22
 **/
@Data
public class DidTotalIdModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 子库id
     */
    private Long did;

    /**
     * 总库id
     */
    private Long totalDid;
}
