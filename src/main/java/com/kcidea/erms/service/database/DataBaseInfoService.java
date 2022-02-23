package com.kcidea.erms.service.database;

import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.domain.database.DatabaseBaseInfo;
import com.kcidea.erms.domain.database.DatabaseContactPeople;
import com.kcidea.erms.model.company.AccessUrlInfoModel;
import com.kcidea.erms.model.company.ContactPeopleInfoModel;
import com.kcidea.erms.model.database.detail.DataBaseTitleDetailModel;
import com.kcidea.erms.model.database.info.DatabaseInfoInsertModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2022/1/6
 **/
public interface DataBaseInfoService {

    /**
     * 查询数据库列表
     *
     * @param sid          学校id
     * @param did          数据库id
     * @param languageId   语种
     * @param area         所在地区
     * @param natureType   数据库性质
     * @param fulltextFlag 是否全文
     * @param propertyId   资源类型
     * @param subjectId    学科id
     * @param pageFlag     是否分页
     * @param pageNum      页码
     * @param pageSize     每页数量
     * @return 学科分类统计列表
     * @author majuehao
     * @date 2021/10/18 10:27
     **/
    PageResult<DataBaseTitleDetailModel> findDatabaseList(Long sid, Long did, Long languageId, String area,
                                                          String natureType, Integer fulltextFlag, Long propertyId,
                                                          Long subjectId, Integer pageFlag, Integer pageNum, Integer pageSize);

    /**
     * 查询子库列表
     *
     * @param sid          学校id
     * @param totalDid     主库数据库id
     * @param languageId   语种
     * @param area         所在地区
     * @param natureType   数据库性质
     * @param fulltextFlag 是否全文
     * @param propertyId   资源类型
     * @param subjectId    学科id
     * @return 数据库列表
     * @author majuehao
     * @date 2021/10/18 10:27
     **/
    List<DataBaseTitleDetailModel> findSonDatabaseList(Long sid, Long totalDid, Long languageId, String area,
                                                       String natureType, Integer fulltextFlag, Long propertyId,
                                                       Long subjectId);

    /**
     * 导出数据库列表
     *
     * @param sid          学校id
     * @param did          数据库
     * @param languageId   语种
     * @param area         所在地区
     * @param natureType   数据库性质
     * @param fulltextFlag 是否全文
     * @param propertyId   资源类型
     * @param subjectId    学科id
     * @return 数据库列表
     * @author majuehao
     * @date 2021/10/18 10:27
     **/
    ResponseEntity<byte[]> exportDatabaseList(Long sid, Long did, Long languageId, String area, String natureType,
                                              Integer fulltextFlag, Long propertyId, Long subjectId);

    /**
     * 删除数据库
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 删除的结果
     * @author majuehao
     * @date 2022/1/13 9:53
     **/
    String deleteDatabase(Long sid, Long did);

    /**
     * 新增数据库
     *
     * @param sid       学校id
     * @param userId    用户id
     * @param did       数据库id
     * @param totalFlag 总库标识
     * @param totalDid  总库id
     * @return 新增的结果
     * @author majuehao
     * @date 2022/1/13 9:53
     **/
    String addDatabaseLevel(Long sid, Long userId, Long did, Integer totalFlag, Long totalDid);

    /**
     * 编辑数据库
     *
     * @param sid                     学校id
     * @param userId                  用户id
     * @param databaseInfoInsertModel 数据库信息详情
     * @return 更新的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    String updateDatabase(Long sid, Long userId, DatabaseInfoInsertModel databaseInfoInsertModel);

    /**
     * 新增或者更新基础信息
     *
     * @param sid    学校id
     * @param did    数据库id
     * @param userId 用户id
     * @param model  用户填写的数据
     * @return 数据库信息
     * @author majuehao
     * @date 2022/1/14 16:50
     **/
    DatabaseBaseInfo addOrUpdateDatabaseBaseInfo(Long sid, Long did, Long userId, DatabaseInfoInsertModel model);

    /**
     * 回显编辑数据库
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 基本信息
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    DatabaseInfoInsertModel findOneTotalDatabaseInfo(Long sid, Long did);

    /**
     * 访问信息 -> 回显编辑访问信息
     *
     * @param sid      学校id
     * @param did      数据库id
     * @param accessId 访问id
     * @return 访问信息
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    AccessUrlInfoModel findOneAccessUrl(Long sid, Long did, Long accessId);

    /**
     * 访问信息 -> 删除访问信息
     *
     * @param sid      学校id
     * @param did      数据库id
     * @param accessId 访问id
     * @return 删除的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    String deleteAccessUrl(Long sid, Long did, Long accessId);

    /**
     * 访问信息 -> 新增访问信息
     *
     * @param sid                        学校id
     * @param userId                     用户id
     * @param databaseAccessUrlInfoModel 访问信息
     * @return 新增的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    String addAccessUrl(Long sid, Long userId, AccessUrlInfoModel databaseAccessUrlInfoModel);

    /**
     * 访问信息 ->  编辑访问信息
     *
     * @param sid                        学校id
     * @param userId                     用户id
     * @param databaseAccessUrlInfoModel 访问信息
     * @return 编辑的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    String updateAccessUrl(Long sid, Long userId, AccessUrlInfoModel databaseAccessUrlInfoModel);

    /**
     * 库商信息 -> 回显编辑联系人
     *
     * @param sid         学校id
     * @param did         数据库id
     * @param contactId   联系人id
     * @param contactType 联系人类型
     * @return 联系人
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    DatabaseContactPeople findOneContactPeople(Long sid, Long did, Long contactId, Integer contactType);

    /**
     * 库商信息 -> 新增联系人
     *
     * @param sid                    学校id
     * @param contactPeopleInfoModel 联系人详情
     * @return 新增的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    String addContactPeople(Long sid, ContactPeopleInfoModel contactPeopleInfoModel);

    /**
     * 库商信息 -> 编辑联系人
     *
     * @param sid                    学校id
     * @param contactPeopleInfoModel 联系人详情
     * @return 编辑的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    String updateContactPeople(Long sid, ContactPeopleInfoModel contactPeopleInfoModel);

    /**
     * 库商信息 -> 删除联系人
     *
     * @param sid         学校id
     * @param did         数据库id
     * @param contactId   联系人id
     * @param contactType 联系人类型
     * @return 删除的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    String deleteContactPeople(Long sid, Long did, Long contactId, Integer contactType);

}
