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
 * @date 2021/11/16
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class School implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 学校名字
     */
    private String name;

    /**
     * 学校英文名字
     */
    private String englishName;

    /**
     * 销售邮箱
     */
    private String salesEmail;

    /**
     * 订购类型，0试用1订购2未订购3停定
     */
    private Integer orderType;

    /**
     * 学校类型
     */
    private Integer schoolType;

    /**
     * 学校等级
     */
    private Integer schoolLevel;

    /**
     * 服务开始日期
     */
    private LocalDate startData;

    /**
     * 服务结束日期
     */
    private LocalDate endData;

    /**
     * 引文类型(wos、scoups、都有)
     */
    private Integer quotedType;

    /**
     * 数据开始时间
     */
    private Integer startYear;

    /**
     * logo
     */
    private String logo;

    /**
     * 0未安装  1安装
     */
    private Integer localProgramFlag;

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
}
