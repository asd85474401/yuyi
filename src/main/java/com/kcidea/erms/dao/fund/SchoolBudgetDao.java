package com.kcidea.erms.dao.fund;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.fund.SchoolBudget;
import com.kcidea.erms.model.common.IdNameModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/15
 **/
public interface SchoolBudgetDao extends BaseMapper<SchoolBudget> {

    /**
     * 根据学校id,预算id,名字,查找预算来源数量
     *
     * @param sid  学校id
     * @param name 名字
     * @return 预算来源数量
     * @author majuehao
     * @date 2021/11/15 16:23
     **/
    int findCountBySidName(@Param("sid") Long sid, @Param("name") String name);

    /**
     * 根据学校id获取预算来源下拉集合
     *
     * @param sid 学校id
     * @return 预算来源下拉集合
     * @author yeweiwei
     * @date 2021/11/16 13:43
     */
    List<IdNameModel> findSelectListBySid(@Param("sid") Long sid);
}
