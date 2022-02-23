package com.kcidea.erms.service.database.impl;

import com.kcidea.erms.dao.ers.SchoolDatabaseListDao;
import com.kcidea.erms.domain.ers.SchoolDatabaseList;
import com.kcidea.erms.service.database.SchoolDataBaseListService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/25
 **/
@Service
public class SchoolDataBaseListServiceImpl implements SchoolDataBaseListService {

    @Resource
    private SchoolDatabaseListDao schoolDatabaseListDao;

    /**
     * 级联修改ERS学校数据库表
     *
     * @param schoolDatabaseList 学校数据库详情
     * @param insertFlag         新增标识
     * @author majuehao
     * @date 2021/12/25 17:23
     **/
    @Override
    public void addOrUpdateDataStopInfo(SchoolDatabaseList schoolDatabaseList, boolean insertFlag) {
        if (insertFlag) {
            schoolDatabaseListDao.insert(schoolDatabaseList);
        } else {
            schoolDatabaseListDao.updateById(schoolDatabaseList);
        }
    }

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
    @Override
    public SchoolDatabaseList findOneBySidYearDid(Long sid, Integer vYear, Long did) {
        return schoolDatabaseListDao.findOneBySidYearDid(sid, vYear, did);
    }
}
