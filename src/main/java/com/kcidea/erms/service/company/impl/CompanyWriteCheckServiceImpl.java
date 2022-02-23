package com.kcidea.erms.service.company.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.util.DateTimeUtil;
import com.kcidea.erms.dao.company.CompanyWriteCheckDao;
import com.kcidea.erms.dao.company.CompanyWriteDao;
import com.kcidea.erms.dao.database.DatabaseAccessUrlDao;
import com.kcidea.erms.dao.database.DatabaseContactPeopleDao;
import com.kcidea.erms.dao.database.DatabaseLevelDao;
import com.kcidea.erms.dao.user.UserDao;
import com.kcidea.erms.domain.company.CompanyWrite;
import com.kcidea.erms.domain.company.CompanyWriteCheck;
import com.kcidea.erms.domain.database.DatabaseAccessUrl;
import com.kcidea.erms.domain.database.DatabaseContactPeople;
import com.kcidea.erms.domain.database.DatabaseLevel;
import com.kcidea.erms.domain.user.User;
import com.kcidea.erms.enums.database.EnumCheckState;
import com.kcidea.erms.enums.database.EnumDataBaseType;
import com.kcidea.erms.enums.database.EnumTableType;
import com.kcidea.erms.model.company.*;
import com.kcidea.erms.model.database.info.DatabaseInfoInsertModel;
import com.kcidea.erms.model.database.detail.DatabaseInfoModel;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.company.CompanyWriteCheckService;
import com.kcidea.erms.service.database.DataBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/01/14
 **/
@Slf4j
@Service
public class CompanyWriteCheckServiceImpl extends BaseService implements CompanyWriteCheckService {

    @Resource
    private CompanyWriteCheckDao companyWriteCheckDao;

    @Resource
    private UserDao userDao;

    @Resource
    private DatabaseLevelDao databaseLevelDao;

    @Resource
    private DataBaseInfoService dataBaseInfoService;

    @Resource
    private DatabaseContactPeopleDao databaseContactPeopleDao;

    @Resource
    private DatabaseAccessUrlDao databaseAccessUrlDao;

    @Resource
    private CompanyWriteDao companyWriteDao;


    /**
     * 查询数据商填写信息审核列表
     *
     * @param sid       学校id
     * @param did       数据库
     * @param tableType 类型
     * @param state     审核状态
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @return 列表
     * @author majuehao
     * @date 2021/10/18 10:27
     **/
    @Override
    public PageResult<CompanyWriteCheckModel> findCompanyWriteCheckList(Long sid, Long did, Integer tableType,
                                                                        Integer state, Integer pageNum,
                                                                        Integer pageSize) {
        // 校验参数
        super.checkSidDid(sid, did);
        super.checkPageParam(pageNum, pageSize);

        PageResult<CompanyWriteCheckModel> result = new PageResult<>();

        //分页参数
        Page<CompanyWriteCheck> page = new Page<>(pageNum, pageSize);

        //查询列表
        List<CompanyWriteCheck> checkList = companyWriteCheckDao.findListBySidDidTableTypeStatePage(sid, did, tableType
                , state, page);

        //返回的记录
        List<CompanyWriteCheckModel> recordList = this.findCompanyWriteCheckModelList(sid, checkList);

        // 组装数据返回
        return result.success(recordList, page.getTotal());
    }

