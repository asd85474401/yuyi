package com.kcidea.erms.service.company.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.util.BeanListCopyUtils;
import com.kcidea.erms.common.util.ConvertUtil;
import com.kcidea.erms.common.util.DateTimeUtil;
import com.kcidea.erms.common.util.MinioFileUtil;
import com.kcidea.erms.dao.company.*;
import com.kcidea.erms.dao.ers.AgentDao;
import com.kcidea.erms.dao.ers.CompanyDao;
import com.kcidea.erms.dao.ers.VdatabasePropertyDao;
import com.kcidea.erms.domain.company.*;
import com.kcidea.erms.domain.database.DatabaseKey;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import com.kcidea.erms.enums.database.*;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.company.*;
import com.kcidea.erms.model.database.AttachmentDontCheckModel;
import com.kcidea.erms.model.database.detail.DataBaseTitleDetailModel;
import com.kcidea.erms.model.database.detail.DatabaseInfoModel;
import com.kcidea.erms.model.subject.SubjectAndCategoryModel;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.company.CompanyWriteService;
import com.kcidea.erms.service.ers.DatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/01/05
 **/
@Slf4j
@Service
public class CompanyWriteServiceImpl extends BaseService implements CompanyWriteService {

    @Resource
    private CompanyDatabaseBaseInfoDao companyDatabaseBaseInfoDao;

    @Resource
    private CompanyWriteDao companyWriteDao;

    @Resource
    private CompanyDatabaseContactPeopleDao companyDatabaseContactPeopleDao;

    @Resource
    private CompanyDao companyDao;

    @Resource
    private AgentDao agentDao;

    @Resource
    private VdatabasePropertyDao vdatabasePropertyDao;

    @Resource
    private CompanyDatabaseAttachmentDao companyDatabaseAttachmentDao;

    @Resource
    private CompanyDatabaseAccessUrlDao companyDatabaseAccessUrlDao;

    @Resource
    private CompanyDatabaseLevelDao companyDatabaseLevelDao;

    @Resource
    private DatabaseService databaseService;

    @Resource
    private CompanyWriteCheckDao companyWriteCheckDao;

    /**
     * 基本信息 -> 填写基本信息
     *
     * @param totalDatabaseInfoModel 基本信息详情
     * @return 添加或更新的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String addOrUpdateDatabaseInfo(TotalDatabaseInfoModel totalDatabaseInfoModel) {
        if (null == totalDatabaseInfoModel) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(totalDatabaseInfoModel.getApiKey());
        Long sid = databaseKey.getSId();
        Long did = databaseKey.getDId();
        // 是否暂存
        Integer tempSaveFlag = totalDatabaseInfoModel.getTempSaveFlag();

        if (null == tempSaveFlag || tempSaveFlag == EnumTrueFalse.否.getValue()) {
            // 对参数进行空校验
            this.checkData(totalDatabaseInfoModel, true);
        }

        // 校验添加的子库是否合法
        this.checkAddSonIsLegal(sid, did, totalDatabaseInfoModel.getTotalDid());

        // 总库的totalId就是自己
        if (totalDatabaseInfoModel.getTotalDid() == null) {
            totalDatabaseInfoModel.setTotalDid(did);
        }

        // 查询审核状态
        Integer state = this.findCheckState(totalDatabaseInfoModel.getApiKey(), EnumTableType.基本信息.getValue())
                .getState();
        if (state != null && state == EnumCheckState.待审核.getValue()) {
            throw new CustomException(Vm.CHECKING_NOT_UPDATE);
        }

        // 查询原有数据
        CompanyDatabaseBaseInfo info = companyDatabaseBaseInfoDao.findOneBySidDid(sid, did);

        // 如果查询不到则为新增
        if (info == null) {
            // 声明对象并复制属性
            info = new CompanyDatabaseBaseInfo();
            BeanUtils.copyProperties(totalDatabaseInfoModel, info);

            // 设置学校id、数据库id
            info.setSId(sid);
            info.setDId(did);

            // list转String并以逗号分割
            info.setTypeList(StringUtils.join(totalDatabaseInfoModel.getTypeList(), Constant.SplitChar.COMMA_CHAR));
            info.setSubjectList(StringUtils.join(totalDatabaseInfoModel.getSubjectList(),
                    Constant.SplitChar.COMMA_CHAR));

            // 设置创建时间
            info.setCreatedTime(LocalDateTime.now());

            // 新增数据
            companyDatabaseBaseInfoDao.insert(info);

            // 新增附件表
            this.updateAttachment(sid, info.getId(), EnumAttachmentType.使用指南.getValue(),
                    totalDatabaseInfoModel.getAttachList());

            // 新增数据库层级关系
            this.addDatabaseLevel(sid, did, totalDatabaseInfoModel.getTotalDid());

            return Vm.INSERT_SUCCESS;
        } else {
            // 如果不为空则为编辑
            // 数据库详情复制属性，覆盖原有
            CompanyDatabaseBaseInfo companyDatabaseBaseInfo = new CompanyDatabaseBaseInfo();
            BeanUtils.copyProperties(totalDatabaseInfoModel, info);
            BeanUtils.copyProperties(info, companyDatabaseBaseInfo);

            // list转String并以逗号分割
            companyDatabaseBaseInfo.setTypeList(StringUtils.join(totalDatabaseInfoModel.getTypeList(),
                    Constant.SplitChar.COMMA_CHAR));
            companyDatabaseBaseInfo.setSubjectList(StringUtils.join(totalDatabaseInfoModel.getSubjectList(),
                    Constant.SplitChar.COMMA_CHAR));

            // 更新时间
            companyDatabaseBaseInfo.setUpdatedTime(LocalDateTime.now());

            // 更新数据
            companyDatabaseBaseInfoDao.updateById(companyDatabaseBaseInfo);

            // 更新附件
            this.updateAttachment(sid, info.getId(), EnumAttachmentType.使用指南.getValue(),
                    totalDatabaseInfoModel.getAttachList());

            // 新增数据库层级关系
            this.addDatabaseLevel(sid, did, totalDatabaseInfoModel.getTotalDid());

            return Vm.UPDATE_SUCCESS;
        }
    }

    /**
     * 数据库基本信息的空校验
     *
     * @param totalDatabaseInfoModel 参数信息
     * @author majuehao
     * @date 2022/2/7 10:33
     **/
    private void checkData(TotalDatabaseInfoModel totalDatabaseInfoModel, boolean checkSubjectAndTypeFlag) {
        if (totalDatabaseInfoModel.getLanguage() == null) {
            throw new CustomException("请选择数据库语种");
        }
        if (totalDatabaseInfoModel.getPaperFlag() == null) {
            throw new CustomException("请选择数据库纸电种类");
        }
        if (Strings.isNullOrEmpty(totalDatabaseInfoModel.getName())) {
            throw new CustomException("请输入数据库名称");
        }
        if (Strings.isNullOrEmpty(totalDatabaseInfoModel.getArea())) {
            throw new CustomException("请输入地区");
        }
        if (null == totalDatabaseInfoModel.getFulltextFlag()) {
            throw new CustomException("请选择数据库全文种类");
        }
        if (Strings.isNullOrEmpty(totalDatabaseInfoModel.getNatureType())) {
            throw new CustomException("请选择数据库性质");
        }
        if (checkSubjectAndTypeFlag) {
            if (CollectionUtils.isEmpty(totalDatabaseInfoModel.getSubjectList())) {
                throw new CustomException("请至少选择一个数据库学科覆盖");
            }
            if (CollectionUtils.isEmpty(totalDatabaseInfoModel.getTypeList())) {
                throw new CustomException("请选择数据库资源类型");
            }
        }
        if (Strings.isNullOrEmpty(totalDatabaseInfoModel.getDataTime())) {
            throw new CustomException("请输入数据库数据时间");
        }
        if (Strings.isNullOrEmpty(totalDatabaseInfoModel.getCompanyName())) {
            throw new CustomException("请选择数据库供应商");
        }
        if (Strings.isNullOrEmpty(totalDatabaseInfoModel.getTotalResource())) {
            throw new CustomException("请输入数据库资源总量");
        }
        if (Strings.isNullOrEmpty(totalDatabaseInfoModel.getUpdateFrequency())) {
            throw new CustomException("请输入数据库更新频率");
        }
        if (Strings.isNullOrEmpty(totalDatabaseInfoModel.getSearch())) {
            throw new CustomException("请输入数据库检索功能");
        }
        if (Strings.isNullOrEmpty(totalDatabaseInfoModel.getIntroduce())) {
            throw new CustomException("请输入数据库内容简介");
        }
    }

