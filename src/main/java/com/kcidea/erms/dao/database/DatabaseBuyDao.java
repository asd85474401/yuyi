package com.kcidea.erms.dao.database;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.database.DatabaseBuy;
import com.kcidea.erms.model.database.buyplan.DatabaseBuyListModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/12/7
 **/
public interface DatabaseBuyDao extends BaseMapper<DatabaseBuy> {
    /**
     * 根据条件检索数据库采购列表
     *
     * @param sid        学校id
     * @param did        数据库id
     * @param vYear      年份
     * @param natureType 数据库性质
     * @param buyType    数据库采购类型
     * @return 数据库采购列表
     * @author yeweiwei
     * @date 2021/12/7 20:24
     */
    List<DatabaseBuyListModel> findListBySidDidYearNatureBuyType(@Param("sid") Long sid, @Param("did") Long did,
                                                                 @Param("vYear") Integer vYear,
                                                                 @Param("natureType") Integer natureType,
                                                                 @Param("buyType") Integer buyType);

    /**
     * 根据年份、学校id查询在订购列表中的数据库id集合
     *
     * @param sid   学校id
     * @param vYear 年份
     * @return 数据库id集合
     * @author yeweiwei
     * @date 2021/12/8 13:33
     */
    Set<Long> findDidSetBySidYear(@Param("sid") Long sid, @Param("vYear") Integer vYear);
}
