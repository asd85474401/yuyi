package top.lcywings.pony;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/10
 **/
@MapperScan("top.lcywings.pony.dao")
@EnableCaching
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
//@EnableScheduling
public class PonyApplication {

    public static void main(String[] args) {
        SpringApplication.run(PonyApplication.class, args);
    }

    /**
     * 解决Jackson导致Long型数据精度丢失问题
     *
     * @return org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
     * @author huxubin
     * @date 2020/12/15
     **/
    @Bean("jackson2ObjectMapperBuilderCustomizer")
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder
                .serializerByType(Long.class, ToStringSerializer.instance)
                .serializerByType(Long.TYPE, ToStringSerializer.instance);
    }

}
