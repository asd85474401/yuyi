package com.kcidea.erms.service.database.impl;

import com.alibaba.excel.EasyExcel;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.util.BeanListCopyUtils;
import com.kcidea.erms.common.util.DateTimeUtil;
import com.kcidea.erms.common.util.DownloadUtil;
import com.kcidea.erms.common.util.FormatUtil;
import com.kcidea.erms.dao.database.*;
import com.kcidea.erms.domain.database.DatabaseAccessUrl;
import com.kcidea.erms.domain.database.DatabaseBaseInfo;
import com.kcidea.erms.domain.database.DatabaseContactPeople;
import com.kcidea.erms.domain.database.DatabaseLevel;
import com.kcidea.erms.domain.ers.Agent;
import com.kcidea.erms.domain.ers.Company;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import com.kcidea.erms.enums.database.EnumAttachmentType;
import com.kcidea.erms.enums.database.EnumDataBaseType;
import com.kcidea.erms.model.company.AccessUrlInfoModel;
import com.kcidea.erms.model.company.ContactPeopleInfoModel;
import com.kcidea.erms.model.database.AttachmentDontCheckModel;
import com.kcidea.erms.model.database.AttachmentModel;
import com.kcidea.erms.model.database.DatabasePropertyModel;
import com.kcidea.erms.model.database.DidTotalIdModel;
import com.kcidea.erms.model.database.detail.DataBaseTitleDetailModel;
import com.kcidea.erms.model.database.detail.DatabaseInfoModel;
import com.kcidea.erms.model.database.info.DatabaseInfoInsertModel;
import com.kcidea.erms.model.subject.SubjectAndCategoryModel;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.company.CompanyWriteCheckService;
import com.kcidea.erms.service.database.DataBaseInfoService;
import com.kcidea.erms.service.database.DatabaseEvaluationService;
import com.kcidea.erms.service.ers.DatabaseService;
import com.kcidea.erms.service.ers.SchoolDatabaseSubjectRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.Collator;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author huxubin
 * @version 1.0
 * @date 2022/1/6
 **/
@Slf4j
@Service
public class DataBaseInfoServiceImpl extends BaseService implements DataBaseInfoService {

    @Resource
    private DatabaseBaseInfoDao databaseBaseInfoDao;

    @Resource
    private DatabaseLevelDao databaseLevelDao;

    @Resource
    private DatabaseAttachmentDao databaseAttachmentDao;

    @Resource
    private DatabaseService databaseService;

    @Resource
    private SchoolDatabaseSubjectRelService schoolDatabaseSubjectRelService;

    @Resource
    private DatabaseEvaluationService databaseEvaluationService;

    @Resource
    private DatabaseAccessUrlDao databaseAccessUrlDao;

    @Resource
    private DatabaseContactPeopleDao databaseContactPeopleDao;

    @Resource
    private CompanyWriteCheckService companyWriteCheckService;

