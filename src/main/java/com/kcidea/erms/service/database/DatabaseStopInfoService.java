package com.kcidea.erms.service.database;

import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.database.stopinfo.DatabaseStopInfoModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/24
 **/
public interface DatabaseStopInfoService {

    /**
     * 查询数据库停订列表
     *
     * @param sid        学校id
     * @param vYear      订购年份
     * @param did        数据库id
     * @param languageId 语种id
     * @param type       资源类型
     * @param pageNum    页码
     * @param pageSize   每页数量
     * @return 数据库停订列表
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    PageResult<DatabaseStopInfoModel> findDatabaseStopList(Long sid, Integer vYear, Long did, Long languageId, Long type,
                                                       Integer pageNum, Integer pageSize);

    /**
     * 新增或编辑停订数据库
     *
     * @param sid                   学校id
     * @param userId                学校id
     * @param databaseStopInfoModel 停订数据库详情
     * @return 新增或编辑的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    String addOrUpdateDatabaseStopInfo(Long sid, Long userId, DatabaseStopInfoModel databaseStopInfoModel);

    /**
     * 查询停订的数据库下拉列表
     *
     * @param sid   学校id
     * @param vYear 停订年份
     * @return 数据库集合
     * @author majuehao
     * @date 2021/11/24 14:02
     */
    List<IdNameModel> findDatabaseStopSelectList(Long sid, Integer vYear);

    /**
     * 回显编辑停订数据库
     *
     * @param sid            学校id
     * @param databaseStopId 停订数据库id
     * @return 停订数据库详情
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    DatabaseStopInfoModel findOneById(Long sid, Long databaseStopId);


    /**
     * 删除停订数据库详情
     *
     * @param sid            学校id
     * @param databaseStopId 停订数据库id
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/22 11:03
     **/
    String deleteDatabaseStopInfo(Long sid, Long databaseStopId);

    /**
     * 导出数据库停订列表
     *
     * @param sid        学校id
     * @param vYear      订购年份
     * @param did        数据库id
     * @param languageId 语种id
     * @param type       资源类型
     * @return 导出的文件
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    ResponseEntity<byte[]> exportDatabaseStopList(Long sid, Integer vYear, Long did, Long languageId, Long type);
}
