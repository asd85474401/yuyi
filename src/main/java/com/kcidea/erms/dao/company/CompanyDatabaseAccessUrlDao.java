package com.kcidea.erms.dao.company;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.company.CompanyDatabaseAccessUrl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/1/5 13:38
 **/
public interface CompanyDatabaseAccessUrlDao extends BaseMapper<CompanyDatabaseAccessUrl> {

    /**
     * 根据学校id、数据库id，查询数据库访问链接
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库访问链接列表
     * @author majuehao
     * @date 2022/1/6 19:24
     **/
    List<CompanyDatabaseAccessUrl> findListBySidDid(@Param("sid") Long sid, @Param("did") Long did);

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
    CompanyDatabaseAccessUrl findOneBySidDidAccessId(@Param("sid") Long sid, @Param("did") Long did,
                                                     @Param("accessId") Long accessId);

    /**
     * 根据学校id、数据库id，查询数据库访问链接数量
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库访问链接数量
     * @author majuehao
     * @date 2022/1/7 8:52
     **/
    int findCountBySidDid(@Param("sid") Long sid, @Param("did") Long did);

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
     * 根据学校id、数据库id、链接、id，查询数量
     *
     * @param sid 学校id
     * @param did 数据库id
     * @param url 链接
     * @return 数量
     * @author majuehao
     * @date 2022/1/20 12:19
     **/
    int findCountBySidDidUrl(@Param("sid") Long sid, @Param("did") Long did,
                             @Param("url") String url);

}