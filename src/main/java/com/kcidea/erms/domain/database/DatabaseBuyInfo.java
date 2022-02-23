package com.kcidea.erms.domain.database;

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
 * @date 2021/12/7
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DatabaseBuyInfo implements Serializable {

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
     * 采购类型
     */
    private Integer buyType;

    /**
     * 数据库性质（0=自建数据库、1=商购数据库、2=OA数据库）
     */
    private Integer natureType;

    /**
     * 文献类型
     */
    private String resourceType;

    /**
     * 使用起止年
     */
    private String useYear;

    /**
     * 手动获取方式(0=在线查询 1=公司提供)
     */
    private Integer downloadType;

    /**
     * 手动下载地址
     */
    private String downloadUrl;

    /**
     * 手动下载帐号
     */
    private String downloadAccount;

    /**
     * 手动下载密码
     */
    private String downloadPassword;

    /**
     * 是否支持sushi收割(0=不支持 1=支持)
     */
    private Integer sushiFlag;

    /**
     * Sushi地址
     */
    private String sushiAddress;

    /**
     * requestor_id
     */
    private String requestorId;

    /**
     * customer_id
     */
    private String customerId;

    /**
     * ApiKey
     */
    private String apiKey;

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