    /**
     * 校验添加的子库是否合法
     *
     * @param sid      学校id
     * @param did      数据库id
     * @param totalDid 总库id
     * @author majuehao
     * @date 2022/1/11 16:03
     **/
    private void checkAddSonIsLegal(Long sid, Long did, Long totalDid) {
        // totalDid为null，表示此时是添加总库，不需要进行校验
        if (totalDid == null) {
            return;
        }

        if (did.equals(totalDid)) {
            throw new CustomException("不允许添加总库本身作为子库！");
        }

        Set<Long> sonDidSet = companyDatabaseLevelDao.findSonDidListBySidDid(sid, totalDid);

        if (sonDidSet.contains(did)) {
            throw new CustomException("该子库已经添加");
        }
    }

    /**
     * 新增数据库层级关系
     *
     * @param sid      学校id
     * @param did      数据库id
     * @param totalDid 总库id
     * @author majuehao
     * @date 2022/1/11 10:02
     **/
    private void addDatabaseLevel(Long sid, Long did, Long totalDid) {
        CompanyDatabaseLevel companyDatabaseLevel;

        int totalFlag = EnumDataBaseType.子库.getValue();
        // totalId等于did说明是总库
        if (totalDid.equals(did)) {
            totalFlag = EnumDataBaseType.总库.getValue();
        }

        // 如果找不到一样的数据，再新增
        if (companyDatabaseLevelDao.findCountBySidDidTotalFlagTotalDid(sid, did, totalFlag, totalDid) == 0) {
            companyDatabaseLevel = new CompanyDatabaseLevel().create(sid, did, totalFlag, totalDid, LocalDateTime.now());
            // 新增层级关系
            companyDatabaseLevelDao.insert(companyDatabaseLevel);
        }
    }

