package com.kcidea.erms.model.subject;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/01/13
 **/
@Data
public class SubjectAndCategoryModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 门类学科
     */
    private String subjectCategory;

    /**
     * 学科门类数据
     */
    private Set<Long> subjectCategoryList;

    /**
     * 一级学科
     */
    private String subject;

    /**
     * 一级学科数据
     */
    private Set<Long> subjectOneList;


    public SubjectAndCategoryModel create(String subjectCategory, Set<Long> subjectCategoryList, String subject,
                                          Set<Long> subjectOneList) {
        this.subjectCategory = subjectCategory;
        this.subjectCategoryList = subjectCategoryList;
        this.subject = subject;
        this.subjectOneList = subjectOneList;
        return this;
    }
}
