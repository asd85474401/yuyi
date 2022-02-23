package top.lcywings.pony.common.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * 反射相关的工具类
 *
 * @author huxubin
 * @version 1.0
 * @date 2020/6/22 10:31
 */
@Slf4j
public class ReflexUtil {

    /**
     * 获取反射后的BigDecimal值
     *
     * @param obj          对象
     * @param propertyName 对应的字段
     * @return 对应的值
     * @author huxubin
     * @date 2020/6/22
     **/
    public static BigDecimal getBigDecimalValue(Object obj, String propertyName) {

        BigDecimal ret = BigDecimal.ZERO;

        if (obj == null) {
            return ret;
        }

        Field field;
        try {

            field = obj.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            Object value = field.get(obj);
            ret = ConvertUtil.objToDec(value);
        } catch (Exception e) {
            log.error("通过反射获取对象的BigDecimal值出错啦，原因：{}", e.getMessage());
        }
        return ret;
    }

    /**
     * 获取反射后的int值
     *
     * @param obj          对象
     * @param propertyName 对应的字段
     * @return 对应的值
     */
    public static int getIntValue(Object obj, String propertyName) {
        int ret = 0;

        if (obj == null) {
            return ret;
        }

        Field field;
        try {
            field = obj.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            Object value = field.get(obj);
            ret = ConvertUtil.objToInt(value);
        } catch (Exception e) {
            log.error("通过反射获取对象的Int值出错啦，原因：{}", e.getMessage());
        }
        return ret;
    }

    /**
     * 给对象的属性加
     *
     * @param obj          对象
     * @param propertyName 字段名
     * @param value        对应的值
     * @return 返回的结果
     */
    public static <T> T setValue(T obj, String propertyName, Object value) {
        Field field = null;
        try {
            field = obj.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            log.error("通过反射给对象赋值出错啦，原因：{}", e.getMessage());
        }
        return obj;
    }

    /**
     * 通过反射给对象增加值
     *
     * @param obj          对象
     * @param propertyName 属性
     * @param addValue     增加的值
     * @return 增加后的对象
     * @author huxubin
     * @date 2020/8/7 14:15
     */
    public static <T> T addBigDecimalValue(T obj, String propertyName, BigDecimal addValue) {
        Field field = null;
        try {
            field = obj.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            BigDecimal tempSum = ConvertUtil.objToDec(field.get(obj), BigDecimal.ZERO);
            tempSum = tempSum.add(addValue);
            field.set(obj, tempSum);
        } catch (Exception e) {
            log.error("通过反射给对象增加值出错啦，原因：{}", e.getMessage());
        }
        return obj;
    }

    /**
     * 通过反射给对象增加值
     *
     * @param obj          对象
     * @param propertyName 属性
     * @param addValue     增加的值
     * @return 增加后的对象
     * @author huxubin
     * @date 2020/8/7 14:15
     */
    public static <T> T addIntValue(T obj, String propertyName, int addValue) {
        Field field = null;
        try {
            field = obj.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            int tempSum = ConvertUtil.objToInt(field.get(obj));
            tempSum += addValue;
            field.set(obj, tempSum);
        } catch (Exception e) {
            log.error("通过反射给对象增加值出错啦，原因：{}", e.getMessage());
        }
        return obj;
    }

}
