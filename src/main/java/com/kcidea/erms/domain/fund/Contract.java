package com.kcidea.erms.domain.fund;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class Contract implements Serializable {

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
     * 合同名称
     */
    private String name;

    /**
     * 合同编号
     */
    private String number;

    /**
     * 合同签订日期
     */
    private LocalDate signingDay;

    /**
     * 合同开始日期
     */
    private LocalDate startDay;

    /**
     * 合同结束日期
     */
    private LocalDate endDay;

    /**
     * 类型（1=新订 2=续订 3=买断）
     */
    private Integer orderType;

    /**
     * 合同金额
     */
    private BigDecimal price;

    /**
     * 合同币种
     */
    private Integer priceType;

    /**
     * 人民币金额
     */
    private BigDecimal rmbPrice;

    /**
     * 预算来源
     */
    private Long budgetId;

    /**
     * 支付类型（0=单次支付 1=多次支付）
     */
    private Integer payType;

    /**
     * 支付状态（0=未支付 1=已支付部分 2=支付完成）
     */
    private Integer payState;

    /**
     * 付款开始日期
     */
    private LocalDate payStartDay;

    /**
     * 付款截止日期
     */
    private LocalDate payEndDay;

    /**
     * 数据商id
     */
    private Long companyId;

    /**
     * 代理商id
     */
    private Long agentId;

    /**
     * 使用权，单选
     */
    private Integer useRight;

    /**
     * 存档权，多选，逗号分割
     */
    private String saveRight;

    /**
     * 价格构成
     */
    private String priceStructure;

    /**
     * 价格方案
     */
    private String priceScheme;

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