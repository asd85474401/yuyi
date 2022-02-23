package com.kcidea.erms.service.fund;

import com.kcidea.erms.model.fund.BudgetTrendAnalysisModel;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/17 9:59
 **/
public interface StatisticalAnalysisService {

    /**
     * 历年预算排名
     *
     * @param startYear 开始年份
     * @param endYear   结束年份
     * @param sid       学校id
     * @return 历年预算排名
     * @author majuehao
     * @date 2021/11/17 8:59
     **/
    BudgetTrendAnalysisModel findBudgetTrendsList(Integer startYear, Integer endYear, Long sid);
}
