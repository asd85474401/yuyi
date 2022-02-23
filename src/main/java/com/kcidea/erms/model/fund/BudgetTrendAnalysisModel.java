package com.kcidea.erms.model.fund;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/23
 **/
@Data
public class BudgetTrendAnalysisModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 历年预算趋势
     */
    private List<YearBudgetModel> budgetTrendList;

    /**
     * 历年预算排名
     */
    private List<YearBudgetModel> budgetRankList;

}
