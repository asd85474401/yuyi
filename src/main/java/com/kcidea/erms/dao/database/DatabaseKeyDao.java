package com.kcidea.erms.dao.database;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.database.DatabaseKey;
import org.apache.ibatis.annotations.Param;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/24 13:28
 **/
public interface DatabaseKeyDao extends BaseMapper<DatabaseKey> {

    /**
     * 根据apiKey查询
     * @param apiKey apiKey
     * @return 查询的结果
     * @author huxubin
     * @date 2022/1/5 13:43
     */
    DatabaseKey findOneByApiKey(@Param("apiKey") String apiKey);

    /**
     * 查询数据库的apiKey
     * @param sid 学校id
     * @param did 数据库id
     * @return 查询的结果
     * @author huxubin
     * @date 2022/1/5 13:58
     */
    DatabaseKey findOneBySidDid(@Param("sid") Long sid, @Param("did") Long did);
}