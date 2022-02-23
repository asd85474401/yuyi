package top.lcywings.pony.common.util;

import top.lcywings.pony.common.constant.Constant;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/10
 **/
@Component
public class RedisUtil {

    private static StringRedisTemplate redisTemplate;

    /**
     * 静态注入
     */
    public RedisUtil(StringRedisTemplate template) {
        RedisUtil.redisTemplate = template;
    }

    /**
     * 存放到redis，默认存放7天
     *
     * @param key   key
     * @param value value
     * @author huxubin
     * @date 2021/11/10 9:29
     */
    public static void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value, Constant.RedisTime.DEFAULT);
    }

    /**
     * 根据key查询缓存
     *
     * @param key redis的Key
     * @return 对应的缓存
     * @author huxubin
     * @date 2021/11/10 9:30
     */
    public static Object getByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 根据Key查询缓存
     *
     * @param key redis的Key
     * @return 对应的缓存
     * @author huxubin
     * @date 2021/11/10 9:30
     */
    public static String getStringByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 根据key修改redis的val
     *
     * @param key   key
     * @param value value
     * @author huxubin
     * @date 2021/11/10 9:30
     */
    public static void updateByKey(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 更新键对应的值，但是保持原来的生命周期
     *
     * @param key   key
     * @param value value
     * @author huxubin
     * @date 2021/11/10 9:31
     */
    public static void updateByKeyKeepTime(String key, String value) {
        //0:设置偏移量
        redisTemplate.opsForValue().set(key, value, 0);
    }

    /**
     * 根据key删除缓存
     *
     * @param key key
     * @author huxubin
     * @date 2021/11/10 9:31
     */
    public static void deleteByKey(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 根据key模糊删除数据
     *
     * @param key key
     * @author huxubin
     * @date 2021/11/10 9:31
     */
    public static void deleteByKeyLike(String key) {
        redisTemplate.delete(Objects.requireNonNull(redisTemplate.keys(key + "::*")));
    }

    /**
     * 存放数据并手动设置时间
     *
     * @param key   key
     * @param value value
     * @param time  单位为秒，如果小于0则取默认时间
     * @author huxubin
     * @date 2021/11/10 9:32
     */
    public static boolean setValueAndTime(String key, String value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                setValue(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
