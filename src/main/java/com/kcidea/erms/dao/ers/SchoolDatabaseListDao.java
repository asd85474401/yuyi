package com.kcidea.erms.dao.ers;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.ers.SchoolDatabaseList;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/26
 **/
@DS("slave_ers")
public interface SchoolDatabaseListDao extends BaseMapper<SchoolDatabaseList> {
    /**
     * 根据学校id、年份、数据库id查询
     *
     * @param sid   学校id
     * @param vYear 年份
     * @param did   数据库id
     * @return 学校订购数据库
     * @author yeweiwei
     * @date 2021/11/26 14:58
     */
    SchoolDatabaseList findOneBySidYearDid(@Param("sid") Long sid, @Param("vYear") Integer vYear, @Param("did") Long did);

    /**
     * 查询学校订购的数据库id集合
     *
     * @param sid   学校id
     * @param vYear 年份
     * @return 数据库id集合
     * @author yeweiwei
     * @date 2021/11/30 17:07
     */
    Set<Long> findOrderDidSetBySidYear(@Param("sid") Long sid, @Param("vYear") Integer vYear);

    /**
     * 根据学校id、年份、数据库id删除
     *
     * @param sid   学校id
     * @param vYear 年份
     * @param did   数据库id
     * @author yeweiwei
     * @date 2021/11/26 14:58
     */
    void deleteBySidYearDid(@Param("sid") Long sid, @Param("vYear") Integer vYear, @Param("did") Long did);

    /**
     * 根据学校id、数据库id,查询数据库订购了数据库的年份
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 年份集合
     * @author majuehao
     * @date 2021/12/25 18:19
     **/
    List<Integer> findYearListBySidDid(@Param("sid") Long sid, @Param("did") Long did);

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
}
