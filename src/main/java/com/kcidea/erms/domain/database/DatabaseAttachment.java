package com.kcidea.erms.domain.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * database_attachment
 *
 * @author
 */
@Data
public class DatabaseAttachment implements Serializable {
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
     * 对应的主键的id
     */
    private Long uniqueId;

    /**
     * 类型(1=评估附件 2=停订附件)
     */
    private Integer type;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 备注说明
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

    private static final long serialVersionUID = 1L;

    public DatabaseAttachment create(Long sId, Long uniqueId, Integer type, String fileName, String filePath,
                                     Long createdBy, LocalDateTime createdTime) {
        this.sId = sId;
        this.uniqueId = uniqueId;
        this.type = type;
        this.fileName = fileName;
        this.filePath = filePath;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        return this;
    }
}