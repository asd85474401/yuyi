package top.lcywings.pony.common.util;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import top.lcywings.pony.common.constant.Constant;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间工具类
 *
 * @author by Bin
 * @version 1.0
 * @date 2020/4/13 9:55
 */
public class DateTimeUtil {

    /**
     * 获取当年的年份
     *
     * @return 当前的年份
     */
    public static int getNowYear() {
        LocalDateTime now = LocalDateTime.now();
        return now.getYear();
    }

    /**
     * 获取当前的月份
     *
     * @return 当前的月份
     */
    public static int getNowMonth() {
        LocalDateTime now = LocalDateTime.now();
        return now.getMonthValue();
    }

    /**
     * 将时间转换成LocalDateTime
     *
     * @param time   传入的时间
     * @param format 转换的格式
     * @return 转换的结果
     */
    public static LocalDateTime stringToLocalDateTime(String time, String format) {
        if (!Strings.isNullOrEmpty(time)) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
            return LocalDateTime.parse(time, df);
        }
        return null;
    }

    /**
     * 将时间转换成LocalDateTime(不传值)
     *
     * @param time 时间字符串
     * @return localDataTime类型的时间
     */
    public static LocalDateTime stringToLocalDateTime(String time) {
        return stringToLocalDateTime(time, Constant.Pattern.DATE_TIME);
    }

    /**
     * 将时间转换成LocalDate
     *
     * @param time 传入的时间
     * @return 转换的结果
     */
    public static LocalDate stringToLocalDate(String time) {
        return stringToLocalDate(time, Constant.Pattern.DATE);
    }

    /**
     * 将时间转换成LocalDate
     *
     * @param time   传入的时间
     * @param format 转换的格式
     * @return 转换的结果
     */
    public static LocalDate stringToLocalDate(String time, String format) {
        if (!Strings.isNullOrEmpty(time)) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(time, df);
        }
        return null;
    }

    /**
     * 将时间转换成String格式
     *
     * @param time localDataTime类型的时间
     * @return 时间字符串
     */
    public static String localDateTimeToString(LocalDateTime time) {
        return localDateTimeToString(time, Constant.Pattern.DATE_TIME);
    }

    /**
     * 将时间转换成String格式
     *
     * @param time LocalDate类型的时间
     * @return 时间字符串
     */
    public static String localDateToString(LocalDate time) {
        return localDateToString(time, Constant.Pattern.DATE);
    }

    /**
     * 将时间转换成LocalDateTime
     *
     * @param time   传入的时间
     * @param format 转换的格式
     * @return 转换的结果
     */
    public static String localDateTimeToString(LocalDateTime time, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        String result = "";
        if (time != null) {
            result = df.format(time);
        }
        return result;
    }

    /**
     * 将时间转换成LocalDateTime
     *
     * @param time   传入的时间
     * @param format 转换的格式
     * @return 转换的结果
     */
    public static String localDateToString(LocalDate time, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        String result = "";
        if (time != null) {
            result = df.format(time);
        }
        return result;
    }

    /**
     * 计算2个时间的天数差
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 相差的天数
     * @author huxubin
     * @date 2020/7/14
     **/
    public static int getDayDuration(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        //相差的天数
        long days = duration.toDays();
        return (int) days;
    }


    /**
     * 计算2个时间的天数差
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 相差的天数
     * @author huxubin
     * @date 2020/7/14
     **/
    public static int getDayDuration(LocalDate start, LocalDate end) {
        LocalDateTime startTime = start.atTime(12, 0);
        LocalDateTime endTime = end.atTime(12, 0);
        return getDayDuration(startTime, endTime);
    }

    /**
     * 从字符串中识别日期
     *
     * @param dateStr 字符串
     * @return 年份日期
     * @author huxubin
     * @date 2020/8/4 15:03
     */
    public static String matchYearFromStr(String dateStr) {
        // 正则表达式规则
        String regEx = "\\d{4}";
        // 编译正则表达式 // 忽略大小写的写法
        Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(dateStr);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 获取这个月的第一天
     *
     * @param date 时间
     * @return 这个月的第一天
     * @author huxubin
     * @date 2021/1/5 19:45
     */
    public static LocalDate getFirstDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取这个月的最后一天
     *
     * @param date 时间
     * @return 这个月的最后一天
     * @author huxubin
     * @date 2021/1/5 19:46
     */
    public static LocalDate getLastDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }


    /**
     * 将任意格式字符串转为时间
     * 当strTime为2008-9时返回为2008-9-1 00:00格式日期时间，无法转换返回null.
     *
     * @param strTime 时间字符串
     * @return 时间
     * @author majuehao
     * @date 2021/12/23 10:40
     **/
    public static LocalDateTime strToDate(String strTime) {
        if (strTime == null || strTime.trim().length() <= 0) {
            return null;
        }

        LocalDateTime date = null;
        List<String> list = Lists.newArrayList();

        list.add("yyyy-MM-dd HH:mm");
        list.add("yyyy-M-dd HH:mm");
        list.add("yyyy-MM-d HH:mm");
        list.add("yyyy-M-d HH:mm");
        list.add("yyyy/MM/dd HH:mm");
        list.add("yyyy/M/dd HH:mm");
        list.add("yyyy/MM/d HH:mm");
        list.add("yyyy/M/d HH:mm");
        list.add("yyyy.MM.dd HH:mm");
        list.add("yyyy.M.dd HH:mm");
        list.add("yyyy.MM.d HH:mm");
        list.add("yyyy.M.d HH:mm");

        for (String format : list) {
            date = parseStrToDate(strTime, format);
            if (date != null) {
                break;
            }
        }

        return date;
    }


    /**
     * 格式化String时间
     *
     * @param time String类型时间
     * @return timeFormat String类型格式
     * @author majuehao
     * @date 2021/12/23 10:24
     **/
    public static LocalDateTime parseStrToDate(String time, String timeFormat) {
        if (Strings.isNullOrEmpty(time)) {
            return null;
        }

        LocalDateTime date = null;
        try {
            date = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(timeFormat));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取年份范围
     *
     * @param yearList 年份数据
     * @return 收录范围
     * @date 2021/12/24 15:48
     */
    public static String getYearRangeByYearList(List<Integer> yearList) {

        if (CollectionUtils.isEmpty(yearList)) {
            return "";
        }

        StringBuilder yearRange = new StringBuilder();

        //按照年份从小到大排序
        Collections.sort(yearList);

        Integer startYear = 0;
        Integer endYear = 0;

        if (yearList.size() == 1) {
            yearRange.append(yearList.get(0));
            return yearRange.toString();
        }

        //循环收录的年份，判断是否有断层，如果有就组合起来添加到收录范围
        for (int i = 0; i < yearList.size(); i++) {

            //从头开始(年份最小的)，获取收录的年份
            Integer year = yearList.get(i);
            if (startYear == 0) {
                startYear = year;
                continue;
            }

            //判断是否是最后一年
            if (i == yearList.size() - 1) {
                endYear = year;
            }

            //当前年份的上一年(去年)
            Integer lastYear = year - 1;
            //判断去年的年份是否和 上一个取的收录年份一致
            if (!lastYear.equals(yearList.get(i - 1))) {
                //不一致那就代表年份不是逐年的，有断层
                //那么久要组合一下，用开始年份和上一个取的收录范围组合成一个收录范围
                yearRange.append(startYear).append("-").append(yearList.get(i - 1)).append(";");
                //将开始年份设置为当前取的年份
                startYear = year;
            }
        }

        if (Objects.equals(startYear, endYear)) {
            //将最后一段的年份组合成新的收录范围
            yearRange.append(startYear);
        } else {
            //将最后一段的年份组合成新的收录范围
            yearRange.append(startYear).append("-").append(endYear);
        }
        return yearRange.toString();
    }

}
