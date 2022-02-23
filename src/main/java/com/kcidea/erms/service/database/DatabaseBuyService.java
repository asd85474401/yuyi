package com.kcidea.erms.service.database;

import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.database.buyplan.DatabaseBuyListModel;

import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/12/7
 **/
public interface DatabaseBuyService {
    /**
     * 查询数据库采购列表
     *
     * @param sid        学校id
     * @param vYear      年份
     * @param did        数据库id
     * @param language   语种
     * @param type       资源类型
     * @param natureType 数据库性质
     * @param buyType    采购类型
     * @param subjectId  学科覆盖id
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 数据库采购列表
     * @author yeweiwei
     * @date 2021/12/7 20:01
     */
    PageResult<DatabaseBuyListModel> findBuyList(Long sid, Integer vYear, Long did, Long language, Long type, Integer natureType,
                                                 Integer buyType, Long subjectId, Integer pageNum,
                                                 Integer pageSize);

    /**
     * 查询在学校订购列表中的数据库id名称集合
     *
     * @param sid   学校id
     * @param vYear 年份
     * @return 数据库id名称集合
     * @author yeweiwei
     * @date 2021/12/8 13:31
     */
    List<IdNameModel> findDatabaseSelectList(Long sid, Integer vYear);
}
