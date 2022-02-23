package com.kcidea.erms.config;

import com.google.common.collect.Sets;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/18
 **/
@WebFilter(urlPatterns = "/*", filterName = "CORSFilter")
public class CORSFilterConfig implements Filter {

    @Override
    public void destroy() {
    }

    /**
     * 此过滤器只是处理跨域问题
     *
     * @param servletRequest  请求
     * @param servletResponse 响应
     * @param chain           chain
     * @throws ServletException 异常
     * @throws IOException      异常
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String[] originArr = {
                "http://192.168.1.182:8083",
                "http://192.168.1.155",
                "http://192.168.1.88:8083",
                "http://202.114.9.206",
        };

        Set<String> allowedOrigins = Sets.newHashSet(originArr);
        String originHeader = ((HttpServletRequest) servletRequest).getHeader("Origin");

        if (allowedOrigins.contains(originHeader)) {
            response.setHeader("Access-Control-Allow-Origin", originHeader);
        }

        response.setHeader("Access-Control-Allow-Methods", "POST,GET,DELETE,PUT,OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, " +
                "authorization, Authorization, X-Requested-With ,accessToken, token , companyToken");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.addHeader("X-Frame-Options", "DENY");
        response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate, max-age=0");
        response.addHeader("Cache-Control", "no-cache='set-cookie'");
        response.setHeader("Set-Cookie", "HttpOnly;Secure;SameSite=None");
        response.addHeader("Pragma", "no-cache");
        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

}