    @Value("${my-config.temp-path}")
    private String tempPath;

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
     * @return 数据库列表
     * @author majuehao
     * @date 2021/10/18 10:27
     **/
    @Override
    public PageResult<DataBaseTitleDetailModel> findDatabaseList(Long sid, Long did, Long languageId, String area,
                                                                 String natureType, Integer fulltextFlag,
                                                                 Long propertyId, Long subjectId, Integer pageFlag,
                                                                 Integer pageNum, Integer pageSize) {
        // 校验参数
        super.checkSid(sid);
        if (pageFlag.equals(EnumTrueFalse.是.getValue())) {
            super.checkPageParam(pageNum, pageSize);
        }

        PageResult<DataBaseTitleDetailModel> result = new PageResult<>();

        // 查询可用数据库
        Set<Long> didSet = databaseLevelDao.findDidSetBySid(sid);

        if (CollectionUtils.isEmpty(didSet)) {
            return result.success(Lists.newArrayList(), 0);
        }

        // 根据学校id、语种、学科、属性、地区、数据库性质、全文标识，筛选集合
        getListByCondition(sid, propertyId, languageId, subjectId, did, area, natureType, fulltextFlag, didSet);

        // 查询子库与总库的对应表
        Map<Long, Long> didTotalIdMap = databaseLevelDao.findDidTotalIdListBySid(sid).stream()
                .collect(Collectors.toMap(DidTotalIdModel::getDid, DidTotalIdModel::getTotalDid,
                        (key1, key2) -> key2));
        // 将符合条件的数据库id中，将子库替换成相应的总库
        for (Long sonDid : didTotalIdMap.keySet()) {
            if (didSet.contains(sonDid)) {
                didSet.remove(sonDid);
                didSet.add(didTotalIdMap.get(sonDid));
            }
        }

        // 组装数据库名称并排序返回
        List<DataBaseTitleDetailModel> list = packageNamesAndSort(sid, didSet);

        // 分页
        int total = list.size();
        if (pageFlag.equals(EnumTrueFalse.是.getValue())) {
            list = list.stream().skip(Math.min(pageNum * pageSize - pageSize, total))
                    .limit(pageSize).collect(Collectors.toList());
        }

        // 组装数据
        prepareData(sid, list);

        // 组装数据返回
        return result.success(list, total);
    }

    /**
     * 组装数据库名称并排序返回
     *
     * @param sid    学校id
     * @param didSet 数据库id集合
     * @return 排序集合
     * @author majuehao
     * @date 2022/1/13 15:08
     **/
    private List<DataBaseTitleDetailModel> packageNamesAndSort(Long sid, Set<Long> didSet) {
        List<DataBaseTitleDetailModel> list = Lists.newArrayList();

        // 组装数据库名称
        for (Long databaseId : didSet) {
            DataBaseTitleDetailModel model = new DataBaseTitleDetailModel()
                    .setDId(databaseId).setName(super.findDatabaseName(sid, databaseId));
            list.add(model);
        }

        // 排序返回
        Comparator<Object> comparator = Collator.getInstance(Locale.CHINA);
        list.sort((x, y) -> comparator.compare(x.getName(), y.getName()));
        return list;
    }

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
    @Override
    public List<DataBaseTitleDetailModel> findSonDatabaseList(Long sid, Long totalDid, Long languageId, String area,
                                                              String natureType, Integer fulltextFlag, Long propertyId,
                                                              Long subjectId) {
        // 校验参数
        super.checkSid(sid);

        List<DataBaseTitleDetailModel> list = Lists.newArrayList();

        // 查询子库
        Set<Long> sonDidSet = databaseLevelDao.findSonListBySidTotalDid(sid, totalDid);

        if (sonDidSet == null) {
            return list;
        }

        // 根据学校id、语种、学科、属性、地区、数据库性质、全文标识,筛选集合
        getListByCondition(sid, propertyId, languageId, subjectId, null, area, natureType, fulltextFlag, sonDidSet);

        // 组装数据库名称并排序返回
        list = packageNamesAndSort(sid, sonDidSet);

        // 组装数据
        prepareData(sid, list);

        // 返回
        return list;
    }

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
    @Override
    public ResponseEntity<byte[]> exportDatabaseList(Long sid, Long did, Long languageId, String area, String natureType,
                                                     Integer fulltextFlag, Long propertyId, Long subjectId) {
        // 校验参数
        super.checkSid(sid);

        // 返回对象
        List<DataBaseTitleDetailModel> exportModels = Lists.newArrayList();

        // 查询主库信息
        List<DataBaseTitleDetailModel> list = this.findDatabaseList(sid, did, languageId, area, natureType,
                fulltextFlag, propertyId, subjectId, EnumTrueFalse.否.getValue(), null, null).getData();

        // 查询学校访问信息
        Map<Long, String> accessUrlMap = this.findAccessUrlMap(sid);

        // 查询学校联系人
        Map<String, String> contactPeopleMap = this.findContactPeopleMap(sid);

        // 查询主库、子库的信息按顺序组装进excel实体
        for (DataBaseTitleDetailModel model : list) {

            // 组装导出数据（总库）
            prepareExcelData(model, exportModels, EnumDataBaseType.总库, accessUrlMap, contactPeopleMap);

            // 如果该主库有子库，查询子库并组装进excel
            if (model.getIcon().equals(EnumTrueFalse.是.getValue())) {
                // 查询子库信息
                List<DataBaseTitleDetailModel> sonList = findSonDatabaseList(sid, model.getDId(), languageId, area,
                        natureType, fulltextFlag, propertyId, subjectId);

                for (DataBaseTitleDetailModel sonModel : sonList) {

                    // 组装导出数据（子库）
                    prepareExcelData(sonModel, exportModels, EnumDataBaseType.子库, accessUrlMap, contactPeopleMap);
                }
            }
        }

        // 导出的文件名称和路径
        String filePath = tempPath + System.currentTimeMillis();
        String fileName = "数据库列表";

        // 导出
        EasyExcel.write(filePath, DataBaseTitleDetailModel.class).sheet(fileName).doWrite(exportModels);
        return DownloadUtil.getResponseEntityCanDeleteFile(filePath,
                fileName.concat(Constant.Suffix.XLSX_WITH_POINT), EnumTrueFalse.是.getValue());
    }

