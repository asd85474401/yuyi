package com.kcidea.erms.dao.ers;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.ers.SchoolDatabaseSubjectRel;
import com.kcidea.erms.model.common.IdLongModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/24
 **/
@DS("slave_ers")
public interface SchoolDatabaseSubjectRelDao extends BaseMapper<SchoolDatabaseSubjectRel> {
    /**
     * 根据学校id、数据库id查询一级学科
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 学科id集合
     * @author yeweiwei
     * @date 2021/11/24 18:36
     */
    Set<Long> findSubjectIdSetBySidDid(@Param("sid") Long sid, @Param("did") Long did);

    /**
     * 根据学校id、数据库id删除
     *
     * @param sid 学校id
     * @param did 数据库id
     * @author yeweiwei
     * @date 2021/11/29 13:32
     */
    void deleteBySidDid(@Param("sid") Long sid, @Param("did") Long did);

    /**
     * 根据学校id、学科id查询覆盖此学科的所有数据库id
     *
     * @param sid       学校id
     * @param subjectId 学科id
     * @return 数据库id集合
     * @author yeweiwei
     * @date 2021/12/8 9:29
     */
    Set<Long> findDidSetBySidSubjectId(@Param("sid") Long sid, @Param("subjectId") Long subjectId);

    /**
     * 根据学校id查询数据库学科覆盖集合
     *
     * @param sid 学校id
     * @return 数据库学科覆盖集合
     * @author yeweiwei
     * @date 2021/12/8 9:33
     */
    List<IdLongModel> findListBySid(@Param("sid") Long sid);
}
