package com.kcidea.erms.model.fund;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/16
 **/
@Data
public class ContractPayListModel  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付记录id
     */
    private Long id;

    /**
     * 经费来源
     */
    private String budgetName;

    /**
     * 付款日期
     */
    private String payDay;

    /**
     * 付款金额
     */
    private BigDecimal price;

    /**
     * 发票编号
     */
    private String invoiceNumber;
}
