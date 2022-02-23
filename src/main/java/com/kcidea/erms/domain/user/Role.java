package com.kcidea.erms.domain.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.kcidea.erms.enums.user.EnumUserAction;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜单
 *
 * @author yeweiwei
 * @version 1.0
 * @date 2021/5/26
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Role implements Serializable {

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
     * 菜单名称
     */
    private String name;

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

    public Role create(Long sId, String name, String remark, Long userId, LocalDateTime time, EnumUserAction action) {
        this.sId = sId;
        this.name = name;
        this.remark = remark;

        switch (action){
            case 新增:
                this.createdBy = userId;
                this.createdTime = time;
                break;
            case 修改:
                this.updatedBy = userId;
                this.updatedTime = time;
                break;
            default:
                break;
        }

        return this;
    }
}