    /**
     * 根据学校id查询访问信息，再根据did分组
     *
     * @param sid 学校id
     * @return 访问信息Map
     * @author majuehao
     * @date 2022/1/24 17:05
     **/
    private Map<Long, String> findAccessUrlMap(Long sid) {
        Map<Long, String> accessUrlMap = Maps.newHashMap();

        Map<Long, List<DatabaseAccessUrl>> listMap = companyWriteCheckService.findSchoolAccessUrlMap(sid);

        for (Map.Entry<Long, List<DatabaseAccessUrl>> entry : listMap.entrySet()) {
            List<DatabaseAccessUrl> accessUrls = entry.getValue();
            StringBuilder accessUrlStr = new StringBuilder();

            for (DatabaseAccessUrl accessUrl : accessUrls) {
                // 将逗号替换为顿号
                String accessType = FormatUtil.formatValue(accessUrl.getAccessType())
                        .replaceAll(Constant.SplitChar.COMMA_CHAR, Constant.SplitChar.CHAIN_COMMA_CHAR);
                String loginType = FormatUtil.formatValue(accessUrl.getLoginType())
                        .replaceAll(Constant.SplitChar.COMMA_CHAR, Constant.SplitChar.CHAIN_COMMA_CHAR);

                accessUrlStr.append("访问方式："
                        .concat(accessType).concat("，访问地址：")
                        .concat(FormatUtil.formatValue(accessUrl.getUrl()))
                        .concat("，登录方式：")
                        .concat(loginType).concat("；\n"));
            }

            accessUrlMap.put(entry.getKey(), accessUrlStr.toString());
        }

        return accessUrlMap;
    }

