package com.kcidea.erms.common.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/10
 **/
@Slf4j
public class Md5Util {

    /**
     * 获取MD5的密码
     *
     * @param password 密码
     * @return MD5的密码
     * @author huxubin
     * @date 2021/11/10 16:50
     */
    public static String toMd5(String password) {
        return md5String(password.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 转换到MD5的String
     *
     * @param data 转换的结果
     * @return md5
     * @author huxubin
     * @date 2021/11/10 16:39
     */
    public static String md5String(byte[] data) {
        StringBuilder md5Str = new StringBuilder();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buf = md5.digest(data);

            for (byte b : buf) {
                md5Str.append(getByte2Hex(b));
            }
        } catch (Exception ex) {
            log.error("转换MD5出错，原因：" + ex.getMessage(), ex);
        }

        return md5Str.toString();
    }

    /**
     * 获取字节的转换数据
     *
     * @param b 字节
     * @return 转换后的数据
     * @author huxubin
     * @date 2021/11/10 16:48
     */
    private static String getByte2Hex(byte b) {
        StringBuilder hex = new StringBuilder(Integer.toHexString(b));
        if (hex.length() > 2) {
            hex = new StringBuilder(hex.substring(hex.length() - 2));
        }

        while (hex.length() < 2) {
            hex.insert(0, "0");
        }

        return hex.toString();
    }
}
