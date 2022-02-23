package com.kcidea.erms.dao.ers;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.ers.Subject;
import com.kcidea.erms.model.common.DropDownModel;
import com.kcidea.erms.model.common.IdNameModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;


/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/24
 **/
@DS("slave_ers")
public interface SubjectDao extends BaseMapper<Subject> {


    /**
     * 根据学科类型查询学科id名称
     *
     * @param categoryType 学科类型
     * @return 学科id名称
     * @author yeweiwei
     * @date 2021/10/27 16:21
     */
    List<Subject> findListByCategoryType(@Param("categoryType") Integer categoryType);

    /**
     * 根据学科类型、学科层级查询学科下拉框
     *
     * @param categoryType 学科类型
     * @param treeLevel    学科层级
     * @return 学科下拉框
     * @author yeweiwei
     * @date 2021/11/24 19:37
     */
    List<DropDownModel> findListByCategoryTypeTreeLevel(@Param("categoryType") Integer categoryType,
                                                        @Param("treeLevel") Integer treeLevel);

    /**
     * 根据学科id查询父级学科id和名称
     *
     * @param subjectId 学科id
     * @return 父级学科id和名称
     * @author majuehao
     * @date 2021/12/1 13:36
     **/
    IdNameModel findSubjectIdNameBySubjectId(@Param("subjectId") Long subjectId);

    /**
     * 根据学科类别查询学科id集合
     *
     * @param subjectCategory 学科类别
     * @return 学科id集合
     * @author yeweiwei
     * @date 2021/12/8 9:31
     */
    Set<Long> findIdSetBySubjectCategory(@Param("subjectCategory") Long subjectCategory);
}