    /**
     * 将查询出来的数据进行二次处理
     *
     * @param sid       学校id
     * @param checkList 查询出来的数据
     * @return 返回到前台的数据
     * @author huxubin
     * @date 2022/1/14 14:12
     */
    private List<CompanyWriteCheckModel> findCompanyWriteCheckModelList(Long sid, List<CompanyWriteCheck> checkList) {
        //已经查询的用户
        Map<Long, User> userMap = Maps.newHashMap();
        List<CompanyWriteCheckModel> resultList = Lists.newArrayList();
        for (CompanyWriteCheck writeCheck : checkList) {
            //审核状态
            EnumCheckState checkState = EnumCheckState.getCheckState(writeCheck.getState());
            //填写类型
            EnumTableType enumTableType = EnumTableType.getEnumTableType(writeCheck.getTableType());
            //创建时间
            String createTime = DateTimeUtil.localDateTimeToString(writeCheck.getCreatedTime());
            //审核人
            String checkUser = null;
            //审核时间
            String checkTime = null;
            //如果不是待审核，那肯定有审核人和时间，需要进行查询
            if (checkState != EnumCheckState.待审核) {
                Long checkUserId = writeCheck.getCheckUserId();

                if (userMap.containsKey(checkUserId)) {
                    checkUser = userMap.get(checkUserId).getNickName();
                } else {
                    User user = userDao.selectById(checkUserId);
                    if (user != null) {
                        checkUser = user.getNickName();
                        userMap.put(checkUserId, user);
                    }
                }

                checkTime = DateTimeUtil.localDateTimeToString(writeCheck.getCheckTime());
            }

            //数据库名称
            String dataBaseName = super.findDatabaseName(sid, writeCheck.getDId());

            //单个对象数据
            CompanyWriteCheckModel model = new CompanyWriteCheckModel();
            model.create(writeCheck.getId(), writeCheck.getDId(), dataBaseName, enumTableType.getName(), createTime,
                    checkUser, checkTime, checkState.getName(), checkState.getValue(), writeCheck.getRemark());
            resultList.add(model);
        }
        return resultList;
    }

