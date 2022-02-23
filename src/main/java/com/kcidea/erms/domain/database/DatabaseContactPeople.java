package com.kcidea.erms.domain.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/1/12 17:12
 **/
@Data
@Accessors(chain = true)
public class DatabaseContactPeople implements Serializable {
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
     * 联系人姓名
     */
    private String name;

    /**
     * 职位
     */
    private String position;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 座机号
     */
    private String telephone;

    /**
     * 联系邮箱
     */
    private String email;

    /**
     * 联系QQ
     */
    private String qq;

    /**
     * 微信号
     */
    private String wechat;

    /**
     * 类型：0=供应商联系人 1=代理商联系人
     */
    private Integer type;

    /**
     * 邮寄地址
     */
    private String address;

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

    public DatabaseContactPeople create(Long sId, Long dId, String name, String position, String phone, String telephone,
                                        String email, String qq, String wechat, Integer type, String address, String remark,
                                        Long createdBy, LocalDateTime createdTime) {
        this.sId = sId;
        this.dId = dId;
        this.name = name;
        this.position = position;
        this.phone = phone;
        this.telephone = telephone;
        this.email = email;
        this.qq = qq;
        this.wechat = wechat;
        this.type = type;
        this.address = address;
        this.remark = remark;
        this.createdBy = createdBy;
        this.createdTime = createdTime;

        return this;
    }
}