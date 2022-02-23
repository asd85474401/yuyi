package com.kcidea.erms.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/10
 **/
@Slf4j
public class IpUtil {

    /**
     * 获取IP地址
     *
     * @param request 请求
     * @return IP地址
     * @author huxubin
     * @date 2021/11/10 15:45
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip)) {
                //根据网卡取本机配置的IP
                try {
                    ip = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException e) {
                    log.error("Ip解析出错，原因： " + e.getMessage());
                }
            }
        }

        //对于通过多个代理的情况， 第一个IP为客户端真实IP,多个IP按照','分割 "***.***.***.***".length() =
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

}
