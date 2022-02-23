package com.kcidea.erms.dao.company;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.company.CompanyDatabaseBaseInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/1/5 13:38
 **/
public interface CompanyDatabaseBaseInfoDao extends BaseMapper<CompanyDatabaseBaseInfo> {

    /**
     * 根据学校id、数据库id，查询数量
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库信息数量
     * @author majuehao
     * @date 2022/1/5 16:37
     **/
    int findCountBySidDid(@Param("sid") Long sid, @Param("did") Long did);

    /**
     * 根据学校id、数据库id，查询数据库信息
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库信息
     * @author majuehao
     * @date 2022/1/5 16:37
     **/
    CompanyDatabaseBaseInfo findOneBySidDid(@Param("sid") Long sid, @Param("did") Long did);

    /**
     * 根据学校id、数据库id，查询数据库名字
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库名字
     * @author majuehao
     * @date 2022/1/5 16:37
     **/
    String findNameBySidDid(@Param("sid") Long sid, @Param("did") Long did);

    /**
     * 根据学校id、数据库id，删除数据库信息
     *
     * @param sid 学校id
     * @param did 数据库id
     * @author majuehao
     * @date 2022/1/5 16:37
     **/
    void deleteBySidDid(@Param("sid") Long sid, @Param("did") Long did);
}