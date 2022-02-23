package com.kcidea.erms.dao.company;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.company.CompanyDatabaseLevel;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/1/10 15:12
 **/
public interface CompanyDatabaseLevelDao extends BaseMapper<CompanyDatabaseLevel> {

    /**
     * 根据学校id,数据库id，查总库对应的子库
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 总库对应的子库
     * @author majuehao
     * @date 2021/10/22 14:02
     */
    Set<Long> findSonDidListBySidDid(@Param("sid") Long sid, @Param("did") Long did);

    /**
     * 根据学校id、数据库id，删除层级信息
     *
     * @param sid      学校id
     * @param did      数据库id
     * @param totalDid 总库id
     * @author majuehao
     * @date 2022/1/5 16:37
     **/
    void deleteBySidDidTotalDid(@Param("sid") Long sid, @Param("did") Long did, @Param("totalDid") Long totalDid);

    /**
     * 根据学校id、数据库id，查询层级关系
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 层级关系
     * @author majuehao
     * @date 2022/1/18 11:52
     **/
    CompanyDatabaseLevel findOneBySidDid(@Param("sid") Long sid, @Param("did") Long did);

    /**
     * 根据学校id、数据库id，查询数量
     *
     * @param sid       学校id
     * @param did       数据库id
     * @param totalFlag 总库标识
     * @param totalDid  总库id
     * @return 数量
     * @author majuehao
     * @date 2022/1/19 19:27
     **/
    int findCountBySidDidTotalFlagTotalDid(@Param("sid") Long sid, @Param("did") Long did,
                                           @Param("totalFlag") int totalFlag, @Param("totalDid") Long totalDid);
}