    /**
     * 根据学校id查询联系人，再根据did+联系人类型分组
     *
     * @param sid 学校id
     * @return 联系人Map
     * @author majuehao
     * @date 2022/1/24 16:54
     **/
    private Map<String, String> findContactPeopleMap(Long sid) {
        Map<String, String> contactPeopleMap = Maps.newHashMap();

        // 查询数据
        Map<String, List<DatabaseContactPeople>> schoolContactMap = Maps.newHashMap();
        List<DatabaseContactPeople> schoolContactList = databaseContactPeopleDao.findListBySid(sid);

        if (!CollectionUtils.isEmpty(schoolContactList)) {
            schoolContactMap = schoolContactList.stream().collect(Collectors.groupingBy(
                    x -> FormatUtil.formatValue(x.getDId()).concat(Constant.SplitChar.LINE_CHAR)
                            .concat(FormatUtil.formatValue(x.getType()))));
        }

        for (Map.Entry<String, List<DatabaseContactPeople>> entry : schoolContactMap.entrySet()) {
            List<DatabaseContactPeople> contactPeopleList = entry.getValue();
            StringBuilder peopleStr = new StringBuilder();

            for (DatabaseContactPeople people : contactPeopleList) {
                String position = FormatUtil.formatValue(people.getPosition())
                        .replaceAll(Constant.SplitChar.COMMA_CHAR, Constant.SplitChar.CHAIN_COMMA_CHAR);

                peopleStr.append(FormatUtil.formatValue(people.getName())
                        .concat("（")
                        .concat(position).concat("），手机号：")
                        .concat(FormatUtil.formatValue(people.getPhone()))
                        .concat("，邮箱：").concat(FormatUtil.formatValue(people.getEmail()))
                        .concat("，邮寄地址：").concat(FormatUtil.formatChainNoValue(people.getAddress()))
                        .concat("；\n"));
            }
            contactPeopleMap.put(entry.getKey(), peopleStr.toString());
        }

        return contactPeopleMap;
    }

    /**
     * 将数据装入excel的model
     *
     * @param model            列表数据
     * @param exportModels     excel的model
     * @param type             数据库类型
     * @param accessUrlMap     访问信息mao
     * @param contactPeopleMap 联系人map
     * @author majuehao
     * @date 2022/1/24 17:23
     **/
    private void prepareExcelData(DataBaseTitleDetailModel model, List<DataBaseTitleDetailModel> exportModels
            , EnumDataBaseType type, Map<Long, String> accessUrlMap, Map<String, String> contactPeopleMap) {
        Long did = model.getDId();

        // 子库名字前增加制表符
        if (type == EnumDataBaseType.子库) {
            model.setName(Constant.SplitChar.SON_DATABASE_TAB.concat(model.getName()));
        }

        // 处理子库数量
        model.setSonCountStr(FormatUtil.formatCount(model.getSonCount()));

        // 层级
        model.setLevel(type.getName());

        // 数据时间
        model.setDataTime(FormatUtil.formatDataTime(model.getDataTime()));

        // 访问信息
        model.setAccessInfo(accessUrlMap.get(did));

        // 供应商联系人信息
        model.setCompanyPeople(
                contactPeopleMap.get(did + Constant.SplitChar.LINE_CHAR + Constant.CompanyWrite.COMPANY_CONTACT));

        // 代理商联系人信息
        model.setAgentPeople(
                contactPeopleMap.get(did + Constant.SplitChar.LINE_CHAR + Constant.CompanyWrite.AGENT_CONTACT));

        // 添加进excel
        exportModels.add(model);
    }

    /**
     * 根据学校id、语种、学科、属性、地区、数据库性质、全文标识,筛选集合
     *
     * @param sid          学校id
     * @param propertyId   属性id
     * @param languageId   语种id
     * @param subjectId    学科id
     * @param did          数据库id
     * @param area         地区
     * @param natureType   数据库性质
     * @param fulltextFlag 全文标识
     * @param didSet       数据库id集合
     * @author majuehao
     * @date 2022/1/12 14:17
     **/
    private void getListByCondition(Long sid, Long propertyId, Long languageId, Long subjectId, Long did, String area,
                                    String natureType, Integer fulltextFlag, Set<Long> didSet) {
        // 查询符合学校id，地区，数据库性质，全文标识的数据库
        if (!area.equals(Constant.ALL_STRING_VALUE) || !natureType.equals(Constant.ALL_STRING_VALUE) ||
                !fulltextFlag.equals(Constant.ALL_INT_VALUE)) {
            Set<Long> didByAreaNatureTypeFulltextFlag = databaseBaseInfoDao.findListBySidAreaNatureTypeFulltextFlag(
                    sid, area, natureType, fulltextFlag);
            // 筛选
            didSet.retainAll(didByAreaNatureTypeFulltextFlag);
        }

        // 查询符合数据库id的数据库
        if (null != did && !did.equals(Constant.ALL_LONG_VALUE)) {
            didSet.retainAll(Sets.newHashSet(did));
        }

        if (!languageId.equals(Constant.ALL_LONG_VALUE) || !propertyId.equals(Constant.ALL_LONG_VALUE)) {
            // 查询符合数据库属性、数据库语种数据，优先学校沙箱数据库
            Set<Long> didSetBySidPropertyIdLanguageId =
                    super.getDidSetBySidPropertyIdLanguageId(sid, propertyId, languageId);
            // 筛选
            didSet.retainAll(didSetBySidPropertyIdLanguageId);
        }

        if (!subjectId.equals(Constant.ALL_LONG_VALUE)) {
            // 筛选符合学科的数据
            Set<Long> didSetBySidSubject = super.getDidSetBySidSubject(sid, subjectId);
            // 筛选
            didSet.retainAll(didSetBySidSubject);
        }
    }

