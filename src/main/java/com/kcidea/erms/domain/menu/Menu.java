package com.kcidea.erms.domain.menu;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * modelName
     */
    private String modelName;

    /**
     * 菜单层级
     */
    private Integer level;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 父菜单id
     */
    private Long parentId;

    /**
     * 显示操作
     */
    private Integer showFlag;

    /**
     * 查看操作
     */
    private Integer selectFlag;

    /**
     * 插入操作
     */
    private Integer insertFlag;

    /**
     * 更新操作
     */
    private Integer updateFlag;

    /**
     * 删除操作
     */
    private Integer deleteFlag;

    /**
     * 导入操作
     */
    private Integer importFlag;

    /**
     * 导出操作
     */
    private Integer exportFlag;

    /**
     * 其它操作
     */
    private Integer otherFlag;

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
}
