package com.kcidea.erms.model.company;

import lombok.Data;

import java.io.Serializable;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/01/10
 **/
@Data
public class TotalDidSonDidModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总库id
     */
    private Long totalDid;

    /**
     * 子库id
     */
    private Long sonDid;
}
