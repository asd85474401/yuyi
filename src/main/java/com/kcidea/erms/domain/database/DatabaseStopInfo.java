package com.kcidea.erms.domain.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/24 13:28
 **/
@Data
public class DatabaseStopInfo implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 学校id
     */
    private Long sId;

    /**
     * 数据库id
     */
    private Long dId;

    /**
     * 年份
     */
    private Integer vyear;

    /**
     * 停订日期
     */
    private LocalDate stopDay;

    /**
     * 停订原因
     */
    private String remark;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    private Long updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    public DatabaseStopInfo create(Long sId, Long dId, Integer vyear, LocalDate stopDay, String remark,
                                   Long createdBy, LocalDateTime createdTime) {

        this.sId = sId;
        this.dId = dId;
        this.vyear = vyear;
        this.stopDay = stopDay;
        this.remark = remark;
        this.createdBy = createdBy;
        this.createdTime = createdTime;

        return this;
    }

    private static final long serialVersionUID = 1L;
}