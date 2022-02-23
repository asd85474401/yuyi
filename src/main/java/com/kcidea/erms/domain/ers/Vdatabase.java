package com.kcidea.erms.domain.ers;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/16
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Vdatabase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 数据库名称
     */
    private String name;

    /**
     * 数据库编码
     */
    private String code;

    /**
     * 学校id，0为系统自带的，其他是学校自定义的
     */
    private Long customSchoolId;

    /**
     * 父数据库id，默认为0
     */
    private Long parentId;

    /**
     * 数据库路径
     */
    private String url;

    /**
     * 免费资源 ，1是免费  0 是非免费
     */
    private Integer freeFlag;

    /**
     * 排序
     */
    private int sort;

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

    public Vdatabase create(String name, Long customSchoolId, Long createdBy, LocalDateTime createdTime) {
        this.name = name;
        this.customSchoolId = customSchoolId;
        this.createdBy = createdBy;
        this.createdTime = createdTime;

        return this;
    }
}
