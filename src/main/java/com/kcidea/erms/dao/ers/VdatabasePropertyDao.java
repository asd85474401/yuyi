package com.kcidea.erms.dao.ers;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.ers.VdatabaseProperty;
import com.kcidea.erms.model.common.IdNameModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/24
 **/
@DS("slave_ers")
public interface VdatabasePropertyDao extends BaseMapper<VdatabaseProperty> {

    /**
     * 根据学校id查询属性集合
     *
     * @param sid 学校id
     * @return 数据库属性集合
     * @author yeweiwei
     * @date 2021/11/24 19:17
     */
    List<IdNameModel> findListBySid(@Param("sid") Long sid);
}
