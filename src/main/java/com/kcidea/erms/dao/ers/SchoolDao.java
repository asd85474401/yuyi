package com.kcidea.erms.dao.ers;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.ers.School;
import org.apache.ibatis.annotations.Param;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/16
 **/
@DS("slave_ers")
public interface SchoolDao extends BaseMapper<School> {
    /**
     * 查询学校开始年份
     *
     * @param id 学校id
     * @return 开始年份
     * @author yeweiwei
     * @date 2021/11/16 11:52
     */
    Integer findStartYearById(@Param("id") Long id);

    /**
     * 根据学校id查询学校名称
     *
     * @param sid 学校id
     * @return 学校名称
     * @author yeweiwei
     * @date 2021/11/30 19:10
     */
    String findSchoolNameBySid(@Param("sid") Long sid);
}
