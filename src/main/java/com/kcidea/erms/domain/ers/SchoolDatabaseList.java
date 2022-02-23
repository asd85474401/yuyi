package com.kcidea.erms.domain.ers;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/26
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SchoolDatabaseList implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 数据库id
     */
    private Long dId;

    /**
     * 数据库编码
     */
    private String code;

    /**
     * 学校id
     */
    private Long sId;

    /**
     * 订购方式
     */
    private Integer orderType;

    /**
     * 订购年份
     */
    private Integer vyear;

    /**
     * 订购开始日期
     */
    private LocalDate startData;

    /**
     * 订购结束日期
     */
    private LocalDate endData;

    /**
     * 总库标志
     */
    private Integer totalFlag;

    /**
     * 关联总库id，仅在子库时有此字段
     */
    private Long totalDId;

    /**
     * 备注
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

    public SchoolDatabaseList create(Long dId, String code, Long sId, Integer orderType, Integer vyear,
                                     Integer totalFlag, Long totalDId, Long createdBy, LocalDateTime createdTime) {
        this.dId = dId;
        this.code = code;
        this.sId = sId;
        this.orderType = orderType;
        this.vyear = vyear;
        this.totalFlag = totalFlag;
        this.totalDId = totalDId;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        return this;
    }
}
