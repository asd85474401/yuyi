package com.kcidea.erms.domain.message;

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
 * @date 2021/11/17
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Message implements Serializable {

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
     * 用户id
     */
    private Long userId;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 是否已读（0=未读 1=已读）
     */
    private Integer readFlag;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 跳转路径
     */
    private String url;

    /**
     * 消息类型 1=支付到期提醒
     */
    private Integer type;

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

    public Message create(Long sId, Long userId, String title, Integer readFlag, String content, String url,
                          Integer type, LocalDateTime createdTime) {
        this.sId = sId;
        this.userId = userId;
        this.title = title;
        this.readFlag = readFlag;
        this.content = content;
        this.url = url;
        this.type = type;
        this.createdTime = createdTime;

        return this;
    }
}
