package com.kcidea.erms.domain.ers;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 学校数据库别名表 
 * </p>
 *
 * @author yqliang
 * @since 2020-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SchoolDatabaseAlias implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
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
     * 数据库名字
     */
    private String dName;

    /**
     * 学校自定义链接
     */
    private String url;

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

    public SchoolDatabaseAlias create(Long sId, Long dId, String dName, String url, String remark,
                                      Long createdBy, LocalDateTime createdTime) {
        this.sId = sId;
        this.dId = dId;
        this.dName = dName;
        this.url = url;
        this.remark = remark;
        this.createdBy = createdBy;
        this.createdTime = createdTime;

        return this;
    }
}
