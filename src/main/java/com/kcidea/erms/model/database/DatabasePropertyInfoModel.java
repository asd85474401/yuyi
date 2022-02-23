package com.kcidea.erms.model.database;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/24
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DatabasePropertyInfoModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据库ID
     */
    private Long did;

    /**
     * 学校id
     */
    private Long sid;

    /**
     * 语种
     */
    private Long languageId;

    /**
     * 类型
     */
    private Long propertyId;

    /**
     * 纸本标识
     */
    private Integer paperFlag;

    /**
     * 类型名称
     */
    private String propertyName;
}
