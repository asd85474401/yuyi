package com.kcidea.erms.dao.database;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.database.DatabaseAccessUrl;
import com.kcidea.erms.model.company.AccessUrlInfoModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/1/12 17:01
 **/
public interface DatabaseAccessUrlDao extends BaseMapper<DatabaseAccessUrl> {
    /**
     * 根据学校id、数据库id，查询数据库访问信息列表
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库访问信息列表
     * @author majuehao
     * @date 2022/1/12 17:06
     **/
    List<AccessUrlInfoModel> findListBySidDid(@Param("sid") Long sid, @Param("did") Long did);

    /**
     * 根据学校id、数据库id、访问id，查询数据库访问链接
     *
     * @param sid      学校id
     * @param did      数据库id
     * @param accessId 访问id
     * @return 数据库访问链接
     * @author majuehao
     * @date 2022/1/6 19:24
     **/
    DatabaseAccessUrl findOneBySidDidAccessId(@Param("sid") Long sid, @Param("did") Long did,
                                              @Param("accessId") Long accessId);

    /**
     * 根据学校id、数据库id、访问id，删除数据库访问链接
     *
     * @param sid      学校id
     * @param did      数据库id
     * @param accessId 访问id
     * @author majuehao
     * @date 2022/1/6 19:24
     **/
    void deleteBySidDidAccessId(@Param("sid") Long sid, @Param("did") Long did,
                                @Param("accessId") Long accessId);


    /**
     * 根据学校i，查询数据库访问链接
     *
     * @param sid      学校id
     * @author majuehao
     * @date 2022/1/6 19:24
     **/
    List<DatabaseAccessUrl> findListBySid(@Param("sid") Long sid);
}