    /**
     * 基本信息 -> 回显编辑基本信息
     *
     * @param apiKey apiKey
     * @return 基本信息
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @Override
    public TotalDatabaseInfoModel findOneTotalDatabaseInfo(String apiKey) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        Long sid = databaseKey.getSId();
        Long did = databaseKey.getDId();

        // 返回集合
        TotalDatabaseInfoModel model = new TotalDatabaseInfoModel();
        model.setSubjectList(Lists.newArrayList());
        model.setAttachList(Lists.newArrayList());
        model.setTypeList(Lists.newArrayList());

        // 查询数据
        CompanyDatabaseBaseInfo info = companyDatabaseBaseInfoDao.findOneBySidDid(sid, did);

        // 都找不到数据说明是首次进入
        if (info == null) {
            // 数据库id
            model.setDid(did);

            // 查询底层的配置，回显：数据库名称、语种、纸电、资源类型
            // 查询数据库名称
            DatabaseInfoModel infoModel = databaseService.findDatabaseInfo(sid, did);
            model.setName(infoModel.getName());

            // 资源类型
            model.setTypeList(infoModel.getPropertyIdList());

            // 语种
            model.setLanguage(infoModel.getLanguageId());

            // 纸电
            model.setPaperFlag(infoModel.getPaperFlag());
        } else {
            // 都不为空，返回回显数据
            BeanUtils.copyProperties(info, model);
            // 数据库id
            model.setDid(did);

            // 资源类型转id集合
            model.setTypeList(ConvertUtil.stringToList(info.getTypeList(), Constant.SplitChar.COMMA_CHAR));

            // 学科转id集合
            model.setSubjectList(ConvertUtil.stringToList(info.getSubjectList(), Constant.SplitChar.COMMA_CHAR));

            // 还要回显使用指南的附件数据
            model.setAttachList(companyDatabaseAttachmentDao.findFileListBySidUniqueIdType(sid, info.getId(),
                    EnumAttachmentType.使用指南.getValue()));
        }

        List<String> typeList = Lists.newArrayList();
        for (Long type : model.getTypeList()) {
            EnumDataBaseProperty property = EnumDataBaseProperty.getEnumDataBaseProperty(type);
            if (property != null) {
                typeList.add(property.getName());
            }
        }

        //设置属性的纯文本
        model.setTypeNameList(String.join(Constant.SplitChar.COMMA_CHAR, typeList));

        //学科信息
        SubjectAndCategoryModel categoryModel =
                super.findSubjectAndCategoryModelBySubjectIdSet(Sets.newHashSet(model.getSubjectList()));
        model.setSubjectCategoryNameList(categoryModel.getSubjectCategory());
        model.setSubjectOneNameList(categoryModel.getSubject());

        return model;
    }

    /**
     * 基本信息 -> 子库列表
     *
     * @param apiKey apiKey
     * @param did    子库id
     * @return 访问信息列表
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @Override
    public List<DataBaseTitleDetailModel> findSonDatabaseList(String apiKey, Long did) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        Long sid = databaseKey.getSId();
        Long totalDid = databaseKey.getDId();

        // 查询总库对应的子库
        Set<Long> sonDidSet = companyDatabaseLevelDao.findSonDidListBySidDid(sid, totalDid);

        List<DataBaseTitleDetailModel> list = Lists.newArrayList();

        if (did.equals(Constant.ALL_LONG_VALUE)) {
            for (Long sonDid : sonDidSet) {
                DataBaseTitleDetailModel model = new DataBaseTitleDetailModel();

                // 查询数据
                CompanyDatabaseBaseInfo info = companyDatabaseBaseInfoDao.findOneBySidDid(sid, sonDid);
                if (info == null) {
                    continue;
                }

                BeanUtils.copyProperties(info, model);

                list.add(model);
            }
        } else {
            DataBaseTitleDetailModel model = new DataBaseTitleDetailModel();
            // 查询数据
            CompanyDatabaseBaseInfo info = companyDatabaseBaseInfoDao.findOneBySidDid(sid, did);
            if (info != null) {
                BeanUtils.copyProperties(info, model);
            }
            list.add(model);
        }

        for (DataBaseTitleDetailModel model : list) {
            // 学科转id集合
            Set<Long> subjectIdSet = Lists.newArrayList(model.getSubjectList().split(Constant.SplitChar.COMMA_CHAR))
                    .stream().map(Long::parseLong).collect(Collectors.toSet());

            SubjectAndCategoryModel subjectAndCategoryModel =
                    super.findSubjectAndCategoryModelBySubjectIdSet(subjectIdSet);

            // 设置一级学科
            model.setSubjectList(subjectAndCategoryModel.getSubject());
            // 设置门类学科
            model.setSubjectCategories(subjectAndCategoryModel.getSubjectCategory());

            // 资源类型转id集合
            List<Long> typeList = Lists.newArrayList(model.getTypeList().split(Constant.SplitChar.COMMA_CHAR))
                    .stream().map(Long::parseLong).collect(Collectors.toList());

            Map<Long, String> typeIdNameMap = vdatabasePropertyDao.findListBySid(sid)
                    .stream().collect(Collectors.toMap(IdNameModel::getId, IdNameModel::getName));
            List<String> typeListStr = Lists.newArrayList();
            for (Long typeId : typeList) {
                typeListStr.add(typeIdNameMap.get(typeId) == null ? "" : typeIdNameMap.get(typeId));
            }
            // 设置类型
            model.setTypeList(String.join(Constant.SplitChar.COMMA_CHAR, typeListStr));

            // apiKey
            model.setApiKey(super.findDataBaseApiKeyBySidDid(sid, model.getDId()));
        }

        return list;
    }


    /**
     * 基本信息 -> 删除基本信息
     *
     * @param apiKey apiKey
     * @return 删除的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @Override
    public String deleteDatabaseInfo(String apiKey, Long totalDid) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        Long sid = databaseKey.getSId();
        Long did = databaseKey.getDId();

        // 删除层级关系
        companyDatabaseLevelDao.deleteBySidDidTotalDid(sid, did, totalDid);

        return Vm.DELETE_SUCCESS;
    }


    /**
     * 库商信息 -> 联系人列表
     *
     * @param apiKey      apiKey
     * @param contactType 联系人类型
     * @return 联系人列表
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @Override
    public List<ContactPeopleInfoModel> findDatabaseContactPeople(String apiKey, int contactType) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        Long sid = databaseKey.getSId();
        Long did = databaseKey.getDId();

        // 查询数据
        List<CompanyDatabaseContactPeople> list = companyDatabaseContactPeopleDao
                .findListBySidDidContactType(sid, did, contactType);

        return BeanListCopyUtils.copyListProperties(list, ContactPeopleInfoModel::new);
    }

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
    @Override
    public CompanyDatabaseContactPeople findOneDatabaseContactPeople(String apiKey, Long contactId, int contactType) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        Long sid = databaseKey.getSId();
        Long did = databaseKey.getDId();

        return companyDatabaseContactPeopleDao.findOneBySidDidContactTypeContactId(sid, did, contactType, contactId);
    }

    /**
     * 库商信息 -> 新增或编辑联系人
     *
     * @param contactPeopleInfoModel 联系人详情
     * @param contactType            联系人类型
     * @return 编辑的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String addOrUpdateContactPeople(ContactPeopleInfoModel contactPeopleInfoModel, int contactType) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(contactPeopleInfoModel.getApiKey());
        Long sid = databaseKey.getSId();
        Long did = databaseKey.getDId();

        Long contactId = contactPeopleInfoModel.getContactId();

        // 查询审核状态
        Integer state = this.findCheckState(contactPeopleInfoModel.getApiKey(),
                EnumTableType.库商信息.getValue()).getState();
        if (state == EnumCheckState.待审核.getValue()) {
            throw new CustomException(Vm.CHECKING_NOT_UPDATE);
        }

        // 新增标识
        boolean insertFlag = false;
        if (contactId == null) {
            insertFlag = true;
        }

        // 如果职务包括商务，那邮寄地址为必填项
        if (contactPeopleInfoModel.getPosition().contains(Constant.CompanyWrite.COMMERCE)
                && Strings.isNullOrEmpty(contactPeopleInfoModel.getAddress())) {
            throw new CustomException("请填写邮寄地址！");
        }

        if (insertFlag) {
            // 手机号唯不唯一
            if (companyDatabaseContactPeopleDao.findCountBySidDidContactTypePhone(sid, did, contactType,
                    contactPeopleInfoModel.getPhone()) != 0) {
                throw new CustomException("检测到本库已有相同的联方式！");
            }

            CompanyDatabaseContactPeople model = new CompanyDatabaseContactPeople();
            // 复制并添加数据
            BeanUtils.copyProperties(contactPeopleInfoModel, model);
            model.setSId(sid);
            model.setDId(did);
            model.setType(contactType);
            model.setCreatedTime(LocalDateTime.now());

            // 新增联系人
            companyDatabaseContactPeopleDao.insert(model);
            return Vm.INSERT_SUCCESS;
        } else {
            // 查询数据
            CompanyDatabaseContactPeople contactPeopleModel = companyDatabaseContactPeopleDao
                    .findOneBySidDidContactTypeContactId(sid, did, contactType, contactId);
            if (contactPeopleModel == null) {
                throw new CustomException(Vm.NO_DATA);
            }

            if (!Objects.equals(contactPeopleInfoModel.getPhone(), contactPeopleModel.getPhone())) {
                // 手机号唯不唯一
                if (companyDatabaseContactPeopleDao.findCountBySidDidContactTypePhone(sid, did, contactType,
                        contactPeopleInfoModel.getPhone()) != 0) {
                    throw new CustomException("检测到本库已有相同的联方式！");
                }
            }

            // 复制并修改数据
            BeanUtils.copyProperties(contactPeopleInfoModel, contactPeopleModel);
            contactPeopleModel.setId(contactId);
            contactPeopleModel.setUpdatedTime(LocalDateTime.now());

            // 更新联系人
            companyDatabaseContactPeopleDao.updateById(contactPeopleModel);
            return Vm.UPDATE_SUCCESS;
        }
    }

    /**
     * 查询供应商下拉
     *
     * @param apiKey apiKey
     * @return 供应商集合
     * @author majuehao
     * @date 2022/1/6 14:35
     **/
    @Override
    public List<String> findCompanySelectList(String apiKey) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        List<IdNameModel> list = companyDao.findListBySid(databaseKey.getSId());
        if (list != null) {
            return list.stream().map(IdNameModel::getName).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    /**
     * 查询代理商下拉
     *
     * @param apiKey apiKey
     * @return 代理商集合
     * @author majuehao
     * @date 2022/1/6 14:35
     **/
    @Override
    public List<String> findAgentSelectList(String apiKey) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        List<IdNameModel> list = agentDao.findListBySid(databaseKey.getSId());
        if (list != null) {
            return list.stream().map(IdNameModel::getName).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String deleteContactPeople(String apiKey, Long contactId, int contactType) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        Long sid = databaseKey.getSId();
        Long did = databaseKey.getDId();

        // 查询审核状态
        Integer state = this.findCheckState(apiKey, EnumTableType.库商信息.getValue()).getState();
        if (state == EnumCheckState.待审核.getValue()) {
            throw new CustomException(Vm.CHECKING_NOT_UPDATE);
        }

        //  根据学校id、数据库id、联系人类型、联系人id，删除联系人
        companyDatabaseContactPeopleDao.deleteBySidDidContactTypeContactId(sid, did, contactType, contactId);

        return Vm.DELETE_SUCCESS;
    }

    /**
     * 访问信息 -> 总库访问信息列表
     *
     * @param apiKey apiKey
     * @return 访问信息列表
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @Override
    public List<AccessUrlInfoModel> findAccessUrlList(String apiKey) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        Long sid = databaseKey.getSId();
        Long did = databaseKey.getDId();

        List<CompanyDatabaseAccessUrl> list = companyDatabaseAccessUrlDao.findListBySidDid(sid, did);

        return BeanListCopyUtils.copyListProperties(list, AccessUrlInfoModel::new);
    }

    /**
     * 访问信息 -> 子库访问信息列表
     *
     * @param apiKey apiKey
     * @return 访问信息列表
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @Override
    public List<AccessUrlInfoModel> findSonAccessUrlList(String apiKey, Long did) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        Long sid = databaseKey.getSId();
        Long totalDid = databaseKey.getDId();

        return this.findChildAccessUrlList(sid, did, totalDid);
    }

    /**
     * 查询子库的访问链接
     *
     * @param sid      学校id
     * @param did      筛选的子库
     * @param totalDid 总库id
     * @return 查询的结果
     * @author huxubin
     * @date 2022/1/18 13:49
     */
    private List<AccessUrlInfoModel> findChildAccessUrlList(Long sid, Long did, Long totalDid) {
        List<CompanyDatabaseAccessUrl> list = Lists.newArrayList();
        if (did.equals(Constant.ALL_LONG_VALUE)) {
            // 查询总库对应的子库
            Set<Long> sonDidSet = companyDatabaseLevelDao.findSonDidListBySidDid(sid, totalDid);

            // 查询子库数据
            for (Long sonDid : sonDidSet) {
                list.addAll(companyDatabaseAccessUrlDao.findListBySidDid(sid, sonDid));
            }
        } else {
            list.addAll(companyDatabaseAccessUrlDao.findListBySidDid(sid, did));
        }

        List<AccessUrlInfoModel> result = BeanListCopyUtils.copyListProperties(list, AccessUrlInfoModel::new);
        // 组装名字
        for (AccessUrlInfoModel accessUrlInfoModel : result) {
            accessUrlInfoModel.setName(this.getDatabaseName(sid, accessUrlInfoModel.getDId()));
        }
        return result;
    }

    /**
     * 访问信息 -> 回显编辑访问信息
     *
     * @param apiKey   apiKey
     * @param accessId 访问id
     * @return 访问信息
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @Override
    public AccessUrlInfoModel findOneAccessUrl(String apiKey, Long accessId) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        Long sid = databaseKey.getSId();
        Long did = databaseKey.getDId();

        // 查询数据
        CompanyDatabaseAccessUrl companyDatabaseAccessUrl = companyDatabaseAccessUrlDao.findOneBySidDidAccessId(sid,
                did, accessId);

        if (companyDatabaseAccessUrl == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        AccessUrlInfoModel accessUrlInfoModel = new AccessUrlInfoModel();
        BeanUtils.copyProperties(companyDatabaseAccessUrl, accessUrlInfoModel);
        return accessUrlInfoModel;
    }

    /**
     * 访问信息 -> 新增或编辑访问信息
     *
     * @param accessUrlInfoModel 访问信息
     * @return 新增或编辑的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String addOrUpdateAccessUrl(AccessUrlInfoModel accessUrlInfoModel) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(accessUrlInfoModel.getApiKey());
        Long sid = databaseKey.getSId();
        Long did = databaseKey.getDId();

        Long totalDid = accessUrlInfoModel.getTotalDid();
        if (totalDid == null) {
            accessUrlInfoModel.setTotalDid(did);
        }

        // 查询审核状态
        Integer state = this.findCheckState(accessUrlInfoModel.getApiKey(), EnumTableType.访问信息.getValue()).getState();
        if (state == EnumCheckState.待审核.getValue()) {
            throw new CustomException(Vm.CHECKING_NOT_UPDATE);
        }

        // 新增/编辑标识
        boolean insert = false;
        if (accessUrlInfoModel.getId() == null) {
            insert = true;
        }

        if (insert) {

            // 链接唯不唯一
            if (companyDatabaseAccessUrlDao.findCountBySidDidUrl(sid, did, accessUrlInfoModel.getUrl()) != 0) {
                throw new CustomException("检测到本库已有相同的访问链接！");
            }

            // 声明插入对象，复制属性
            CompanyDatabaseAccessUrl companyDatabaseAccessUrl = new CompanyDatabaseAccessUrl();
            BeanUtils.copyProperties(accessUrlInfoModel, companyDatabaseAccessUrl);
            companyDatabaseAccessUrl.setSId(sid);
            companyDatabaseAccessUrl.setDId(did);
            companyDatabaseAccessUrl.setCreatedTime(LocalDateTime.now());

            // 新增访问信息
            companyDatabaseAccessUrlDao.insert(companyDatabaseAccessUrl);
            return Vm.INSERT_SUCCESS;
        } else {
            // 查询数据
            CompanyDatabaseAccessUrl companyDatabaseAccessUrl = companyDatabaseAccessUrlDao.findOneBySidDidAccessId(sid,
                    did, accessUrlInfoModel.getId());

            // 查不到报错
            if (companyDatabaseAccessUrl == null) {
                throw new CustomException(Vm.NO_DATA);
            }

            if (!Objects.equals(accessUrlInfoModel.getUrl(), companyDatabaseAccessUrl.getUrl())) {
                // 链接唯不唯一
                if (companyDatabaseAccessUrlDao.findCountBySidDidUrl(sid, did, accessUrlInfoModel.getUrl()) != 0) {
                    throw new CustomException("检测到本库已有相同的访问链接！");
                }
            }

            // 复制属性，设置更新时间
            BeanUtils.copyProperties(accessUrlInfoModel, companyDatabaseAccessUrl);
            companyDatabaseAccessUrl.setUpdatedTime(LocalDateTime.now());

            // 更新
            companyDatabaseAccessUrlDao.updateById(companyDatabaseAccessUrl);
            return Vm.UPDATE_SUCCESS;
        }
    }

    /**
     * 访问信息 -> 删除访问信息
     *
     * @param apiKey   apiKey
     * @param accessId 访问id
     * @return 删除的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @Override
    public String deleteAccessUrl(String apiKey, Long accessId) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        Long sid = databaseKey.getSId();
        Long did = databaseKey.getDId();

        // 查询审核状态
        Integer state = this.findCheckState(apiKey, EnumTableType.访问信息.getValue()).getState();
        if (state != null && state == EnumCheckState.待审核.getValue()) {
            throw new CustomException(Vm.CHECKING_NOT_UPDATE);
        }

        // 删除数据
        companyDatabaseAccessUrlDao.deleteBySidDidAccessId(sid, did, accessId);

        return Vm.DELETE_SUCCESS;
    }

    /**
     * 获取子库的apiKey
     *
     * @param apiKey apiKey
     * @param did    子库id
     * @return 子库的apiKey
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @Override
    public String findSonApiKey(String apiKey, Long did) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        Long sid = databaseKey.getSId();

        return super.findDataBaseApiKeyBySidDid(sid, did);
    }

    /**
     * 获取子库的apiKey列表
     *
     * @param apiKey apiKey
     * @return 子库的apiKey列表
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @Override
    public List<IdNameModel> findSonApiKeyList(String apiKey) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        Long sid = databaseKey.getSId();
        Long did = databaseKey.getDId();

        // 查询总库对应的子库
        Set<Long> sonDidSet = companyDatabaseLevelDao.findSonDidListBySidDid(sid, did);

        // 全部选项
        List<IdNameModel> list = Lists.newArrayList(new IdNameModel().create(Constant.ALL_LONG_VALUE,
                Constant.ALL_STRING_VALUE));

        // 组装返回
        for (Long sonDid : sonDidSet) {
            // 名字
            String sonName = companyDatabaseBaseInfoDao.findNameBySidDid(sid, sonDid);

            // apiKey
            String sonApiKey = super.findDataBaseApiKeyBySidDid(sid, sonDid);

            list.add(new IdNameModel().setId(sonDid).setName(sonName).setApiKey(sonApiKey));
        }

        return list;
    }

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
    @Override
    public PageResult<IdNameModel> findDatabaseSelectList(String apiKey, String name, String letter, Long propertyId,
                                                          Long languageId, Integer pageNum, Integer pageSize) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        Long sid = databaseKey.getSId();

        return databaseService.findDatabaseSelectList(sid, name, letter, propertyId, languageId, pageNum, pageSize);
    }

    /**
     * 联想查询数据库
     *
     * @param apiKey apiKey
     * @param search 检索条件
     * @return 数据库集合
     * @author majuehao
     * @date 2021/11/26 9:29
     **/
    @Override
    public List<IdNameModel> findDatabaseListBySearch(String apiKey, String search) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        Long sid = databaseKey.getSId();

        return databaseService.findDatabaseListBySearch(sid, search);
    }

