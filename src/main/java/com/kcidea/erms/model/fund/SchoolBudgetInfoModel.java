package com.kcidea.erms.model.fund;

import com.kcidea.erms.common.constant.Vm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/15
 **/
@Data
public class SchoolBudgetInfoModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 预算id
     */
    private Long budgetId;

    /**
     * 预算关系id
     */
    private Long budgetRelId;

    /**
     * 预算来源
     */
    @NotBlank(message = Vm.BUDGET_NAME_NOT_BLANK)
    @Size(max = 100, message = "预算来源不能超过100个字符！")
    private String name;

    /**
     * 历年经费
     */
    private List<YearBudgetModel> totalPriceList;

    public SchoolBudgetInfoModel create(Long id, Long budgetId, String name, List<YearBudgetModel> totalPriceList) {
        this.id = id;
        this.budgetId = budgetId;
        this.name = name;
        this.totalPriceList = totalPriceList;
        return this;
    }
}
