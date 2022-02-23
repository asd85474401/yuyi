package com.kcidea.erms.service.company;

import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.domain.company.CompanyDatabaseContactPeople;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.company.AccessUrlInfoModel;
import com.kcidea.erms.model.company.ContactPeopleInfoModel;
import com.kcidea.erms.model.company.TableCheckState;
import com.kcidea.erms.model.company.TotalDatabaseInfoModel;
import com.kcidea.erms.model.database.AttachmentDontCheckModel;
import com.kcidea.erms.model.database.detail.DataBaseTitleDetailModel;
import com.kcidea.erms.model.database.detail.DatabaseInfoModel;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/01/05
 **/
public interface CompanyWriteService {

    /**
     * 基本信息 -> 填写基本信息
     *
     * @param totalDatabaseInfoModel 基本信息详情
     * @return 添加或更新的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    String addOrUpdateDatabaseInfo(TotalDatabaseInfoModel totalDatabaseInfoModel);

    /**
     * 基本信息 -> 回显编辑基本信息
     *
     * @param apiKey apiKey
     * @return 基本信息
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    TotalDatabaseInfoModel findOneTotalDatabaseInfo(String apiKey);

    /**
     * 库商信息 -> 联系人列表
     *
     * @param apiKey      apiKey
     * @param contactType 联系人类型
     * @return 联系人列表
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    List<ContactPeopleInfoModel> findDatabaseContactPeople(String apiKey, int contactType);


    /**
     * 库商信息 -> 回显编辑联系人
     *
     * @param apiKey      apiKey
     * @param contactId   联系人id
     * @param contactType 联系人类型
     * @return 联系人信息
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    CompanyDatabaseContactPeople findOneDatabaseContactPeople(String apiKey, Long contactId, int contactType);

    /**
     * 库商信息 -> 新增或编辑联系人
     *
     * @param contactPeopleInfoModel 联系人详情
     * @param contactType            联系人类型
     * @return 编辑的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    String addOrUpdateContactPeople(ContactPeopleInfoModel contactPeopleInfoModel, int contactType);

    /**
     * 查询供应商下拉
     *
     * @param apiKey apiKey
     * @return 供应商集合
     * @author majuehao
     * @date 2022/1/6 14:35
     **/
    List<String> findCompanySelectList(String apiKey);

    /**
     * 查询代理商下拉
     *
     * @param apiKey apiKey
     * @return 代理商集合
     * @author majuehao
     * @date 2022/1/6 14:35
     **/
    List<String> findAgentSelectList(String apiKey);

    /**
     * 库商信息 -> 删除联系人
     *
     * @param apiKey      apiKey
     * @param contactId   联系人id
     * @param contactType 联系人类型
     * @return 删除的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    String deleteContactPeople(String apiKey, Long contactId, int contactType);

    /**
     * 访问信息 -> 总库访问信息列表
     *
     * @param apiKey apiKey
     * @return 访问信息列表
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    List<AccessUrlInfoModel> findAccessUrlList(String apiKey);

    /**
     * 访问信息 -> 回显编辑总库访问信息
     *
     * @param apiKey   apiKey
     * @param accessId 访问id
     * @return 访问信息
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    AccessUrlInfoModel findOneAccessUrl(String apiKey, Long accessId);

    /**
     * 访问信息 -> 新增或编辑访问信息
     *
     * @param accessUrlInfoModel 访问信息
     * @return 新增或编辑的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    String addOrUpdateAccessUrl(AccessUrlInfoModel accessUrlInfoModel);

    /**
     * 访问信息 -> 删除访问信息
     *
     * @param apiKey   apiKey
     * @param accessId 访问id
     * @return 删除的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    String deleteAccessUrl(String apiKey, Long accessId);

    /**
     * 访问信息 -> 子库访问信息列表
     *
     * @param apiKey apiKey
     * @param did    数据库id
     * @return 访问信息列表
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    List<AccessUrlInfoModel> findSonAccessUrlList(String apiKey, Long did);

    /**
     * 获取子库的apiKey
     *
     * @param apiKey apiKey
     * @param did    子库id
     * @return 子库的apiKey
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    String findSonApiKey(String apiKey, Long did);

    /**
     * 获取子库的apiKey列表
     *
     * @param apiKey apiKey
     * @return 子库的apiKey列表
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    List<IdNameModel> findSonApiKeyList(String apiKey);

    /**
     * 查询学校自定义和系统默认的数据库集合
     *
     * @param apiKey     apiKey
     * @param name       数据库名称
     * @param letter     首字母
     * @param propertyId 数据库属性
     * @param languageId 数据库语种
     * @param pageNum    页码
     * @param pageSize   每页数量
     * @return 数据库集合
     * @author yeweiwei
     * @date 2021/11/25 16:16
     */
    PageResult<IdNameModel> findDatabaseSelectList(String apiKey, String name, String letter, Long propertyId, Long languageId, Integer pageNum, Integer pageSize);

    /**
     * 联想查询数据库
     *
     * @param apiKey apiKey
     * @param search 检索条件
     * @return 数据库集合
     * @author majuehao
     * @date 2021/11/26 9:29
     **/
    List<IdNameModel> findDatabaseListBySearch(String apiKey, String search);

    /**
     * 新增数据库信息
     *
     * @param apiKey       apiKey
     * @param databaseInfo 数据库信息
     * @return 新增的结果
     * @author yeweiwei
     * @date 2021/11/25 10:59
     */
    IdNameModel addDatabase(String apiKey, DatabaseInfoModel databaseInfo);

    /**
     * 基本信息 -> 子库列表
     *
     * @param apiKey apiKey
     * @param did    子库id
     * @return 访问信息列表
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    List<DataBaseTitleDetailModel> findSonDatabaseList(String apiKey, Long did);

    /**
     * 基本信息 -> 删除基本信息
     *
     * @param apiKey   apiKey
     * @param totalDid 总库id
     * @return 删除的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    String deleteDatabaseInfo(String apiKey, Long totalDid);

    /**
     * 获取标签的审核状态
     *
     * @param apiKey    apiKey
     * @param tableType 标签类型
     * @return 新增的结果
     * @author yeweiwei
     * @date 2021/11/25 10:59
     */
    TableCheckState findCheckState(String apiKey, Integer tableType);

    /**
     * 提交审核
     *
     * @param apiKey    apiKey
     * @param tableType 标签类型
     * @return 更新结果
     * @author majuehao
     * @date 2021/11/25 10:59
     */
    String updateCheckState(String apiKey, Integer tableType);

    /**
     * 获取数据库名称
     *
     * @param apiKey apiKey
     * @return 数据库名称
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    IdNameModel findDatabaseIdName(String apiKey);

    /**
     * 返回修改
     *
     * @param apiKey    apiKey
     * @param tableType 标签类型
     * @return 更新结果
     * @author majuehao
     * @date 2021/11/25 10:59
     */
    String updateCheckStateReturnModify(String apiKey, Integer tableType);

    /**
     * 更新库商填写附件表
     *
     * @param sid        学校id
     * @param uniqueId   表id
     * @param type       附件类型
     * @param attachList 要更新的附件列表
     * @author majuehao
     * @date 2022/1/14 13:04
     **/
    void updateAttachment(Long sid, Long uniqueId, Integer type, List<AttachmentDontCheckModel> attachList);
}