    /**
     * 新增数据库信息
     *
     * @param apiKey       apiKey
     * @param databaseInfo 数据库信息
     * @return 新增的结果
     * @author yeweiwei
     * @date 2021/11/25 10:59
     */
    @Override
    public IdNameModel addDatabase(String apiKey, DatabaseInfoModel databaseInfo) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        Long sid = databaseKey.getSId();
        return databaseService.addDatabase(sid, databaseInfo, null);
    }

    /**
     * 获取标签的审核状态
     *
     * @param apiKey    apiKey
     * @param tableType 标签类型
     * @return 新增的结果
     * @author yeweiwei
     * @date 2021/11/25 10:59
     */
    @Override
    public TableCheckState findCheckState(String apiKey, Integer tableType) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        Long sid = databaseKey.getSId();
        Long did = databaseKey.getDId();

        // 审核id
        Long checkId;
        // 审核状态
        Integer state;
        // 提交审核的时间
        String createdTime = "";
        // 审核的时间
        String checkTime = "";
        // 审核结果
        String remark = "";

        // 查询审核状态
        CompanyWrite companyWrite = companyWriteDao.findOneBySidDidTableType(sid, did, tableType);

        if (companyWrite == null) {
            // 没有查到就生成一条未提交的表格数据
            companyWrite = new CompanyWrite().create(sid, did, tableType, EnumCheckState.未提交.getValue(),
                    LocalDateTime.now());
            companyWriteDao.insert(companyWrite);
        }

        checkId = companyWrite.getCheckId();
        state = companyWrite.getState();

        // 根据审核id判断，是否绑定了审核
        if (checkId != null) {
            // 查询审核信息
            CompanyWriteCheck companyWriteCheck = companyWriteCheckDao.findOneBySidId(sid, checkId);
            if (companyWriteCheck == null) {
                throw new CustomException(Vm.NO_DATA);
            }

            createdTime = DateTimeUtil.localDateTimeToString(companyWriteCheck.getCreatedTime(),
                    Constant.Pattern.DATE_TIME_NO_SECOND);
            checkTime = DateTimeUtil.localDateTimeToString(companyWriteCheck.getCheckTime(),
                    Constant.Pattern.DATE_TIME_NO_SECOND);

            remark = companyWriteCheck.getRemark();
        }

        return new TableCheckState().create(state, createdTime, remark, checkTime, checkTime);
    }

    /**
     * 获取数据库名称
     *
     * @param apiKey apiKey
     * @return 数据库名称
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @Override
    public IdNameModel findDatabaseIdName(String apiKey) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        Long sid = databaseKey.getSId();
        Long did = databaseKey.getDId();

        return new IdNameModel().create(did, getDatabaseName(sid, did));
    }

    /**
     * 根据学校id、数据库id，获取数据库名字
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库名字
     * @author majuehao
     * @date 2022/1/20 15:26
     **/
    private String getDatabaseName(Long sid, Long did) {
        // 获取数据库名字
        String name = companyDatabaseBaseInfoDao.findNameBySidDid(sid, did);

        if (Strings.isNullOrEmpty(name)) {
            name = super.findDatabaseName(sid, did);
        }
        return name;
    }

    /**
     * 返回修改
     *
     * @param apiKey    apiKey
     * @param tableType 标签类型
     * @return 更新结果
     * @author majuehao
     * @date 2021/11/25 10:59
     */
    @Override
    public String updateCheckStateReturnModify(String apiKey, Integer tableType) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        Long sid = databaseKey.getSId();
        Long did = databaseKey.getDId();

        //查询审核
        CompanyWrite companyWrite = companyWriteDao.findOneBySidDidTableType(sid, did, tableType);
        //如果不存在，就直接抛出异常
        if (companyWrite == null) {
            throw new CustomException("非法请求，未能查询到表格状态，已经记录当前操作IP");
        }

        //只有审核通过或不通过状态可以返回修改
        if (companyWrite.getState() != EnumCheckState.审核未通过.getValue()
                && companyWrite.getState() != EnumCheckState.审核通过.getValue()) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        companyWrite.setState(EnumCheckState.未提交.getValue());
        companyWrite.setUpdatedTime(LocalDateTime.now());
        companyWriteDao.updateById(companyWrite);

        return Vm.UPDATE_SUCCESS;
    }
    //region --------------------提交审核--------------------

    /**
     * 提交审核
     *
     * @param apiKey    apiKey
     * @param tableType 标签类型
     * @return 更新结果
     * @author majuehao
     * @date 2021/11/25 10:59
     */
    @Override
    public String updateCheckState(String apiKey, Integer tableType) {
        // 根据apiKey获取数据库Key实体
        DatabaseKey databaseKey = super.findOneByApiKey(apiKey);
        Long sid = databaseKey.getSId();
        Long did = databaseKey.getDId();

        //查询审核
        CompanyWrite companyWrite = companyWriteDao.findOneBySidDidTableType(sid, did, tableType);
        //如果不存在，就直接抛出异常
        if (companyWrite == null) {
            throw new CustomException("非法请求，未能查询到表格状态，已经记录当前操作IP");
        }

        // 校验是否填写完整
        this.checkFillWhetherFull(sid, did);

        //只有未提交状态可以提交审核
        if (companyWrite.getState() != EnumCheckState.未提交.getValue()) {
            throw new CustomException("检测到您已经提交过审核，无法重复提交");
        }

        // 有待审核的就无法再次提交
        if (companyWriteCheckDao.findCountSidDidTableTypeState(sid, did, tableType, EnumCheckState.待审核.getValue())
                != 0) {
            throw new CustomException("检测到数据库已存在待审核的记录，无法重复提交");
        }

        EnumTableType enumTableType = EnumTableType.getEnumTableType(tableType);

        String json;
        switch (enumTableType) {
            case 基本信息: {
                json = this.findBaseInfoCheckJson(sid, did, apiKey);
                break;
            }
            case 库商信息: {
                json = this.findContactPeopleCheckJson(sid, did);
                break;
            }
            case 访问信息: {
                json = this.findAccessUrlCheckJson(sid, did);
                break;
            }
            default:
                throw new CustomException(Vm.ERROR_PARAMS);
        }

        // 增加审核信息
        CompanyWriteCheck companyWriteCheck = new CompanyWriteCheck().create(
                sid, did, tableType, null, null, EnumCheckState.待审核.getValue(), null,
                json, null, LocalDateTime.now());
        companyWriteCheckDao.insert(companyWriteCheck);

        // 修改表格状态并绑定审核信息
        companyWrite.update(EnumCheckState.待审核.getValue(), companyWriteCheck.getId(), null,
                LocalDateTime.now());
        companyWriteDao.updateById(companyWrite);

        return Vm.UPDATE_SUCCESS;
    }

    /**
     * 校验是否填写完整
     *
     * @param sid 学校id
     * @param did 数据库id
     * @author majuehao
     * @date 2022/2/9 9:51
     **/
    private void checkFillWhetherFull(Long sid, Long did) {
        CompanyDatabaseBaseInfo info = companyDatabaseBaseInfoDao.findOneBySidDid(sid, did);
        if (info == null) {
            throw new CustomException(Vm.NO_DATA);
        }

        String subjectList = info.getSubjectList();
        String typeList = info.getTypeList();

        if (Strings.isNullOrEmpty(subjectList)) {
            throw new CustomException("请选择学科覆盖");
        }
        if (Strings.isNullOrEmpty(typeList)) {
            throw new CustomException("请选择资源类型");
        }

        TotalDatabaseInfoModel infoModel = new TotalDatabaseInfoModel();
        BeanUtils.copyProperties(info, infoModel);
        this.checkData(infoModel, false);
    }

    /**
     * 查询基础信息的JSON
     *
     * @param sid         学校id
     * @param totalDid    总库的id
     * @param totalApiKey 总库的apiKey
     * @return 基础信息JSON
     * @author huxubin
     * @date 2022/1/18 13:17
     */
    private String findBaseInfoCheckJson(Long sid, Long totalDid, String totalApiKey) {

        //总库的
        TotalDatabaseInfoModel totalInfo = this.findOneTotalDatabaseInfo(totalApiKey);

        // 查询总库对应的子库
        List<TotalDatabaseInfoModel> childInfoList = Lists.newArrayList();
        Set<Long> sonDidSet = companyDatabaseLevelDao.findSonDidListBySidDid(sid, totalDid);

        //子库下拉筛选
        List<IdNameModel> childSelectList = Lists.newArrayList();
        childSelectList.add(new IdNameModel().create(Constant.ALL_LONG_VALUE, Constant.ALL_STRING_VALUE));
        if (!CollectionUtils.isEmpty(sonDidSet)) {
            for (Long did : sonDidSet) {
                //查询apiKey
                String apiKey = super.findDataBaseApiKeyBySidDid(sid, did);
                //子库信息
                TotalDatabaseInfoModel childInfoModel = this.findOneTotalDatabaseInfo(apiKey);
                //添加筛选
                childSelectList.add(new IdNameModel().create(did, childInfoModel.getName()));
                //添加子库信息
                childInfoList.add(childInfoModel);
            }
        }

        //组装数据返回
        DataBaseInfoCheckModel infoCheckModel = new DataBaseInfoCheckModel();
        infoCheckModel.create(childSelectList, totalInfo, childInfoList);
        return JSON.toJSONString(infoCheckModel);
    }

    /**
     * 查询库商联系人JSON
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 库商联系人JSON
     * @author huxubin
     * @date 2022/1/18 13:18
     */
    private String findContactPeopleCheckJson(Long sid, Long did) {

        //先找供应商联系人
        List<CompanyDatabaseContactPeople> companyPeopleList =
                companyDatabaseContactPeopleDao.findListBySidDidContactType(sid, did,
                        Constant.CompanyWrite.COMPANY_CONTACT);


        //再找代理商联系人
        List<CompanyDatabaseContactPeople> agentPeopleList =
                companyDatabaseContactPeopleDao.findListBySidDidContactType(sid, did,
                        Constant.CompanyWrite.AGENT_CONTACT);

        //如果供应商和代理商联系人都没有，那就不能提交审核
        if (CollectionUtils.isEmpty(companyPeopleList) && CollectionUtils.isEmpty(agentPeopleList)) {
            throw new CustomException("很抱歉，您尚未填写供应商和代理商联系人信息，无法提交审核");
        }

        //生成数据
        ContactPeopleCheckModel peopleCheckModel = new ContactPeopleCheckModel();
        List<ContactPeopleInfoModel> companyList = BeanListCopyUtils.copyListProperties(companyPeopleList,
                ContactPeopleInfoModel::new);
        List<ContactPeopleInfoModel> agentList = BeanListCopyUtils.copyListProperties(agentPeopleList,
                ContactPeopleInfoModel::new);
        peopleCheckModel.create(companyList, agentList);
        return JSON.toJSONString(peopleCheckModel);
    }

    /**
     * 查询访问信息的JSON
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 访问信息JSON
     * @author huxubin
     * @date 2022/1/18 13:13
     */
    private String findAccessUrlCheckJson(Long sid, Long did) {

        //总库的
        List<CompanyDatabaseAccessUrl> totalList = companyDatabaseAccessUrlDao.findListBySidDid(sid, did);

        //如果总库没有访问链接，不允许提交
        if (CollectionUtils.isEmpty(totalList)) {
            throw new CustomException("很抱歉，您尚未填写总库访问信息，无法提交审核");
        }

        //子库的访问链接
        List<AccessUrlInfoModel> childUrlList = this.findChildAccessUrlList(sid, Constant.ALL_LONG_VALUE, did);

        //总库信息链接
        List<AccessUrlInfoModel> totalUrlList = BeanListCopyUtils.copyListProperties(totalList, AccessUrlInfoModel::new);

        //子库下拉
        List<IdNameModel> childSelectList = Lists.newArrayList();
        childSelectList.add(new IdNameModel().create(Constant.ALL_LONG_VALUE, Constant.ALL_STRING_VALUE));
        for (AccessUrlInfoModel childUrl : childUrlList) {
            childSelectList.add(new IdNameModel().create(childUrl.getDId(), childUrl.getName()));
        }


        //生成数据
        AccessUrlCheckModel urlCheckModel = new AccessUrlCheckModel();
        urlCheckModel.create(childSelectList, totalUrlList, childUrlList);
        return JSON.toJSONString(urlCheckModel);
    }

    //endregion

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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAttachment(Long sid, Long uniqueId, Integer type, List<AttachmentDontCheckModel> attachList) {
        if (null == attachList) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        // 更新附件表
        List<CompanyDatabaseAttachment> attachmentModelList =
                companyDatabaseAttachmentDao.findListBySidUniqueIdType(sid, uniqueId, type);

        Map<String, CompanyDatabaseAttachment> filePathMap = attachmentModelList.stream().collect(Collectors
                .toMap(CompanyDatabaseAttachment::getFilePath, a -> a));

        // 更新附件表
        for (AttachmentDontCheckModel attachmentModel : attachList) {
            String filePath = attachmentModel.getFilePath();
            if (filePathMap.containsKey(filePath)) {
                filePathMap.remove(filePath);
            } else {
                // 新增
                CompanyDatabaseAttachment attachment = new CompanyDatabaseAttachment().create(sid, uniqueId,
                        type, attachmentModel.getFileName(), filePath, LocalDateTime.now());
                companyDatabaseAttachmentDao.insert(attachment);
            }
        }

        //filePathMap中剩余的是以前上传的附件，已经不用了，删除
        if (!CollectionUtils.isEmpty(filePathMap)) {
            for (Map.Entry<String, CompanyDatabaseAttachment> entry : filePathMap.entrySet()) {
                CompanyDatabaseAttachment attachment = entry.getValue();
                companyDatabaseAttachmentDao.deleteById(attachment.getId());
                try {
                    MinioFileUtil.removeObject(Constant.MinIoBucketName.ERMS, attachment.getFilePath());
                } catch (Exception e) {
                    log.error("删除文件出错：" + e.getMessage(), e);
                    throw new CustomException(Vm.SYSTEM_ERROR_PLEASE_REFRESH_AND_RETRY);
                }
            }
        }
    }

}
