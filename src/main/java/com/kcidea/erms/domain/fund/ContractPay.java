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
public class ContractPay implements Serializable {

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
     * 合同id
     */
    private Long contractId;

    /**
     * 支付来源
     */
    private Long budgetId;

    /**
     * 经费来源年份
     */
    private Integer budgetYear;

    /**
     * 是否支付：0=未支付，1=已支付
     */
    private Integer payFlag;

    /**
     * 支付日期
     */
    private LocalDate payDay;

    /**
     * 支付金额
     */
    private BigDecimal price;

    /**
     * 发票编号
     */
    private String invoiceNumber;


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
