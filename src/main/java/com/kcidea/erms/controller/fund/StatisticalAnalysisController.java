package com.kcidea.erms.controller.fund;

import com.kcidea.erms.aop.ActionRights;
import com.kcidea.erms.common.result.Result;
import com.kcidea.erms.controller.common.BaseController;
import com.kcidea.erms.enums.menu.EnumMenu;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.fund.BudgetTrendAnalysisModel;
import com.kcidea.erms.service.fund.StatisticalAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/17
 **/
@Slf4j
@RestController
@RequestMapping("/statisticalAnalysis")
public class StatisticalAnalysisController extends BaseController {

    @Resource
    private StatisticalAnalysisService statisticalAnalysisService;

    /**
     * 历年预算趋势
     *
     * @param startYear 开始年份
     * @param endYear   结束年份
     * @return 历年预算趋势
     * @author majuehao
     * @date 2021/11/17 8:59
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.统计分析)
    @GetMapping(value = "/findBudgetTrendsList")
    public Result<BudgetTrendAnalysisModel> findBudgetTrendsList(@RequestParam(value = "startYear") Integer startYear,
                                                                 @RequestParam(value = "endYear") Integer endYear) {
        super.saveInfoLog();
        super.checkYearRange(startYear, endYear);
        BudgetTrendAnalysisModel ret = statisticalAnalysisService.findBudgetTrendsList(startYear, endYear, getSid());
        return new Result<BudgetTrendAnalysisModel>().success(ret);
    }
}
