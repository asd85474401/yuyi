package com.kcidea.erms.service.fund.impl;

import com.google.common.collect.Lists;
import com.kcidea.erms.dao.fund.SchoolBudgetRelDao;
import com.kcidea.erms.model.fund.BudgetTrendAnalysisModel;
import com.kcidea.erms.model.fund.YearBudgetModel;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.fund.StatisticalAnalysisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/17
 **/
@Service
public class StatisticalAnalysisServiceImpl extends BaseService implements StatisticalAnalysisService {

    @Resource
    private SchoolBudgetRelDao schoolBudgetRelDao;

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
    @Override
    public BudgetTrendAnalysisModel findBudgetTrendsList(Integer startYear, Integer endYear, Long sid) {
        // 校验参数
        super.checkSid(sid);

        // 按照预算排名查询预算，并按照年份收束成Map集合
        List<YearBudgetModel> budgetRankList = schoolBudgetRelDao.findBudgetRankListBySidYear(sid, startYear, endYear);
        Map<Integer, BigDecimal> map =
                budgetRankList.stream().collect(Collectors.toMap(YearBudgetModel::getVYear, YearBudgetModel::getPrice));

        for (Integer year = endYear; year >= startYear; year--) {
            // 如果不包含那年的预算，就设置默认值
            if (!map.containsKey(year)) {
                YearBudgetModel model = new YearBudgetModel();
                model.setVYear(year);
                model.setPrice(BigDecimal.ZERO);
                budgetRankList.add(model);
            }
        }

        // 声明返回集合
        BudgetTrendAnalysisModel model = new BudgetTrendAnalysisModel();
        // 设置按金额排序的预算集合
        model.setBudgetRankList(budgetRankList);
        // 声明按年份排序的预算集合，防止排序影响按金额排序的集合
        List<YearBudgetModel> budgetTrendList = Lists.newArrayList(budgetRankList);
        // 按照年份排序
        budgetTrendList.sort(Comparator.comparingInt(YearBudgetModel::getVYear));
        // 设置按年份排序的预算集合
        model.setBudgetTrendList(budgetTrendList);
        return model;
    }
}
