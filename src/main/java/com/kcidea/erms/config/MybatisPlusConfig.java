package com.kcidea.erms.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/9
 **/
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        //此处设置了MyBatis最大的分页大小，超过了也会被强制设置回来
        return new PaginationInterceptor().setLimit(1000).setOverflow(true);
    }

}