    /**
     * 组装数据
     *
     * @param sid  学校id
     * @param list 列表数据
     * @author majuehao
     * @date 2022/1/12 14:30
     **/
    private void prepareData(Long sid, List<DataBaseTitleDetailModel> list) {

        // 地区、数据库性质、全文标识、更新时间、数据时间、资源总量、资源容量、更新频率、检索功能、并发数、使用指南、内容简介
        String area = "";
        String natureType = "";
        String fulltextFlag = "";
        String updatedTime = "";
        String dataTime = "";
        String totalResource = "";
        String resourceCapacity = "";
        String updateFrequency = "";
        String search = "";
        String concurrency = "";
        String introduce = "";
        String companyName = "";
        String agentName = "";
        String attachments = "";

        Map<Long, Company> companyMap = Maps.newHashMap();
        Map<Long, Agent> agentMap = Maps.newHashMap();

        // 循环组装数据
        for (DataBaseTitleDetailModel model : list) {

            Long did = model.getDId();

            DatabaseBaseInfo info = databaseBaseInfoDao.findOneBySidDid(sid, did);
            if (info != null) {
                area = FormatUtil.formatValue(info.getArea());
                natureType = FormatUtil.formatValue(info.getNatureType());
                fulltextFlag = FormatUtil.formatTrueFlagValue(info.getFulltextFlag());
                updatedTime = DateTimeUtil.localDateTimeToString(info.getUpdatedTime());
                dataTime = FormatUtil.formatValue(info.getDataTime());
                totalResource = FormatUtil.formatValue(info.getTotalResource());
                resourceCapacity = FormatUtil.formatValue(info.getResourceCapacity());
                updateFrequency = FormatUtil.formatValue(info.getUpdateFrequency());
                search = FormatUtil.formatValue(info.getSearch());
                concurrency = FormatUtil.formatValue(info.getConcurrency());
                introduce = FormatUtil.formatValue(info.getIntroduce());
                companyName = super.findCompanyName(info.getCompanyId(), companyMap);
                agentName = super.findAgentName(info.getAgentId(), agentMap);
                attachments = super.attachmentToString(sid, info.getId(), EnumAttachmentType.使用指南);
            }
            // 语种、类型
            DatabasePropertyModel propertyModel = super.findDatabaseProperty(sid, did);
            String language = propertyModel.getLanguage();
            String types = propertyModel.getProperties();
            String paperFlag = propertyModel.getPaperFlag();

            // 门类学科、一级学科
            SubjectAndCategoryModel subjectModel = super.findDatabaseSubjects(sid, did);
            String subjectCategory = subjectModel.getSubjectCategory();
            String subjects = subjectModel.getSubject();

            // 子库数量
            int sonCount = databaseLevelDao.findSonCountListBySidDid(sid, did);
            String sonCountStr = sonCount + "";

            // 是否有子库的标识
            int icon = sonCount > 0 ? EnumTrueFalse.是.getValue() : EnumTrueFalse.否.getValue();

            model.create(did, language, companyName, area, natureType, agentName, fulltextFlag, types, subjectCategory,
                    subjects, sonCount, icon, sonCountStr, updatedTime, dataTime, totalResource, resourceCapacity,
                    updateFrequency, search, concurrency, attachments, introduce, paperFlag);
        }
    }

