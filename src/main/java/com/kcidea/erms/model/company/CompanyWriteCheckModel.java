package com.kcidea.erms.model.company;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/1/14 9:42
 **/
@Data
public class CompanyWriteCheckModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 数据库id
     */
    private Long did;

    /**
     * 数据库名称
     */
    private String name;

    /**
     * 填写类型
     */
    private String tableType;

    /**
     * 申请时间
     */
    private String createdTime;

    /**
     * 审核人
     */
    private String checkUser;

    /**
     * 审核时间
     */
    private String checkTime;

    /**
     * 审核状态
     */
    private String state;

    /**
     * 审核的状态值
     */
    private Integer stateValue;

    /**
     * 审核说明
     */
    private String remark;

    public CompanyWriteCheckModel create(Long id, Long did, String name, String tableType, String createdTime,
                                  String checkUser, String checkTime, String state,Integer stateValue, String remark) {
        this.id = id;
        this.did = did;
        this.name = name;
        this.tableType = tableType;
        this.createdTime = createdTime;
        this.checkUser = checkUser;
        this.checkTime = checkTime;
        this.state = state;
        this.stateValue = stateValue;
        this.remark = remark;
        return this;
    }
}