package com.kcidea.erms.model.company;

import lombok.Data;

import java.io.Serializable;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/01/20
 **/
@Data
public class TableCheckState implements Serializable {

    /**
     * 状态(0=未提交 1=待审核 2=通过 3=未通过)
     */
    private Integer state;

    /**
     * 提交审核的时间
     */
    private String createdTime;

    /**
     * 审核说明
     */
    private String remark;

    /**
     * 审核的时间
     */
    private String checkTime;

    /**
     * 完成审核的时间
     */
    private String checkOverTime;

    private static final long serialVersionUID = 1L;

    public TableCheckState create(Integer state, String createdTime, String remark, String checkTime,
                                  String checkOverTime) {
        this.state = state;
        this.createdTime = createdTime;
        this.remark = remark;
        this.checkTime = checkTime;
        this.checkOverTime = checkOverTime;
        return this;
    }
}
