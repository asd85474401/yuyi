package com.kcidea.erms.service.database;

/**
 * @author huxubin
 * @version 1.0
 * @date 2022/1/13
 **/
public interface DataBaseSortUrlService {

    /**
     * 查询数据库的短链接
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 查询的结果
     * @author huxubin
     * @date 2022/1/13 13:54
     */
    String findDataBaseSortUrl(Long sid, Long did);

    /**
     * 更新数据库的短链接
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 查询的结果
     * @author huxubin
     * @date 2022/1/13 13:56
     */
    String updateDataBaseSortUrl(Long sid, Long did);

    /**
     * 根据学校id和数据库id查询对应的apiKey
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库的apiKey
     * @author huxubin
     * @date 2022/2/9 10:18
     */
    String findApiKey(Long sid, Long did);

    /**
     * 更新数据库的apiKey
     * @param sid 学校id
     * @param did 数据库id
     * @return 更新后的apiKey
     * @author huxubin
     * @date 2022/2/9 10:18
     */
    String updateApiKey(Long sid, Long did);

    /**
     * 保存登录的apiKey
     * @param ip ip地址
     * @param apiKey apiKey
     * @return 保存的结果
     * @author huxubin
     * @date 2022/2/9 14:58
     */
    String saveApiKeyLogin(String ip, String apiKey);
}
