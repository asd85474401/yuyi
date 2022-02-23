package com.kcidea.erms.common.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/01/05
 **/
public class BeanListCopyUtils extends BeanUtils {

    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }

    /**
     * 使用场景：Entity、Bo、Vo层数据的复制，因为BeanUtils.copyProperties只能给目标对象的属性赋值，却不能在List集合下循环赋值，因此添加该方法
     * * 如：List<AdminEntity> 赋值到 List<AdminVo> ，List<AdminVo>中的 AdminVo 属性都会被赋予到值
     * * S: 数据源类 ，T: 目标类::new(eg: AdminVo::new)
     *
     * @author majuehao
     * @date 2022/1/5 18:54
     **/
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, ColaBeanUtilsCallBack<S, T> callBack) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            copyProperties(source, t);
            if (callBack != null) {
                // 回调
                callBack.callBack(source, t);
            }
            list.add(t);
        }
        return list;
    }

    /**
     * ColaBeanUtilsCallBack接口，使用java8的lambda表达式注解
     *
     * @author majuehao
     * @date 2022/1/5 18:54
     **/
    @FunctionalInterface
    public interface ColaBeanUtilsCallBack<S, T> {

        /**
         * 回调接口
         *
         * @param t 数据源类
         * @param s 目标类::new(eg: AdminVo::new)
         * @author majuehao
         * @date 2022/1/12 11:10
         **/
        void callBack(S t, T s);
    }

}