package top.lcywings.pony.enums.menu;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/9
 **/
public enum EnumMenu {

    /**
     * 首页
     */
    首页(5000L, "Home"),

    /**
     * 数据库信息管理
     */
    数据库信息管理(10000L, "DataBaseInfoMgr"),

    /**
     * 数据库列表
     */
    数据库列表(11000L, "DataBaseList"),

    /**
     * 数据库综合评估
     */
    数据库综合评估(20000L, "DataBaseEvaluate"),

    /**
     * 数据库评估
     */
    数据库评估管理(21000L, "DataBaseEvaluateMgr"),

    /**
     * 年度采购计划管理
     */
    年度采购计划管理(22000L, "BuyPlanMgr"),

    /**
     * 数据库采购管理
     */
    数据库采购管理(30000L, "DataBaseBuyMgr"),

    /**
     * 数据库列表采购管理
     */
    数据库列表采购管理(31000L, "DataBaseBuyListMgr"),

    /**
     * 停订数据库管理
     */
    停订数据库管理(32000L, "DataBaseStopListMgr"),

    /**
     * 资金管理
     */
    资金管理(40000L, "FundMgr"),

    /**
     * 预算管理
     */
    预算管理(41000L, "BudgetMgr"),

    /**
     * 账簿管理
     */
    账簿管理(42000L, "AccountBooksMgr"),

    /**
     * 统计分析
     */
    统计分析(43000L, "StatisticalAnalysis"),
    
    /**
     * 数据库使用与维护管理
     */
    数据库使用与维护管理(50000L, "DataBasePreserve"),

    /**
     * 用户咨询与反馈管理
     */
    用户咨询与反馈管理(60000L, "UserFeedbackMgr"),

    /**
     * 问题反馈
     */
    问题反馈(61000L, "FeedbackDispose"),

    /**
     * 业务流转管理
     */
    业务流转管理(70000L, "BusinessMgr"),

    /**
     * 人员角色管理
     */
    人员角色管理(80000L, "RoleUserMgr"),

    /**
     * 人员管理
     */
    人员管理(81000L, "UserMgr"),

    /**
     * 角色管理
     */
    角色管理(82000L, "RoleMgr"),

    /**
     * 其他功能
     */
    其他功能(90000L, "Other"),

    /**
     * 数据库详情
     */
    数据库详情(91000L, "DataBaseDetail"),

    /**
     * 任务列表
     */
    任务列表(92000L, "TaskList"),

    /**
     * 数据商填写信息审核
     */
    数据商填写信息审核(105000L, "CompanyWriteCheck"),

    /**
     * 年度采购计划审核
     */
    年度采购计划审核(110000L, "BuyPlanCheck"),

    /**
     * 用户反馈审核
     */
    用户反馈审核(120000L, "FeedbackCheck"),

    ;

    final Long value;
    final String name;

    EnumMenu(Long value, String name) {
        this.value = value;
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    /**
     * 根据类型的值获取对应的类型
     *
     * @param type 类型
     * @return 对应的枚举
     */
    public static EnumMenu getEnumMenu(Long type) {
        for (EnumMenu enumMenu : EnumMenu.values()) {
            if (enumMenu.value.equals(type)) {
                return enumMenu;
            }
        }
        return null;
    }

}