    /**
     * 删除数据库
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 删除的结果
     * @author majuehao
     * @date 2022/1/13 9:53
     **/
    @Override
    public String deleteDatabase(Long sid, Long did) {
        super.checkSidDid(sid, did);
        databaseLevelDao.deleteBySidDid(sid, did);
        return Vm.DELETE_SUCCESS;
    }

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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String addDatabaseLevel(Long sid, Long userId, Long did, Integer totalFlag, Long totalDid) {
        super.checkSidDid(sid, did);
        super.checkUserId(userId);

        //查询改数据库是否存在
        Set<Long> didSet = databaseLevelDao.findDidSetBySid(sid);
        if (didSet.contains(did)) {
            throw new CustomException("该数据库已存在,无需重复新增");
        }

        if (totalFlag == EnumDataBaseType.子库.getValue() && totalDid == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        if (totalDid == null) {
            totalDid = did;
        }

        // 添加层级信息
        DatabaseLevel databaseLevel = new DatabaseLevel().create(sid, did, totalFlag, totalDid, null, userId,
                LocalDateTime.now());
        databaseLevelDao.insert(databaseLevel);

        // 如果找不到基本信息，添加基础信息
        if (databaseBaseInfoDao.findCountBySidDid(sid, did) == 0) {
            databaseBaseInfoDao.insert(new DatabaseBaseInfo().create(sid, did, userId, LocalDateTime.now()));
        }

        return Vm.INSERT_SUCCESS;
    }


    /**
     * 编辑数据库
     *
     * @param sid    学校id
     * @param userId 用户id
     * @param model  基本信息详情
     * @return 更新的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @Override
    public String updateDatabase(Long sid, Long userId, DatabaseInfoInsertModel model) {
        checkSidUserId(sid, userId);

        if (null == model) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        // 数据库id
        Long did = model.getDid();

        // 别名、语种、类型、纸电
        DatabaseInfoModel databaseInfoModel = model.getDatabaseInfoModel();
        databaseInfoModel.setDid(model.getDid());
        databaseService.updateDatabase(sid, databaseInfoModel, userId);

        // 数据库学科
        List<Long> subjectList = model.getSubjectList();
        schoolDatabaseSubjectRelService.updateDatabaseSubjectRel(sid, did, subjectList, userId);

        // 是否新增代理商和数据商
        model.setCompanyId(databaseService.findCompanyId(sid, userId, model.getCompanyName(), true));
        model.setAgentId(databaseService.findAgentId(sid, userId, model.getAgentName(), true));

        // 新增或编辑基础信息
        DatabaseBaseInfo info = this.addOrUpdateDatabaseBaseInfo(sid, did, userId, model);

        // 更新附件表
        databaseEvaluationService.updateAttachment(sid, info.getId(), EnumAttachmentType.使用指南.getValue(),
                BeanListCopyUtils.copyListProperties(model.getAttachList(), AttachmentModel::new), userId);

        return Vm.UPDATE_SUCCESS;
    }


    /**
     * 新增或者更新基础信息
     *
     * @param sid    学校id
     * @param did    数据库id
     * @param userId 用户id
     * @param model  用户填写的数据
     * @author majuehao
     * @date 2022/1/14 16:50
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DatabaseBaseInfo addOrUpdateDatabaseBaseInfo(Long sid, Long did, Long userId, DatabaseInfoInsertModel model) {
        DatabaseBaseInfo info = databaseBaseInfoDao.findOneBySidDid(sid, did);
        // 为空就要新增
        if (info == null) {
            info = new DatabaseBaseInfo();
            BeanUtils.copyProperties(model, info);
            info.setSId(sid);
            info.setDId(did);
            info.setCreatedBy(userId);
            info.setCreatedTime(LocalDateTime.now());
            databaseBaseInfoDao.insert(info);
        } else {
            // 不为空就要编辑
            BeanUtils.copyProperties(model, info);
            info.setSId(sid);
            info.setDId(did);
            info.setUpdatedBy(userId);
            info.setUpdatedTime(LocalDateTime.now());
            databaseBaseInfoDao.updateById(info);
        }
        return info;
    }

    /**
     * 回显编辑数据库
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 基本信息
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @Override
    public DatabaseInfoInsertModel findOneTotalDatabaseInfo(Long sid, Long did) {
        // 返回集合
        DatabaseInfoInsertModel model = new DatabaseInfoInsertModel();
        model.setSubjectList(Lists.newArrayList());
        model.setAttachList(Lists.newArrayList());

        // 数据库id
        model.setDid(did);

        // 查询底层的配置，回显：数据库名称、语种、纸电、资源类型
        model.setDatabaseInfoModel(databaseService.findDatabaseInfo(sid, did));

        // 查询基本数据
        DatabaseBaseInfo info = databaseBaseInfoDao.findOneBySidDid(sid, did);

        if (info != null) {
            // info不为空，回显基本数据
            BeanUtils.copyProperties(info, model);

            // 学科覆盖
            model.setSubjectList(super.findDatabaseSubjectIdList(sid, did));

            // 回显使用指南的附件数据
            List<AttachmentModel> attachmentModelList = databaseAttachmentDao.
                    findNamePathListBySidUniqueIdType(sid, info.getId(), EnumAttachmentType.使用指南.getValue());
            model.setAttachList(BeanListCopyUtils.copyListProperties(attachmentModelList, AttachmentDontCheckModel::new));

            // 代理商
            model.setAgentName(super.findAgentName(info.getAgentId(), Maps.newHashMap()));

            // 供应商
            model.setCompanyName(super.findCompanyName(info.getCompanyId(), Maps.newHashMap()));
        }

        return model;
    }

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
    @Override
    public AccessUrlInfoModel findOneAccessUrl(Long sid, Long did, Long accessId) {
        // 查询数据
        DatabaseAccessUrl databaseAccessUrl = databaseAccessUrlDao.findOneBySidDidAccessId(sid, did, accessId);

        AccessUrlInfoModel accessUrlInfoModel = new AccessUrlInfoModel();

        if (databaseAccessUrl != null) {
            BeanUtils.copyProperties(databaseAccessUrl, accessUrlInfoModel);
        }

        return accessUrlInfoModel;
    }

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
    @Override
    public String deleteAccessUrl(Long sid, Long did, Long accessId) {
        databaseAccessUrlDao.deleteBySidDidAccessId(sid, did, accessId);
        return Vm.DELETE_SUCCESS;
    }

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
    @Override
    public String addAccessUrl(Long sid, Long userId, AccessUrlInfoModel databaseAccessUrlInfoModel) {
        checkSidUserId(sid, userId);

        if (null == databaseAccessUrlInfoModel) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        checkDid(databaseAccessUrlInfoModel.getDId());

        // 声明插入对象，复制属性
        DatabaseAccessUrl databaseAccessUrl = new DatabaseAccessUrl();
        BeanUtils.copyProperties(databaseAccessUrlInfoModel, databaseAccessUrl);
        databaseAccessUrl.setSId(sid);
        databaseAccessUrl.setCreatedBy(userId);
        databaseAccessUrl.setCreatedTime(LocalDateTime.now());

        // 新增访问信息
        databaseAccessUrlDao.insert(databaseAccessUrl);
        return Vm.INSERT_SUCCESS;
    }

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
    @Override
    public String updateAccessUrl(Long sid, Long userId, AccessUrlInfoModel databaseAccessUrlInfoModel) {
        checkSidUserId(sid, userId);

        if (null == databaseAccessUrlInfoModel || null == databaseAccessUrlInfoModel.getAccessId()) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        checkDid(databaseAccessUrlInfoModel.getDId());

        Long accessId = databaseAccessUrlInfoModel.getAccessId();
        Long did = databaseAccessUrlInfoModel.getDId();

        DatabaseAccessUrl databaseAccessUrl = databaseAccessUrlDao.findOneBySidDidAccessId(sid, did, accessId);
        BeanUtils.copyProperties(databaseAccessUrlInfoModel, databaseAccessUrl);
        databaseAccessUrl.setUpdatedBy(userId);
        databaseAccessUrl.setUpdatedTime(LocalDateTime.now());
        databaseAccessUrlDao.updateById(databaseAccessUrl);

        return Vm.UPDATE_SUCCESS;
    }

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
    @Override
    public DatabaseContactPeople findOneContactPeople(Long sid, Long did, Long contactId, Integer contactType) {
        super.checkSidDid(sid, did);
        return databaseContactPeopleDao.findOneBySidDidContactTypeContactId(sid, did, contactType, contactId);
    }

    /**
     * 库商信息 -> 新增联系人
     *
     * @param sid                    学校id
     * @param contactPeopleInfoModel 联系人详情
     * @return 新增的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @Override
    public String addContactPeople(Long sid, ContactPeopleInfoModel contactPeopleInfoModel) {
        super.checkSid(sid);

        if (null == contactPeopleInfoModel) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        Long did = contactPeopleInfoModel.getDId();
        Integer contactType = contactPeopleInfoModel.getContactType();

        if (contactType == null || did == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        // 如果职务包括商务，那邮寄地址为必填项
        if (contactPeopleInfoModel.getPosition().contains(Constant.CompanyWrite.COMMERCE)
                && Strings.isNullOrEmpty(contactPeopleInfoModel.getAddress())) {
            throw new CustomException("请填写邮寄地址！");
        }

        DatabaseContactPeople model = new DatabaseContactPeople();

        // 复制并添加数据
        BeanUtils.copyProperties(contactPeopleInfoModel, model);
        model.setSId(sid);
        model.setDId(did);
        model.setType(contactType);
        model.setCreatedTime(LocalDateTime.now());

        // 新增联系人
        databaseContactPeopleDao.insert(model);
        return Vm.INSERT_SUCCESS;
    }

    /**
     * 库商信息 -> 编辑联系人
     *
     * @param sid                    学校id
     * @param contactPeopleInfoModel 联系人详情
     * @return 编辑的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @Override
    public String updateContactPeople(Long sid, ContactPeopleInfoModel contactPeopleInfoModel) {
        super.checkSid(sid);

        if (null == contactPeopleInfoModel || null == contactPeopleInfoModel.getContactId()) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        Long contactId = contactPeopleInfoModel.getContactId();
        Long did = contactPeopleInfoModel.getDId();
        Integer contactType = contactPeopleInfoModel.getContactType();

        if (contactId == null || did == null || contactType == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        // 如果职务包括商务，那邮寄地址为必填项
        if (contactPeopleInfoModel.getPosition().contains(Constant.CompanyWrite.COMMERCE)
                && Strings.isNullOrEmpty(contactPeopleInfoModel.getAddress())) {
            throw new CustomException("请填写邮寄地址！");
        }

        // 查原有属性并修改
        DatabaseContactPeople model =
                databaseContactPeopleDao.findOneBySidDidContactTypeContactId(sid, did, contactType, contactId);
        if (model == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }
        contactPeopleInfoModel.setId(model.getId());
        BeanUtils.copyProperties(contactPeopleInfoModel, model);

        // 更新
        databaseContactPeopleDao.updateById(model);
        return Vm.UPDATE_SUCCESS;
    }

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
    @Override
    public String deleteContactPeople(Long sid, Long did, Long contactId, Integer contactType) {
        //  根据学校id、数据库id、联系人类型、联系人id，删除联系人
        databaseContactPeopleDao.deleteBySidDidContactTypeContactId(sid, did, contactType, contactId);
        return Vm.DELETE_SUCCESS;
    }
}
