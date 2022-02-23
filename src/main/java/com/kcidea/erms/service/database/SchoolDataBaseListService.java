package com.kcidea.erms.service.database;

import com.kcidea.erms.domain.ers.SchoolDatabaseList;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/25
 **/
public interface SchoolDataBaseListService {

    /**
     * 级联修改ERS学校数据库表
     *
     * @param schoolDatabaseList 学校数据库详情
     * @param insertFlag         学校数据库详情
     * @author majuehao
     * @date 2021/12/25 17:23
     **/
    void addOrUpdateDataStopInfo(SchoolDatabaseList schoolDatabaseList, boolean insertFlag);

    /**
     * 根据学校id，年份，数据库id，查询学校数据库详情
     *
     * @param sid   学校id
     * @param vYear 年份
     * @param did   数据库id
     * @return 学校数据库详情
     * @author majuehao
     * @date 2021/12/25 17:52
     **/
    SchoolDatabaseList findOneBySidYearDid(Long sid, Integer vYear, Long did);
}
