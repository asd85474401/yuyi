package com.kcidea.erms;

import com.kcidea.erms.common.util.SortUrlUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ErmsApplicationTests {

    @Test
    public void contextLoads() {
        //数据库的apiKey
        String apiKey = "2934ec6972d947c4865d31b97042b825";

        //原来没有，需要请求接口生成一个新的
        String sortUrl = SortUrlUtil.createSortUrl(apiKey);

        System.out.println("生成的短链接如下：" + sortUrl);
    }

}
