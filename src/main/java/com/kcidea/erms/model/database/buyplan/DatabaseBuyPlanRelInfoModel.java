package com.kcidea.erms.model.database.buyplan;

import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/6 16:41
 **/
@Data
public class DatabaseBuyPlanRelInfoModel implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 计划id
     */
    @NotNull
    private Long planId;

    /**
     * 数据库id
     */
    @NotNull
    private Long dId;

    /**
     * 预算金额
     */
    @NotNull
    @DecimalMax(value = "999999999999999",message = "输入金额不能大于15位")
    private BigDecimal price;

    /**
     * 采购原因
     */
    private String remark;

    private static final long serialVersionUID = 1L;
}