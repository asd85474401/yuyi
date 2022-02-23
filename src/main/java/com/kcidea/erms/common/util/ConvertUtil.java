package com.kcidea.erms.common.util;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huxubin
 * @version 1.0
 * @date 2020/7/22
 **/
public class ConvertUtil {

    /**
     * 任意类型转换成Int型
     *
     * @param object 传入的值
     * @return int型的值
     */
    public static int objToInt(Object object) {
        return objToInt(object, 0);
    }

    /**
     * 将任意类型转为换数值
     *
     * @param object       传入的值
     * @param defaultValue 默认值
     * @return 转换的结果
     * @author huxubin
     * @date 2020/7/22
     **/
    public static int objToInt(Object object, int defaultValue) {
        int ret = defaultValue;
        if (object == null) {
            return ret;
        }
        try {
            String tmp = object.toString();
            int dotIndex = tmp.indexOf(".");
            if (dotIndex > 0) {
                tmp = tmp.substring(0, dotIndex);
            }
            tmp = tmp.replace(",", "");
            ret = Integer.parseInt(tmp);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
        return ret;
    }


    /**
     * 将任意类型转换为BigDecimal
     *
     * @param object 传入的内容
     * @return 转换后的结果
     * @author huxubin
     * @date 2020/7/22
     **/
    public static BigDecimal objToDec(Object object) {
        return objToDec(object, BigDecimal.ZERO);
    }

    /**
     * 将任意类型转换成decimal
     *
     * @param object       传入的内容
     * @param defaultValue 默认值
     * @return 转换的结果
     * @author huxubin
     * @date 2020/7/22
     **/
    public static BigDecimal objToDec(Object object, BigDecimal defaultValue) {
        return objToDec(object, defaultValue, 4);
    }

    /**
     * 将任意类型转换成decimal
     *
     * @param object       传入的内容
     * @param defaultValue 默认值
     * @param scale        保留位数
     * @return 转换的结果
     * @author yeweiwei
     * @date 2021/6/18 17:31
     */
    public static BigDecimal objToDec(Object object, BigDecimal defaultValue, Integer scale) {
        BigDecimal ret = defaultValue;
        if (object == null) {
            return ret;
        }
        try {
            String tmp = object.toString();
            tmp = tmp.replace(",", "");
            ret = new BigDecimal(tmp);
            ret = ret.setScale(scale, BigDecimal.ROUND_HALF_UP);
        } catch (NumberFormatException e) {
            return defaultValue;
        }

        return ret;
    }

    /**
     * 将任意类型转换成Long
     *
     * @param object 传入的内容
     * @return 转换的结果
     * @author huxubin
     * @date 2020/7/28
     **/
    public static Long objToLong(Object object) {
        return objToLong(object, 0L);
    }

    /**
     * 将任意类型转换成Long
     *
     * @param object       传入的内容
     * @param defaultValue 默认值
     * @return 转换的结果
     * @author huxubin
     * @date 2020/7/28
     **/
    public static Long objToLong(Object object, Long defaultValue) {
        Long ret = defaultValue;
        if (object == null) {
            return ret;
        }

        try {
            String tmp = object.toString();
            ret = Long.valueOf(tmp);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
        return ret;
    }

    /**
     * 任意类型转换成string
     *
     * @param value 传入的内容
     * @return java.lang.String
     * @author yqliang
     * @date 2020/8/2
     **/
    public static String objToString(Integer value) {
        if (value == null) {
            return "";
        }
        return Integer.toString(value);
    }

    /**
     * 根据最大长度减少字符串
     *
     * @param name    字符串
     * @param maxSize 最大长度
     * @return 减少后的字符串
     * @author huxubin
     * @date 2020/8/18 13:52
     */
    public static String cutString(String name, int maxSize) {
        if (Strings.isNullOrEmpty(name)) {
            return "";
        }

        if (name.length() > maxSize) {
            name = name.substring(0, maxSize);
        }
        return name;
    }

    /**
     * 多个byte[] 合并成一个
     *
     * @param values byte字节
     * @return 合并后的
     */
    public static byte[] byteMergerAll(byte[]... values) {
        int lengthByte = 0;
        for (byte[] value : values) {
            lengthByte += value.length;
        }
        byte[] allByte = new byte[lengthByte];
        int countLength = 0;
        for (byte[] b : values) {
            System.arraycopy(b, 0, allByte, countLength, b.length);
            countLength += b.length;
        }
        return allByte;
    }

    /**
     * 字符串分割成集合
     *
     * @param value     字符串
     * @param splitChar 分隔符
     * @return 集合
     * @author majuehao
     * @date 2022/1/17 11:18
     **/
    public static List<Long> stringToList(String value, String splitChar) {
        if (Strings.isNullOrEmpty(value)) {
            return Lists.newArrayList();
        }
        if (Strings.isNullOrEmpty(splitChar)) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }
        return Lists.newArrayList(value.split(splitChar)).stream().map(Long::parseLong).collect(Collectors.toList());
    }
}
