package com.kcidea.erms.model.fund;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/16
 **/
@Data
public class ContractInfoModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合同id
     */
    private Long id;

    /**
     * 合同名称
     */
    private String name;

    /**
     * 合同编号
     */
    private String number;

    /**
     * 订购类型
     */
    private Integer orderType;

    /**
     * 合同金额（人名币）
     */
    private BigDecimal rmbPrice;

    /**
     * 付款开始日期
     */
    private LocalDate payStartDay;

    /**
     * 付款截止日期
     */
    private LocalDate payEndDay;

    /**
     * 支付状态
     */
    private Integer payState;

    /**
     * 经费id
     */
    private Long budgetId;

    /**
     * 经费名称
     */
    private String budgetName;
//
//    /**
//     * 已支付金额
//     */
//    private BigDecimal paidPrice;
//
//    /**
//     * 待支付金额
//     */
//    private BigDecimal unPaidPrice;
}
