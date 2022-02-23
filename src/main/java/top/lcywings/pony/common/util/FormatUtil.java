package top.lcywings.pony.common.util;

import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;
import top.lcywings.pony.common.constant.Constant;
import top.lcywings.pony.enums.common.EnumTrueFalse;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/9
 **/
public class FormatUtil {

    /**
     * 格式化对应的值
     *
     * @param data 传入的内容
     * @return 格式化后的String值
     */
    public static String formatValue(Object data) {
        String value = "";
        if (data != null) {
            value = data.toString();
        }
        value = value.trim();
        return value;
    }

    /**
     * 去掉首部的字符
     *
     * @param match    匹配的字符
     * @param sequence 字符串
     * @return 去除后的结果
     * @author huxubin
     * @date 2020/7/22
     **/
    public static String trimStart(char match, CharSequence sequence) {
        CharMatcher letterMatcher = CharMatcher.is(match);
        return letterMatcher.trimLeadingFrom(sequence);
    }

    /**
     * 去尾部匹配的字符
     *
     * @param match    匹配的字符
     * @param sequence 字符串
     * @return 去除后的结果
     * @author huxubin
     * @date 2020/7/22
     **/
    public static String trimEnd(char match, CharSequence sequence) {
        CharMatcher letterMatcher = CharMatcher.is(match);
        return letterMatcher.trimTrailingFrom(sequence);
    }

    /**
     * 去掉首和尾部匹配的字符
     *
     * @param match    匹配的字符
     * @param sequence 字符串
     * @return 去除后的结果
     * @author huxubin
     * @date 2020/7/22
     **/
    public static String trim(char match, CharSequence sequence) {
        CharMatcher letterMatcher = CharMatcher.is(match);
        return letterMatcher.trimFrom(sequence);
    }

    /**
     * 是否标识格式化
     *
     * @param trueFalseFlag 标识
     * @return 是否
     * @author majuehao
     * @date 2022/1/14 18:25
     **/
    public static String formatTrueFlagValue(Integer trueFalseFlag) {
        if (trueFalseFlag == null) {
            return "";
        }

        return trueFalseFlag == EnumTrueFalse.是.getValue() ? EnumTrueFalse.是.getName() : EnumTrueFalse.否.getName();
    }

    /**
     * 数量格式化
     *
     * @param count 数量
     * @return N/A或字符型数量
     * @author majuehao
     * @date 2022/1/24 16:02
     **/
    public static String formatCount(Integer count) {
        if (count == null) {
            return Constant.NO_COUNT;
        }

        return count == 0 ? Constant.NO_COUNT : ConvertUtil.objToString(count);
    }

    /**
     * 格式化中文语境下的空
     *
     * @param str 字符串
     * @return java.lang.String
     * @author majuehao
     * @date 2022/1/25 8:52
     **/
    public static String formatChainNoValue(String str) {
        if (Strings.isNullOrEmpty(str)) {
            return Constant.CHAIN_NO;
        }

        return formatValue(str);
    }

    /**
     * 格式化数据时间
     *
     * @param dataTime 数据时间
     * @return 格式化的数据时间
     * @author majuehao
     * @date 2022/1/27 10:15
     **/
    public static String formatDataTime(String dataTime) {
        if (Strings.isNullOrEmpty(dataTime)) {
            return "";
        }

        StringBuilder formatDataTime = new StringBuilder();
        String[] dataTimes = dataTime.split("");
        for (String time : dataTimes) {
            if (time.equals(Constant.SplitChar.LINE_CHAR)) {
                formatDataTime.append(Constant.Pattern.YEAR_STR);
            }
            formatDataTime.append(time);
        }
        formatDataTime.append(Constant.Pattern.YEAR_STR);

        return formatDataTime.toString();
    }
}
