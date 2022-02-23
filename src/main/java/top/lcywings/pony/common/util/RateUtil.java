package top.lcywings.pony.common.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/30
 **/
@Slf4j
public class RateUtil {

    /**
     * 计算两个值的占比，四舍五入，BigDecimal 类型的数据
     *
     * @param oneNumber 分子
     * @param allNumber 分母
     * @param scale 保留位数
     * @return 百分占比
     * @author yeweiwei
     * @date 2021/12/1 8:56
     */
    public static BigDecimal find100PointRatioByTwoBigDecimal(BigDecimal oneNumber, BigDecimal allNumber, int scale) {

        //如果传入的2个数值有一个小于0，返回0
        if (oneNumber.compareTo(BigDecimal.ZERO) < 1 || allNumber.compareTo(BigDecimal.ZERO) < 1) {
            return BigDecimal.ZERO;
        }
        //如果分母为0，返回0
        if (allNumber.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        //返回
        return (oneNumber.multiply(new BigDecimal(100))).divide(allNumber, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 计算两个值的占比，四舍五入，BigDecimal 类型的数据
     *
     * @param oneNumber 分子
     * @param allNumber 分母
     * @param scale 保留位数
     * @return 百分占比
     * @author yeweiwei
     * @date 2021/12/1 8:56
     */
    public static BigDecimal find100PointRatioByTwoInteger(Integer oneNumber, Integer allNumber, int scale) {
        if (oneNumber == null || allNumber == null) {
            return null;
        }
        //数值转换
        BigDecimal oneBigDecimal = new BigDecimal(oneNumber);
        BigDecimal allBigDecimal = new BigDecimal(allNumber);

        //计算数值
        return find100PointRatioByTwoBigDecimal(oneBigDecimal, allBigDecimal, scale);
    }

}
