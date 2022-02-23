package com.kcidea.erms.model.database;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/30
 **/
@Data
public class DatabasePropertyModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据库属性集
     */
    private String properties;

    /**
     * 数据库属语种
     */
    private String language;

    /**
     * 数据库属纸电
     */
    private String paperFlag;

    /**
     * 数据库属性的id集合
     */
    private Set<Long> propertiesSet;

    public DatabasePropertyModel create(String properties, String language, String paperFlag) {
        this.properties = properties;
        this.language = language;
        this.paperFlag = paperFlag;
        return this;
    }
}
