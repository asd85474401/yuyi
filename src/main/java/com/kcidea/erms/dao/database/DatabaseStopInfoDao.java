package com.kcidea.erms.dao.database;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.database.DatabaseStopInfo;
import com.kcidea.erms.model.database.stopinfo.DatabaseStopInfoModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/24 13:28
 **/
public interface DatabaseStopInfoDao extends BaseMapper<DatabaseStopInfo> {

    /**
     * 根据学校id、年份、数据库id，查询数据库停订列表
     *
     * @param sid   学校id
     * @param vYear 订购年份
     * @param did   数据库id
     * @return 数据库停订列表
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    List<DatabaseStopInfoModel> findListBySidYearDid(@Param("sid") Long sid,
                                                     @Param("vYear") Integer vYear,
                                                     @Param("did") Long did);

    /**
     * 根据学校id、年份、数据库id，查询数据库停订列表的数量
     *
     * @param sid   学校id
     * @param vYear 订购年份
     * @param did   数据库id
     * @return 数据库停订列表
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    Integer findCountBySidYearDid(@Param("sid") Long sid,
                                  @Param("vYear") Integer vYear,
                                  @Param("did") Long did);

    /**
     * 根据学校id、停订id，查询数据库停订详情
     *
     * @param sid 学校id
     * @param databaseStopId  停订id
     * @return 数据库停订详情
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    DatabaseStopInfo findOneBySidDatabaseStopId(@Param("sid") Long sid,
                                                @Param("databaseStopId") Long databaseStopId);

    /**
     * 根据学校id、停订id，删除数据库停订详情
     *
     * @param sid 学校id
     * @param databaseStopId  停订id
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    void deleteBySidDatabaseStopId(@Param("sid") Long sid,
                                   @Param("databaseStopId") Long databaseStopId);
}