package top.lcywings.pony.common.constant;

import java.math.BigDecimal;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/9
 **/
public class Constant {

    /**
     * token的key
     **/
    public static final String ERMS_TOKEN_KEY = "accessToken";

    /**
     * int类型 全部对应值
     */
    public static final Integer ALL_INT_VALUE = 999;

    /**
     * long类型 全部对应值
     */
    public static final Long ALL_LONG_VALUE = 999L;

    /**
     * String类型 全部对应值
     */
    public static final String ALL_STRING_VALUE = "全部";

    /**
     * 序列号名称
     */
    public static final String SERIAL_VERSION_UID = "serialVersionUID";

    /**
     * 数量为0
     **/
    public static final String NO_COUNT = "N/A";

    /**
     * 无
     **/
    public static final String CHAIN_NO = "无";

    /**
     * Redis的Key
     */
    public interface Subject {
        /**
         * long类型 综合学科
         */
        Long COMPREHENSIVE_SUBJECT_LONG = 99999L;

        /**
         * String类型 综合学科
         */
        String COMPREHENSIVE_SUBJECT_STRING = "综合";
    }

    /**
     * Redis的Key
     */
    public interface RedisKey {

        /**
         * 缓存的token
         */
        String TOKEN_REDIS_KEY = "erms_login_token:";

        /**
         * 角色的菜单权限
         */
        String ROLE_MENU_KEY = "role_menu_data:";

        /**
         * 学校数据库学科下拉缓存
         */
        String SCHOOL_DATABASE_SUBJECT_DROP_DOWN_KEY = "school_database_subject_drop_down_key";

        /**
         * 学科的key
         */
        String SUBJECT_KEY = "erms_subject_key:";
    }

    /**
     * Redis常量
     */
    public interface RedisTime {

        /**
         * 默认七天
         */
        int DEFAULT = 604800;

        /**
         * 1天 24小说
         */
        int ONE_DAY = 86400;

        /**
         * 半天 12小时
         */
        int HALF_DAY = 43200;

        /**
         * 6小时
         */
        int SIX_HOUR = 21600;

        /**
         * 1小时
         */
        int ONE_HOUR = 3600;

    }


    /**
     * 日期格式化
     */
    public interface Pattern {

        /**
         * 格式化为Date
         */
        String DATE = "yyyy-MM-dd";

        /**
         * 格式化为月份
         */
        String MONTH = "yyyy-MM";

        /**
         * 格式化为DateTime
         */
        String DATE_TIME = "yyyy-MM-dd HH:mm:ss";

        /**
         * 格式化为DateTime,没有秒
         */
        String DATE_TIME_NO_SECOND = "yyyy-MM-dd HH:mm";

        /**
         * 格式化时间到分钟带上年月日后缀
         */
        String DATE_NAME = "yyyy年MM月dd日 HH:mm";

        /**
         * 年
         */
        String YEAR_STR = "年";
    }

    /**
     * 分页信息
     */
    public interface Page {
        /**
         * 每页最大数量
         */
        int MAX_PAGE_SIZE = 100;
    }

    /**
     * 分隔符的常量
     */
    public interface SplitChar {

        /**
         * 逗号的分割
         */
        String COMMA_CHAR = ",";

        /**
         * 顿号的分割
         */
        String CHAIN_COMMA_CHAR = "、";

        /**
         * 横线的分割
         */
        String LINE_CHAR = "-";

        /**
         * 正斜杠的分割
         */
        String FORWARD_SLASH_CHAR = "/";

        /**
         * 点号
         */
        String POINT_CHAR = ".";

        /**
         * 分号
         */
        String SEMICOLON_CHAR = ";";

        /**
         * 换行
         */
        String LINE_FEED_CHAR = "\r\n";

        /**
         * 子库制表符
         */
        String SON_DATABASE_TAB = "|-";
    }

    /**
     * 后台用户
     */
    public interface Admin {
        /**
         * 用户名最大长度
         */
        int MAX_ACCOUNT_LENGTH = 32;
        /**
         * 密码最小长度
         */
        int MIN_PASSWORD_LENGTH = 6;
        /**
         * 密码最大长度
         */
        int MAX_PASSWORD_LENGTH = 20;

        /**
         * nickNName最大长度
         */
        int MAX_NICK_NAME_LENGTH = 100;
    }

    /**
     * 后缀
     */
    public interface Suffix {
        String CSV = "csv";

        String XLS = "xls";

        String XLSX = "xlsx";

        String XLSX_WITH_POINT = ".xlsx";

        String EXE = "exe";
    }

    /**
     * 邮件模板
     */
    public interface Template {
        /**
         * 付款提醒的模板位置
         */
        String PAY_REMIND_EMAIL = "payRemind";

        /**
         * 合同名称
         */
        String PAY_REMIND_CONTRACT_NAME = "contractName";

        /**
         * 付款截止日期
         */
        String PAY_REMIND_END_DAY = "endDay";

        /**
         * 剩余天数
         */
        String PAY_REMIND_DAYS = "days";
    }

    /**
     * 价格运算常量
     */
    public interface Price {

        /**
         * 一万元
         */
        BigDecimal TEN_THOUSAND = new BigDecimal("10000");

    }

    /**
     * 最大评估附件大小
     */
    public static final Long MAX_EVALUATION_ATTACH_FILE_SIZE = 20971520L;


    /**
     * 文件服务器的容器
     *
     * @author yqliang
     * @date 2020/7/23 9:18
     **/
    public interface MinIoBucketName {
        //ERMS
        String ERMS = "erms";
    }

    /**
     * 数据库评估
     */
    public interface DatabaseEvaluation {
        /**
         * 数据库名称长度限制
         */
        Integer NAME_SIZE = 500;

        /**
         * 数据库名称正则校验，汉字或字母开头
         */
        String NAME_REGEXP = "^[\\u4e00-\\u9fa5A-Za-z].*$";

        /**
         * 数据库性质长度限制
         */
        Integer NATURE_TYPE_SIZE = 200;

        /**
         * 数据库地区长度限制
         */
        Integer AREA_SIZE = 200;

    }

    /**
     * 反馈处理
     */
    public interface Feedback {

        /**
         * 100长度限制
         */
        Integer SIZE_HUNDRED = 100;

        /**
         * 150长度限制
         */
        Integer SIZE_HUNDRED_FIFTY = 150;

        /**
         * 1000长度限制
         */
        Integer SIZE_THOUSAND = 1000;

        /**
         * 邮箱校验
         */
        String EMAIL = "^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$";
        /**
         * 手机号校验
         */
        String PHONE = "^(13[0-9]|14[01456879]|15[0-3,5-9]|16[2567]|17[0-8]|18[0-9]|19[0-3,5-9])\\d{8}$";
    }


    /**
     * 数据商填写
     */
    public interface CompanyWrite {
        /**
         * 商务
         */
        String COMMERCE = "商务";

        /**
         * 供应商联系人
         */
        Integer COMPANY_CONTACT = 0;

        /**
         * 代理商联系人
         */
        Integer AGENT_CONTACT = 1;
    }

}
