package com.kcidea.erms.domain.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/24 13:28
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DatabaseKey implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * apiKey
     */
    private String apiKey;

    /**
     * 短链接的url
     */
    private String sortUrl;

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

    public DatabaseKey create(Long sId, Long dId, String apiKey, LocalDateTime createdTime) {
        this.sId = sId;
        this.dId = dId;
        this.apiKey = apiKey;
        this.createdTime = createdTime;
        return this;
    }
}