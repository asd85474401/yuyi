package com.kcidea.erms.dao.ers;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.ers.VdatabasePropertyRel;
import com.kcidea.erms.model.database.DatabasePropertyInfoModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/24
 **/
@DS("slave_ers")
public interface VdatabasePropertyRelDao extends BaseMapper<VdatabasePropertyRel> {

    /**
     * 根据学校id和数据库id查询对应的属性
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库的属性
     * @author yeweiwei
     * @date 2021/11/24 17:12
     */
    List<DatabasePropertyInfoModel> findListBySidDid(@Param("sid") Long sid, @Param("did") Long did);

    /**
     * 根据学校id、数据库id删除
     *
     * @param sid 学校id
     * @param did 数据库id
     * @author yeweiwei
     * @date 2021/11/26 10:58
     */
    void deleteBySidDid(@Param("sid") Long sid, @Param("did") Long did);

    /**
     * 根据学校id 查询所有学校沙箱数据库
     *
     * @param sid 学校id
     * @return 数据库id
     * @author majuehao
     * @date 2021/12/6 14:30
     **/
    Set<Long> findDidSetBySid(@Param("sid") Long sid);

    /**
     * 根据学校id查询属性集合
     *
     * @param sid 学校id
     * @return 属性集合
     * @author yeweiwei
     * @date 2021/12/27 10:30
     */
    List<VdatabasePropertyRel> findListBySid(@Param("sid") Long sid);
}