    /**
     * 审核数据商填写信息
     *
     * @param sid    学校id
     * @param userId 用户id
     * @param id     填写信息的id
     * @param state  审核状态
     * @param remark 审核说明
     * @return 修改的结果
     * @author majuehao
     * @date 2021/12/21 9:49
     **/
    @Override
    public String updateCompanyWriteCheck(Long sid, Long userId, Long id, Integer state, String remark) {

        super.checkSidUserId(sid, userId);

        LocalDateTime now = LocalDateTime.now();

        // 校验审核状态
        CompanyWriteCheck companyWriteCheck = companyWriteCheckDao.findOneBySidId(sid, id);
        if (companyWriteCheck == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }
        super.checkCheckState(companyWriteCheck.getState(), state);

        // 表格类型
        Integer tableType = companyWriteCheck.getTableType();
        // 数据库id
        Long did = companyWriteCheck.getDId();

        CompanyWrite companyWrite = companyWriteDao.findOneBySidDidTableType(sid, did, tableType);
        if (companyWrite == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        if (state.equals(EnumCheckState.审核通过.getValue())) {
            // 审核通过，进行数据存储
            companyWriteInfoAddSchoolDatabase(sid, did, id, tableType, userId, now);
        }

        // 更新审核状态
        companyWriteCheck.saveCheckResult(userId, now, state, remark, userId, now);
        companyWriteCheckDao.updateById(companyWriteCheck);

        // 更新数据商填写表状态
        companyWrite.update(state, companyWriteCheck.getId(), userId, now);
        companyWriteDao.updateById(companyWrite);

        return Vm.UPDATE_SUCCESS;
    }

    /**
     * 数据存储
     *
     * @param sid       学校id
     * @param did       数据库id
     * @param id        审核id
     * @param tableType 表格类型
     * @param userId    用户id
     * @param now       时间
     * @author majuehao
     * @date 2022/1/19 17:01
     **/
    private void companyWriteInfoAddSchoolDatabase(Long sid, Long did, Long id, Integer tableType, Long userId,
                                                   LocalDateTime now) {
        EnumTableType enumTableType = EnumTableType.getEnumTableType(tableType);
        switch (enumTableType) {
            case 基本信息: {
                DataBaseInfoCheckModel dataBaseInfoCheckModel = this.findBaseInfoCheck(sid, id);
                this.addDatabaseInfo(sid, userId, now, dataBaseInfoCheckModel);
                break;
            }
            case 库商信息: {
                ContactPeopleCheckModel contactPeopleCheck = this.findContactPeopleCheck(sid, id);
                this.addContactPeople(sid, did, userId, now, contactPeopleCheck, Constant.CompanyWrite.COMPANY_CONTACT);
                this.addContactPeople(sid, did, userId, now, contactPeopleCheck, Constant.CompanyWrite.AGENT_CONTACT);
                break;
            }
            case 访问信息: {
                AccessUrlCheckModel accessUrlCheck = this.findAccessUrlCheck(sid, id);
                this.addAccessUrl(sid, userId, now, accessUrlCheck);
                break;
            }
            default:
                throw new CustomException("暂不支持的数据类型");
        }
    }

    /**
     * 审核通过 -》 添加总子库数据库信息
     *
     * @param sid                    学校id
     * @param userId                 用户id
     * @param now                    时间
     * @param dataBaseInfoCheckModel 总子库信息
     * @author majuehao
     * @date 2022/1/19 11:18
     **/
    private void addDatabaseInfo(Long sid, Long userId, LocalDateTime now, DataBaseInfoCheckModel dataBaseInfoCheckModel) {

        // 总库信息
        TotalDatabaseInfoModel totalDatabaseInfo = dataBaseInfoCheckModel.getTotalInfo();

        // 子库信息列表
        List<TotalDatabaseInfoModel> sonDatabaseInfoList = dataBaseInfoCheckModel.getChildInfo();

        if (totalDatabaseInfo == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        // 总库id
        Long totalDid = totalDatabaseInfo.getDid();

        // 子库id
        Set<Long> sonDidSet = sonDatabaseInfoList.stream().map(TotalDatabaseInfoModel::getDid)
                .collect(Collectors.toSet());

        // 添加层级信息,校验是否真的审核通过
        this.addDatabaseLevelAndVerify(sid, totalDid, sonDidSet, userId, now);

        // 循环新增信息
        sonDatabaseInfoList.add(totalDatabaseInfo);
        for (TotalDatabaseInfoModel totalDatabaseInfoModel : sonDatabaseInfoList) {
            // 数据库信息
            DatabaseInfoInsertModel databaseInfoInsertModel = new DatabaseInfoInsertModel();

            // 复制属性：地区、全文、数据时间、供应商、代理商、学科、数据库性质、资源总量、容量、更新频率、检索功能、并发、简介、使用指南
            BeanUtils.copyProperties(totalDatabaseInfoModel, databaseInfoInsertModel);
            // 别名、语种、纸电、类型
            databaseInfoInsertModel.setDatabaseInfoModel(new DatabaseInfoModel().create(totalDatabaseInfoModel.getDid(),
                    totalDatabaseInfoModel.getName(), totalDatabaseInfoModel.getLanguage(),
                    totalDatabaseInfoModel.getPaperFlag(), totalDatabaseInfoModel.getTypeList()));

            // 更新数据库
            dataBaseInfoService.updateDatabase(sid, userId, databaseInfoInsertModel);
        }
    }


    /**
     * 添加层级信息,校验是否真的审核通过
     *
     * @param sid       学校id
     * @param totalDid  总库id
     * @param sonDidSet 子库id集合
     * @param userId    用户id
     * @param now       时间
     * @author majuehao
     * @date 2022/1/18 17:17
     **/
    @Override
    public void addDatabaseLevelAndVerify(Long sid, Long totalDid, Set<Long> sonDidSet, Long userId, LocalDateTime now) {
        List<DatabaseLevel> levels = databaseLevelDao.findListBySid(sid);
        if (levels == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        // 将数据收束成did->databaseLevel，totalDid->sonDidSet
        // 数据库层级map
        Map<Long, DatabaseLevel> didLevelMap = levels.stream().collect(Collectors.toMap(DatabaseLevel::getDId, x -> x));
        // 总库对应的子库map
        Map<Long, Set<Long>> totalDidSonDidSetMap = levels.stream().collect(Collectors.groupingBy(DatabaseLevel::getTotalDid
                , Collectors.mapping(DatabaseLevel::getDId, Collectors.toSet())));

        // 判断总库能否导入
        checkTotalDatabaseLevel(didLevelMap, totalDid, totalDidSonDidSetMap);

        // 循环子库判断
        for (Long sonDid : sonDidSet) {
            // 判断子库能否导入
            checkSonDatabaseLevel(sid, sonDid, totalDidSonDidSetMap, didLevelMap, totalDid, userId);
        }
    }

    /**
     * 判断总库能否导入
     *
     * @param didLevelMap          数据库层级map
     * @param totalDid             总库id
     * @param totalDidSonDidSetMap 总库对应的子库map
     * @author majuehao
     * @date 2022/1/25 15:17
     **/
    @Override
    public void checkTotalDatabaseLevel(Map<Long, DatabaseLevel> didLevelMap, Long totalDid,
                                        Map<Long, Set<Long>> totalDidSonDidSetMap) {

        Set<Long> oldSonDidSet = totalDidSonDidSetMap.get(totalDid);
        if (didLevelMap.get(totalDid) == null) {
            throw new CustomException("检测到您审核的数据库已删除，请先添加此数据库");
        }

        if (oldSonDidSet == null) {
            throw new CustomException("检测到您审核的数据库已调整为子库，无法通过审核");
        }

    }

    /**
     * 判断子库能否导入
     *
     * @param sid                  学校id
     * @param sonDid               子库id
     * @param totalDidSonDidSetMap 总库对应的子库map
     * @param didLevelMap          数据库层级map
     * @param totalDid             总库id
     * @param userId               用户id
     * @author majuehao
     * @date 2022/1/25 15:13
     **/
    @Override
    public void checkSonDatabaseLevel(Long sid, Long sonDid, Map<Long, Set<Long>> totalDidSonDidSetMap,
                                      Map<Long, DatabaseLevel> didLevelMap,
                                      Long totalDid, Long userId) {
        Set<Long> oldSonDidSet = totalDidSonDidSetMap.get(totalDid);
        // 子库名字
        String sonName = super.findDatabaseName(sid, sonDid);
        // 总库名字
        String totalName;

        // 该子库已存在，无需添加
        if (oldSonDidSet.contains(sonDid)) {
            return;
        }

        // 查询该子库原有的子库列表
        Set<Long> grandSonDidSet = totalDidSonDidSetMap.get(sonDid);
        // 查询该子库原来的层级
        DatabaseLevel sonDatabaseLevel = didLevelMap.get(sonDid);
        // 该子库原来为子库,不能修改
        if (grandSonDidSet == null && sonDatabaseLevel != null) {
            totalName = super.findDatabaseName(sid, sonDatabaseLevel.getTotalDid());
            // 提示子库原来是哪个总库的子库，所以不能修改
            String errMsg = "检测到子库:".concat(sonName).concat("已经是总库:").concat(totalName)
                    .concat("的子库了，无法通过审核");
            throw new CustomException(errMsg);
        }
        // 该子库为新数据库，直接新增
        if (grandSonDidSet == null && didLevelMap.get(sonDid) == null) {
            // 直接新增
            sonDatabaseLevel = new DatabaseLevel()
                    .create(sid, sonDid, EnumDataBaseType.子库.getValue(), totalDid, null,
                            userId, LocalDateTime.now());
            databaseLevelDao.insert(sonDatabaseLevel);
        }

        // 该子库原来为总库
        if (grandSonDidSet != null) {
            // 该子库还有子库,不能修改
            if (grandSonDidSet.size() > 1) {
                // 提示该子库原来是总库，且还有子库
                String errMsg = "检测到子库:".concat(sonName).concat("是总库且包含子库列表，请检查数据的准确性");
                throw new CustomException(errMsg);
            } else {
                // 更新
                sonDatabaseLevel.setTotalFlag(EnumDataBaseType.子库.getValue())
                        .setTotalDid(totalDid).setUpdatedBy(userId).setUpdatedTime(LocalDateTime.now());
                databaseLevelDao.updateById(sonDatabaseLevel);
            }
        }
    }

    /**
     * 添加联系人信息
     *
     * @param sid                学校id
     * @param did                数据库id
     * @param userId             用户id
     * @param now                时间
     * @param contactPeopleCheck 联系人数据
     * @param contactType        联系人类型
     * @author majuehao
     * @date 2022/1/19 13:19
     **/
    private void addContactPeople(Long sid, Long did, Long userId, LocalDateTime now,
                                  ContactPeopleCheckModel contactPeopleCheck, Integer contactType) {
        if (contactPeopleCheck == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        // 库商填写的联系人列表
        List<ContactPeopleInfoModel> companyContactList;
        if (contactType.equals(Constant.CompanyWrite.COMPANY_CONTACT)) {
            // 供应商联系人
            companyContactList = contactPeopleCheck.getCompanyPeopleList();
        } else {
            // 代理商联系人
            companyContactList = contactPeopleCheck.getAgentPeopleList();
        }

        // 查询学校数据库现有联系人，收束成集合
        Map<String, ContactPeopleInfoModel> schoolContactMap = Maps.newHashMap();
        List<ContactPeopleInfoModel> schoolContactList =
                databaseContactPeopleDao.findListBySidDidType(sid, did, contactType);

        if (!CollectionUtils.isEmpty(schoolContactList)) {
            schoolContactMap = schoolContactList.stream()
                    .collect(Collectors.toMap(ContactPeopleInfoModel::getPhone, x -> x));
        }

        // 循环库商填写的联系人列表
        for (ContactPeopleInfoModel contact : companyContactList) {
            // 联系人数据
            DatabaseContactPeople insertModel = new DatabaseContactPeople()
                    .create(sid, did, contact.getName(), contact.getPosition(), contact.getPhone(),
                            contact.getTelephone(), contact.getEmail(), contact.getQq(), contact.getWechat(),
                            contactType, contact.getAddress(), contact.getRemark(), null,
                            null);

            // 学校的联系人列表中，是否包含此联系人
            ContactPeopleInfoModel schoolContact = schoolContactMap.get(insertModel.getPhone());
            if (schoolContact == null) {
                // 不包含，就直接新增
                insertModel.setCreatedBy(userId).setCreatedTime(now);
                databaseContactPeopleDao.insert(insertModel);
            } else {
                // 包含，就更新原有数据
                insertModel.setId(schoolContact.getId()).setUpdatedBy(userId).setUpdatedTime(now);
                databaseContactPeopleDao.updateById(insertModel);
            }
        }
    }

    /**
     * 添加总子库访问信息
     *
     * @param sid            学校id
     * @param userId         用户id
     * @param now            时间
     * @param accessUrlCheck 总子库访问信息
     * @author majuehao
     * @date 2022/1/19 16:07
     **/
    private void addAccessUrl(Long sid, Long userId, LocalDateTime now, AccessUrlCheckModel accessUrlCheck) {
        if (null == accessUrlCheck) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        // 库商填写的访问链接列表
        List<AccessUrlInfoModel> companyAccessUrlList = Lists.newArrayList();

        // 总库的访问信息
        List<AccessUrlInfoModel> totalUrlList = accessUrlCheck.getTotalUrl();
        // 子库的访问信息
        List<AccessUrlInfoModel> childUrlList = accessUrlCheck.getChildUrlList();

        companyAccessUrlList.addAll(totalUrlList);
        companyAccessUrlList.addAll(childUrlList);

        // 库商填的访问链接，根据did分组  did->list
        Map<Long, List<AccessUrlInfoModel>> companyAccessUrlGroupByDid = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(companyAccessUrlList)) {
            companyAccessUrlGroupByDid = companyAccessUrlList.stream()
                    .collect(Collectors.groupingBy(AccessUrlInfoModel::getDId, Collectors.mapping(a -> a, Collectors.toList())));
        }

        // 学校的访问链接，根据did分组  did->list
        Map<Long, List<DatabaseAccessUrl>> schoolAccessUrlMapGroupByDid = findSchoolAccessUrlMap(sid);

        // 根据分组的map，一个一个数据库的添加访问链接信息
        for (List<AccessUrlInfoModel> databaseUrlList : companyAccessUrlGroupByDid.values()) {

            // 如果该库没有链接
            if (!CollectionUtils.isEmpty(databaseUrlList)) {
                // 根据did查询链接信息，根据url收束成map：url->链接信息
                Map<String, DatabaseAccessUrl> schoolUrlsAccessMap = Maps.newHashMap();
                List<DatabaseAccessUrl> schoolUrlList = schoolAccessUrlMapGroupByDid.get(databaseUrlList.get(0).getDId());
                if (!CollectionUtils.isEmpty(schoolUrlList)) {
                    schoolUrlsAccessMap = schoolUrlList.stream()
                            .collect(Collectors.toMap(DatabaseAccessUrl::getUrl, x -> x));
                }

                // 循环此数据库的访问信息列表
                for (AccessUrlInfoModel companyAccess : databaseUrlList) {
                    DatabaseAccessUrl insertAccessUrl = new DatabaseAccessUrl().create(
                            null, sid, companyAccess.getDId(), companyAccess.getAccessType(), companyAccess.getLoginType()
                            , companyAccess.getUrl(), companyAccess.getAccessTips(), companyAccess.getRemark(), null
                            , null, null, null);

                    // 学校的访问链接
                    DatabaseAccessUrl schoolAccess = schoolUrlsAccessMap.get(companyAccess.getUrl());
                    if (schoolAccess == null) {
                        // 如果学校原来不包含这条链接，那就直接新增
                        insertAccessUrl.setCreatedBy(userId).setCreatedTime(now);
                        databaseAccessUrlDao.insert(insertAccessUrl);
                    } else {
                        // 不包含，那就更新
                        insertAccessUrl.setId(schoolAccess.getId()).setUpdatedBy(userId).setUpdatedTime(now);
                        databaseAccessUrlDao.updateById(schoolAccess);
                    }
                }
            }
        }
    }

    /**
     * 根据学校查询访问链接，按did分组
     *
     * @param sid 学校id
     * @return 访问链接
     * @author majuehao
     * @date 2022/1/24 16:28
     **/
    @Override
    public Map<Long, List<DatabaseAccessUrl>> findSchoolAccessUrlMap(Long sid) {
        Map<Long, List<DatabaseAccessUrl>> schoolAccessUrlMapGroupByDid = Maps.newHashMap();
        List<DatabaseAccessUrl> schoolAccessUrlList = databaseAccessUrlDao.findListBySid(sid);
        if (schoolAccessUrlList != null) {
            schoolAccessUrlMapGroupByDid = schoolAccessUrlList.stream().collect(Collectors.groupingBy(
                    DatabaseAccessUrl::getDId, Collectors.mapping(x -> x, Collectors.toList())));
        }
        return schoolAccessUrlMapGroupByDid;
    }


    /**
     * 数据商填写-》访问信息-》审核回显
     *
     * @param sid 学校id
     * @param id  审核记录的id
     * @return 数据商填写-》访问信息-》审核回显
     * @author huxubin
     * @date 2022/1/18 11:06
     */
    @Override
    public AccessUrlCheckModel findAccessUrlCheck(Long sid, Long id) {

        CompanyWriteCheck companyWriteCheck = companyWriteCheckDao.findOneBySidId(sid, id);
        if (companyWriteCheck == null) {
            throw new CustomException(Vm.DATA_NOT_EXIST);
        }

        //将JSON数据转换成需要的对象
        return JSON.parseObject(companyWriteCheck.getJson(), AccessUrlCheckModel.class);
    }

    /**
     * 查询数据商填写-》库商信息(联系人)-》审核回显
     *
     * @param sid 学校id
     * @param id  审核记录的id
     * @return 数据商填写-》库商信息(联系人)-》审核回显
     * @author huxubin
     * @date 2022/1/18 14:39
     */
    @Override
    public ContactPeopleCheckModel findContactPeopleCheck(Long sid, Long id) {

        CompanyWriteCheck companyWriteCheck = companyWriteCheckDao.findOneBySidId(sid, id);
        if (companyWriteCheck == null) {
            throw new CustomException(Vm.DATA_NOT_EXIST);
        }

        //将JSON数据转换成需要的对象
        return JSON.parseObject(companyWriteCheck.getJson(), ContactPeopleCheckModel.class);
    }

    /**
     * 查询数据商填写-》基本信息-》审核回显
     *
     * @param sid 学校id
     * @param id  审核记录的id
     * @return 数据商填写-》基本信息-》审核回显
     * @author huxubin
     * @date 2022/1/18 15:47
     */
    @Override
    public DataBaseInfoCheckModel findBaseInfoCheck(Long sid, Long id) {
        CompanyWriteCheck companyWriteCheck = companyWriteCheckDao.findOneBySidId(sid, id);
        if (companyWriteCheck == null) {
            throw new CustomException(Vm.DATA_NOT_EXIST);
        }

        //将JSON数据转换成需要的对象
        return JSON.parseObject(companyWriteCheck.getJson(), DataBaseInfoCheckModel.class);
    }

    /**
     * 根据学校id和审核查询审核的状态
     *
     * @param sid 学校id
     * @param id  审核id
     * @return 审核的状态
     * @author huxubin
     * @date 2022/1/19 9:38
     */
    @Override
    public Integer findCompanyWriteState(Long sid, Long id) {

        CompanyWriteCheck companyWriteCheck = companyWriteCheckDao.findOneBySidId(sid, id);
        if (companyWriteCheck == null) {
            throw new CustomException(Vm.DATA_NOT_EXIST);
        }

        return companyWriteCheck.getState();
    }

}
