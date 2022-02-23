package com.kcidea.erms.service.ers;

import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.database.detail.DatabaseInfoModel;

import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/25
 **/
public interface DatabaseService {
    /**
     * 查询学校自定义和系统默认的数据库集合
     *
     * @param sid        学校id
     * @param name       数据库名称
     * @param letter     首字母
     * @param propertyId 数据库属性
     * @param languageId 数据库语种
     * @param pageNum    页码
     * @param pageSize   每页数量
     * @return 数据库集合
     * @author yeweiwei
     * @date 2021/11/25 16:19
     */
    PageResult<IdNameModel> findDatabaseSelectList(Long sid, String name, String letter, Long propertyId,
                                                   Long languageId, Integer pageNum, Integer pageSize);

    /**
     * 查询数据库信息
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库信息
     * @author yeweiwei
     * @date 2021/11/25 11:04
     */
    DatabaseInfoModel findDatabaseInfo(Long sid, Long did);

    /**
     * 新增数据库
     *
     * @param sid          学校id
     * @param databaseInfo 数据库信息
     * @param userId       操作人id
     * @return 数据库信息
     * @author yeweiwei
     * @date 2021/11/25 14:53
     */
    IdNameModel addDatabase(Long sid, DatabaseInfoModel databaseInfo, Long userId);

    /**
     * 联想查询数据库
     *
     * @param sid    学校id
     * @param search 检索条件
     * @return 数据库集合
     * @author majuehao
     * @date 2021/11/26 9:29
     **/
    List<IdNameModel> findDatabaseListBySearch(Long sid, String search);

    /**
     * 更新数据库
     *
     * @param sid          学校id
     * @param databaseInfo 数据库信息
     * @param userId       操作人id
     * @return 更新结果
     * @author yeweiwei
     * @date 2021/11/26 9:47
     */
    String updateDatabase(Long sid, DatabaseInfoModel databaseInfo, Long userId);

    /**
     * 新增数据商
     *
     * @param sid         学校id
     * @param userId      操作人id
     * @param companyName 数据商名称
     * @param addFlag     新增标识
     * @return 数据商id
     * @author yeweiwei
     * @date 2021/12/2 14:07
     */
    Long findCompanyId(Long sid, Long userId, String companyName, boolean addFlag);

    /**
     * 新增代理商
     *
     * @param sid       学校id
     * @param userId    操作人id
     * @param agentName 代理商名称
     * @param addFlag   新增标识
     * @return 代理商id
     * @author yeweiwei
     * @date 2021/12/2 14:07
     */
    Long findAgentId(Long sid, Long userId, String agentName, boolean addFlag);
}
