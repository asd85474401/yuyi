package com.kcidea.erms.model.subject;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/12/1
 **/
@Data
public class SubjectModel  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学科id
     */
    private Long id;

    /**
     * 学科名称
     */
    private String name;

    /**
     * 子学科列表
     */
    List<SubjectModel> childList;

    public SubjectModel create(Long id, String name, List<SubjectModel> childList) {
        this.id = id;
        this.name = name;
        this.childList = childList;
        return this;
    }
}
