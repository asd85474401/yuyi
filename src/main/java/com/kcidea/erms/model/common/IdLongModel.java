package com.kcidea.erms.model.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/12/8
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class IdLongModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * value
     */
    private Long value;
}
