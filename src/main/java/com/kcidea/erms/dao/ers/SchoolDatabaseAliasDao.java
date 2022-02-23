package com.kcidea.erms.dao.ers;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.ers.SchoolDatabaseAlias;
import com.kcidea.erms.model.common.IdNameModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/24
 **/
@DS("slave_ers")
public interface SchoolDatabaseAliasDao extends BaseMapper<SchoolDatabaseAlias> {
    /**
     * 根据学校id、别名查询
     *
     * @param sid 学校id
     * @param name 别名
     * @return 数据库别名信息
     * @author yeweiwei
     * @date 2021/11/25 15:47
     */
    SchoolDatabaseAlias findOneBySidName(@Param("sid") Long sid, @Param("name") String name);

    /**
     * 根据学校id、数据库id查询
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库别名
     * @author yeweiwei
     * @date 2021/11/26 10:46
     */
    SchoolDatabaseAlias findOneBySidDid(@Param("sid") Long sid, @Param("did") Long did);

    /**
     * 根据学校id查询数据库id、别名集合
     *
     * @param sid 学校id
     * @return 学校id、别名集合
     * @author yeweiwei
     * @date 2021/11/29 16:58
     */
    List<IdNameModel> findIdNameListBySid(@Param("sid") Long sid);
}
