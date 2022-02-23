package com.kcidea.erms.dao.database;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.database.DatabaseBaseInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/1/5 13:38
 **/
public interface DatabaseBaseInfoDao extends BaseMapper<DatabaseBaseInfo> {

    /**
     * 根据学校id、数据库id，查询数据库基础信息
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库评估信息
     * @author yeweiwei
     * @date 2021/11/26 13:52
     */
    DatabaseBaseInfo findOneBySidDid(@Param("sid") Long sid, @Param("did") Long did);

    /**
     * 根据学校id查询数据库的地区
     *
     * @param sid 学校id
     * @return 数据库地区
     * @author huxubin
     * @date 2022/1/6 14:42
     */
    List<String> findAreaListBySid(@Param("sid") Long sid);

    /**
     * 根据学校id查询数据库的性质
     *
     * @param sid 学校id
     * @return 数据库性质
     * @author huxubin
     * @date 2022/1/6 14:42
     */
    List<String> findNatureTypeListBySid(@Param("sid") Long sid);

    /**
     * 根据学校id、地区、数据库性质、全文标识，查询数据库id集合
     *
     * @param sid          学校id
     * @param area         地区
     * @param natureType   数据库性质
     * @param fulltextFlag 全文标识
     * @return 数据库id集合
     * @author majuehao
     * @date 2022/1/11 14:27
     **/
    Set<Long> findListBySidAreaNatureTypeFulltextFlag(@Param("sid") Long sid,
                                                      @Param("area") String area,
                                                      @Param("natureType") String natureType,
                                                      @Param("fulltextFlag") Integer fulltextFlag);

    /**
     * 根据学校id、数据库id，查询数据库数量
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库数量
     * @author majuehao
     * @date 2022/1/11 14:27
     **/
    int findCountBySidDid(@Param("sid") Long sid, @Param("did") Long did);
}