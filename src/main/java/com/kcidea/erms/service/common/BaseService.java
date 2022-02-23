package com.kcidea.erms.service.common;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.util.RedisUtil;
import com.kcidea.erms.dao.database.DatabaseAttachmentDao;
import com.kcidea.erms.dao.database.DatabaseKeyDao;
import com.kcidea.erms.dao.ers.*;
import com.kcidea.erms.domain.database.DatabaseAttachment;
import com.kcidea.erms.domain.database.DatabaseKey;
import com.kcidea.erms.domain.ers.Agent;
import com.kcidea.erms.domain.ers.Company;
import com.kcidea.erms.domain.ers.Subject;
import com.kcidea.erms.domain.ers.VdatabasePropertyRel;
import com.kcidea.erms.enums.database.EnumAttachmentType;
import com.kcidea.erms.enums.database.EnumCheckState;
import com.kcidea.erms.enums.database.EnumLanguageType;
import com.kcidea.erms.enums.database.EnumPaperType;
import com.kcidea.erms.enums.subject.EnumSubjectCategoryType;
import com.kcidea.erms.model.common.IdLongModel;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.database.DatabasePropertyInfoModel;
import com.kcidea.erms.model.database.DatabasePropertyModel;
import com.kcidea.erms.model.subject.SubjectAndCategoryModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/12
 **/
public abstract class BaseService {

    @Resource
    private CompanyDao companyDao;

    @Resource
    private AgentDao agentDao;

    @Resource
    private VdatabasePropertyRelDao vdatabasePropertyRelDao;

    @Resource
    private SchoolDatabaseSubjectRelDao schoolDatabaseSubjectRelDao;

    @Resource
    private SubjectDao subjectDao;

    @Resource
    private VdatabaseDao vdatabaseDao;

    @Resource
    private DatabaseKeyDao databaseKeyDao;

    @Resource
    private DatabaseAttachmentDao databaseAttachmentDao;

    @Value("${my-config.file-url}")
    private String FILE_URL;

    /**
     * 校验分页参数
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @author yeweiwei
     * @date 2021/11/12 19:36
     */
    protected void checkPageParam(Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum <= 0) {
            throw new CustomException("很抱歉，传参错误，页码必须大于0");
        }

        if (pageSize == null || pageSize <= 0) {
            throw new CustomException("很抱歉，传参错误，每页数量必须大于0");
        }

        if (pageSize > Constant.Page.MAX_PAGE_SIZE) {
            throw new CustomException("很抱歉，传参错误，每页数量不能超过100条");
        }
    }

    /**
     * 校验学校id
     *
     * @param sid 学校id
     * @author yeweiwei
     * @date 2021/11/18 17:28
     */
    protected void checkSid(Long sid) {
        if (null == sid || 0L == sid) {
            throw new CustomException(Vm.NO_LOGIN);
        }
    }

    /**
     * 校验数据库id
     *
     * @param did 数据库id
     * @author majuehao
     * @date 2021/11/30 14:48
     **/
    protected void checkDid(Long did) {
        if (null == did || 0L == did) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }
    }

    /**
     * 校验操作人id
     *
     * @param userId 操作人id
     * @author majuehao
     * @date 2021/11/18 17:28
     */
    protected void checkUserId(Long userId) {
        if (null == userId || 0L == userId) {
            throw new CustomException(Vm.NO_LOGIN);
        }
    }

    /**
     * 校验学校id和操作人id
     *
     * @param sid    学校id
     * @param userId 操作人id
     * @author yeweiwei
     * @date 2021/11/26 8:48
     */
    protected void checkSidUserId(Long sid, Long userId) {
        this.checkSid(sid);
        this.checkUserId(userId);
    }

    /**
     * 校验学校id和数据库id
     *
     * @param sid 学校id
     * @param did 数据库id
     * @author majuehao
     * @date 2021/11/30 14:48
     **/
    protected void checkSidDid(Long sid, Long did) {
        this.checkSid(sid);
        this.checkDid(did);
    }

    /**
     * 校验学校id和数据库id和用户id
     *
     * @param sid    学校id
     * @param did    数据库id
     * @param userId 操作人id
     * @author majuehao
     * @date 2021/11/30 14:48
     **/
    protected void checkSidDidUserId(Long sid, Long did, Long userId) {
        this.checkSid(sid);
        this.checkDid(did);
        this.checkUserId(userId);
    }

    /**
     * 获取供应商数据
     *
     * @param companyId  供应商id
     * @param companyMap 供应商数据
     * @return 供应商名称
     * @author majuehao
     * @date 2021/11/30 15:35
     **/
    protected String findCompanyName(Long companyId, Map<Long, Company> companyMap) {

        String companyName = "";

        //获取出版社数据
        if (companyId == null || companyId <= 0) {
            return companyName;
        }

        Company company;
        if (companyMap.containsKey(companyId)) {
            company = companyMap.get(companyId);
        } else {
            company = companyDao.selectById(companyId);
        }

        if (company != null) {
            companyName = company.getName();
            companyMap.put(companyId, company);
        }

        return companyName;
    }

    /**
     * 获取代理商数据
     *
     * @param agentId  代理商id
     * @param agentMap 代理商数据
     * @return 代理商名称
     * @author majuehao
     * @date 2021/11/30 15:35
     **/
    protected String findAgentName(Long agentId, Map<Long, Agent> agentMap) {

        String agentName = "";

        //获取出版社数据
        if (agentId == null || agentId <= 0) {
            return agentName;
        }

        Agent agent;
        if (agentMap.containsKey(agentId)) {
            agent = agentMap.get(agentId);
        } else {
            agent = agentDao.selectById(agentId);
        }

        if (agent != null) {
            agentName = agent.getName();
            agentMap.put(agentId, agent);
        }

        return agentName;
    }

    /**
     * 根据学校id、数据库id，获得对应数据库的属性集
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库的属性集
     * @author majuehao
     * @date 2021/11/30 16:28
     **/
    protected DatabasePropertyModel findDatabaseProperty(Long sid, Long did) {
        this.checkSidDid(sid, did);

        // 返回的属性集
        DatabasePropertyModel model = new DatabasePropertyModel();

        // 数据库的属性
        String properties = "";

        // 数据库属性的id集合
        Set<Long> propertiesSet = Sets.newHashSet();

        // 数据库的语种
        String language = "";

        // 数据库的纸电
        String paperFlag = "";

        List<DatabasePropertyInfoModel> propertyList = vdatabasePropertyRelDao.findListBySidDid(sid, did);
        if (propertyList != null) {
            //如果有学校自定义的属性，就使用学校自定义的
            if (propertyList.stream().anyMatch(s -> s.getSid() != null && s.getSid().equals(sid))) {
                propertyList = propertyList.stream()
                        .filter(s -> s.getSid() != null && s.getSid().equals(sid)).collect(Collectors.toList());
            }

            if (!CollectionUtils.isEmpty(propertyList)) {
                propertiesSet = propertyList.stream().map(DatabasePropertyInfoModel::getPropertyId)
                        .collect(Collectors.toSet());
                properties = propertyList.stream().map(DatabasePropertyInfoModel::getPropertyName)
                        .collect(Collectors.joining(Constant.SplitChar.SEMICOLON_CHAR));

                Long languageId = propertyList.get(0).getLanguageId();
                EnumLanguageType enumLanguageType = EnumLanguageType.getLanguageType(languageId);
                language = enumLanguageType == null ? "" : enumLanguageType.getName();

                Integer paperFlagId = propertyList.get(0).getPaperFlag();
                EnumPaperType enumPaperType = EnumPaperType.getPaperType(paperFlagId);
                paperFlag = enumPaperType == null ? "" : enumPaperType.getName();
            }
        }

        model.setProperties(properties);
        model.setPropertiesSet(propertiesSet);
        model.setLanguage(language);
        model.setPaperFlag(paperFlag);
        return model;
    }

    /**
     * 查询数据库的学科
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库的学科id
     * @author huxubin
     * @date 2022/1/19 16:56
     */
    protected List<Long> findDatabaseSubjectIdList(Long sid, Long did) {
        Set<Long> subjectIdSet = schoolDatabaseSubjectRelDao.findSubjectIdSetBySidDid(sid, did);
        return Lists.newArrayList(subjectIdSet);
    }

    /**
     * 查询
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库的学科
     * @author huxubin
     * @date 2022/1/19 16:46
     */
    protected SubjectAndCategoryModel findDatabaseSubjects(Long sid, Long did) {
        Set<Long> subjectIdSet = schoolDatabaseSubjectRelDao.findSubjectIdSetBySidDid(sid, did);
        return this.findSubjectAndCategoryModelBySubjectIdSet(subjectIdSet);
    }

    /**
     * 查询教育部基础学科名称集合，添加综合学科
     *
     * @author majuehao
     * @date 2022/1/18 12:59
     **/
    protected Map<Long, Subject> findSubjectMap(EnumSubjectCategoryType categoryType) {

        String redisKey = Constant.RedisKey.SUBJECT_KEY.concat(Integer.toString(categoryType.getValue()));

        //学科列表
        List<Subject> subjectList;

        //优先从缓存中取，找不到再去查询数据库
        String json = RedisUtil.getStringByKey(redisKey);

        //如果缓存中没有数据，再去查询数据库
        if (!Strings.isNullOrEmpty(json)) {
            subjectList = JSON.parseArray(json, Subject.class);
        } else {
            // 查询教育部学科名称集合
            subjectList = subjectDao.findListByCategoryType(categoryType.getValue());
            //综合学科
            Subject subject = new Subject().createComprehensive();
            subjectList.add(subject);
            RedisUtil.setValueAndTime(redisKey, JSON.toJSONString(subjectList), Constant.RedisTime.DEFAULT);
        }

        return subjectList.stream().collect((Collectors.toMap(Subject::getId, a -> a, (k1, k2) -> k1)));
    }

    /**
     * 组装学科数据
     *
     * @param subjectIdSet 学科id列表
     * @return 一级学科和门类学科
     * @author majuehao
     * @date 2022/1/17 8:59
     **/
    protected SubjectAndCategoryModel findSubjectAndCategoryModelBySubjectIdSet(Set<Long> subjectIdSet) {
        // 查询教育部基础学科名称集合，添加综合学科
        Map<Long, Subject> subjectMap = this.findSubjectMap(EnumSubjectCategoryType.教育部学科);

        //学科门类数据
        Set<String> subjectCategoryNameSet = Sets.newHashSet();

        //学科门类id
        Set<Long> subjectCategoryIdSet = Sets.newHashSet();

        //一级学科数据
        Set<String> subjectOneNameSet = Sets.newHashSet();

        //一级学科id
        Set<Long> subjectOneIdSet = Sets.newHashSet();

        //循环学科进行处理
        for (Long subjectId : subjectIdSet) {
            Subject subject = subjectMap.get(subjectId);
            if (subject == null) {
                continue;
            }

            int treeLevel = subject.getTreeLevel();

            //先获取门类名称
            Subject categorySubject = subjectMap.get(subject.getSubjectCategory());
            if (categorySubject != null) {
                subjectCategoryIdSet.add(subject.getSubjectCategory());
                subjectCategoryNameSet.add(categorySubject.getName());
            }

            //如果当前学科层级是1，那他自己就是一级学科，学科名称就是自己
            if (treeLevel == 1) {
                subjectOneIdSet.add(subjectId);
                subjectOneNameSet.add(subject.getName());
            } else if (treeLevel == 2) {
                //如果是二级学科，他的父学科id就是一级学科
                Subject oneSubject = subjectMap.get(subject.getParentId());
                if (oneSubject != null) {
                    subjectOneNameSet.add(oneSubject.getName());
                    subjectOneIdSet.add(oneSubject.getId());
                }
            }
        }

        //组装数据返回
        SubjectAndCategoryModel model = new SubjectAndCategoryModel();
        model.create(String.join(Constant.SplitChar.SEMICOLON_CHAR, subjectCategoryNameSet), subjectCategoryIdSet,
                String.join(Constant.SplitChar.SEMICOLON_CHAR, subjectOneNameSet), subjectOneIdSet);
        return model;
    }

    /**
     * 查询符合数据库属性、数据库语种数据，优先学校沙箱数据库
     *
     * @param sid        学校id
     * @param propertyId 数据库属性
     * @param languageId 数据库语种
     * @author majuehao
     * @date 2021/12/6 15:07
     **/
    protected Set<Long> getDidSetBySidPropertyIdLanguageId(Long sid, Long propertyId, Long languageId) {
        this.checkSid(sid);
        Set<Long> didSet = Sets.newHashSet();
        List<VdatabasePropertyRel> relList = vdatabasePropertyRelDao.findListBySid(sid);
        Map<Long, List<VdatabasePropertyRel>> relMap =
                relList.stream().collect(Collectors.groupingBy(VdatabasePropertyRel::getDId));
        for (Map.Entry<Long, List<VdatabasePropertyRel>> entry : relMap.entrySet()) {
            Long did = entry.getKey();
            List<VdatabasePropertyRel> dbRelList = entry.getValue();
            // 如果有学校自定义的属性，筛选出自定义的
            if (dbRelList.stream().anyMatch(s -> s.getSId() != null && s.getSId().equals(sid))) {
                dbRelList = dbRelList.stream()
                        .filter(s -> s.getSId() != null && s.getSId().equals(sid)).collect(Collectors.toList());
            }
            // 如果属性列表中有符合条件的，加到id列表中
            if (!languageId.equals(Constant.ALL_LONG_VALUE) && !propertyId.equals(Constant.ALL_LONG_VALUE)) {
                if (dbRelList.stream().anyMatch(s -> s.getLanguageId().equals(languageId)
                        && s.getPropertyId().equals(propertyId))) {
                    didSet.add(did);
                }
            } else if (!propertyId.equals(Constant.ALL_LONG_VALUE)) {
                if (dbRelList.stream().anyMatch(s -> s.getPropertyId().equals(propertyId))) {
                    didSet.add(did);
                }
            } else if (!languageId.equals(Constant.ALL_LONG_VALUE)) {
                if (dbRelList.stream().anyMatch(s -> s.getLanguageId().equals(languageId))) {
                    didSet.add(did);
                }
            }
        }
        return didSet;
    }

    /**
     * 查询符合学校id、学科id的数据库
     *
     * @param sid       学校id
     * @param subjectId 学科id
     * @return 数据库id集合
     * @author majuehao
     * @date 2021/11/30 16:28
     **/
    protected Set<Long> getDidSetBySidSubject(Long sid, Long subjectId) {
        checkSid(sid);

        // 返回集合
        Set<Long> didSet;
        Subject subject;

        // 处理综合学科
        if (subjectId.equals(Constant.Subject.COMPREHENSIVE_SUBJECT_LONG)) {
            subject = new Subject().setId(Constant.Subject.COMPREHENSIVE_SUBJECT_LONG)
                    .setSubjectCategory(Constant.Subject.COMPREHENSIVE_SUBJECT_LONG)
                    .setTreeLevel(1).setName(Constant.Subject.COMPREHENSIVE_SUBJECT_STRING).setParentId(0L);
        } else {
            // 根据学科id查询学科集合
            subject = subjectDao.selectById(subjectId);
        }

        if (subject == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        if (subject.getTreeLevel() == 0) {
            // 如果是学科门类，查询学科覆盖了此门类任意学科的数据库id集合
            Set<Long> subjectIdSet = subjectDao.findIdSetBySubjectCategory(subjectId);
            List<IdLongModel> didSubjectIdList = schoolDatabaseSubjectRelDao.findListBySid(sid);
            didSet = didSubjectIdList.stream().filter(s -> subjectIdSet.contains(s.getValue()))
                    .map(IdLongModel::getId).collect(Collectors.toSet());
        } else {
            // 一级学科或者通用学科直接查询即可
            didSet = schoolDatabaseSubjectRelDao.findDidSetBySidSubjectId(sid, subjectId);
        }

        return didSet;
    }


    /**
     * 根据学校id查询数据库名称，优先使用别名
     *
     * @param sid 学校id
     * @return 数据库id、名称集合
     * @author majuehao
     * @date 2021/12/6 17:01
     **/
    protected Map<Long, String> getDidNameMapBySid(Long sid) {
        List<IdNameModel> didNameList = vdatabaseDao.findDidNameListBySid(sid);
        return didNameList.stream().collect(Collectors.toMap(IdNameModel::getId, IdNameModel::getName));
    }

    /**
     * 将金额处理成万元
     *
     * @param money 金额
     * @return 以万元为单位
     * @author majuehao
     * @date 2021/12/6 17:26
     **/
    protected BigDecimal moneyIntoTenThousand(BigDecimal money) {
        if (money == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(
                money.divide(Constant.Price.TEN_THOUSAND, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
    }

    /**
     * 根据sid、did，获取数据库名称
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库名称
     * @author majuehao
     * @date 2021/12/20 16:48
     **/
    protected String findDatabaseName(Long sid, Long did) {
        checkSid(sid);
        if (did == null) {
            return "";
        }

        //查询数据库名称
        IdNameModel model = vdatabaseDao.findDidNameListBySidDid(sid, did);

        if (model != null) {
            return model.getName();
        }

        return "";
    }

    /**
     * 获得数据库id名称集合
     *
     * @param didSet 数据库id集合
     * @param sid    学校id
     * @return 数据库id名称集合
     * @author yeweiwei
     * @date 2021/12/8 13:39
     */
    protected List<IdNameModel> findDatabaseIdNameList(Set<Long> didSet, Long sid) {
        checkSid(sid);

        // 全部选项
        List<IdNameModel> list = Lists.newArrayList(new IdNameModel().create(Constant.ALL_LONG_VALUE, "全部"));

        // 判断
        if (CollectionUtils.isEmpty(didSet)) {
            return list;
        }

        //查询数据库名称
        List<IdNameModel> didNameList = vdatabaseDao.findDidNameListBySid(sid);
        list.addAll(didNameList.stream().filter(s -> didSet.contains(s.getId())).collect(Collectors.toList()));

        return list;
    }

    /**
     * 判断对象中属性值是否全为空
     *
     * @param object 对象
     * @return 是否全为空
     * @author majuehao
     * @date 2021/12/9 16:11
     **/
    protected boolean checkObjAllFieldsIsNull(Object object) {
        // 如果对象为空，直接返回
        if (null == object) {
            return true;
        }
        try {
            for (Field f : object.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                // 排除序列号这一属性
                if (f.getName().equals(Constant.SERIAL_VERSION_UID)) {
                    continue;
                }
                // 属性不为空且不为"",直接返回false
                if (f.get(object) != null && StringUtils.isNotBlank(f.get(object).toString())) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 根据apiKey获取学校id、数据库id
     *
     * @param apiKey apiKey
     * @return 学校id、数据库id
     * @author majuehao
     * @date 2021/12/29 11:19
     **/
    protected DatabaseKey findOneByApiKey(String apiKey) {
        if (Strings.isNullOrEmpty(apiKey)) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        DatabaseKey databaseKey = databaseKeyDao.findOneByApiKey(apiKey);
        if (databaseKey == null) {
            throw new CustomException("链接已经失效，请联系老师获取最新的填写链接");
        }

        return databaseKey;
    }

    /**
     * 查询数据库的apiKey
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 查询的结果
     * @author huxubin
     * @date 2022/1/5 13:58
     */
    protected String findDataBaseApiKeyBySidDid(Long sid, Long did) {
        return this.findOneDataBaseKeyBySidDid(sid, did).getApiKey();
    }

    /**
     * 根据学校id和数据库id查询apiKey
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 查询的结果
     * @author huxubin
     * @date 2022/1/13 14:10
     */
    protected DatabaseKey findOneDataBaseKeyBySidDid(Long sid, Long did) {
        //根据学校id、数据库id查询apiKey
        DatabaseKey key = databaseKeyDao.findOneBySidDid(sid, did);
        //如果key不存在，就直接新增
        if (key == null) {
            key = new DatabaseKey();
            key.create(sid, did, UUID.randomUUID().toString().replace("-", ""), LocalDateTime.now());
            databaseKeyDao.insert(key);
        }
        return key;
    }

    /**
     * 根据学校id查询数据库的名称
     *
     * @param sid 学校id
     * @return 所有的数据库名称，有别名优先使用别名
     * @author huxubin
     * @date 2022/1/14 14:49
     */
    protected Map<Long, IdNameModel> findDatabaseNameMap(Long sid) {
        List<IdNameModel> didNameList = vdatabaseDao.findDidNameListBySid(sid);
        return didNameList.stream().collect((Collectors.toMap(IdNameModel::getId, a -> a, (k1, k2) -> k1)));
    }

    /**
     * 校验审核数据的审核状态
     *
     * @param beforeState 审核前的状态
     * @param afterState  审核后的状态
     * @author majuehao
     * @date 2022/1/14 14:49
     */
    protected void checkCheckState(Integer beforeState, Integer afterState) {
        if (null == beforeState || null == afterState) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        if (!afterState.equals(EnumCheckState.审核通过.getValue()) &&
                !afterState.equals(EnumCheckState.审核未通过.getValue())) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        if (!beforeState.equals(EnumCheckState.待审核.getValue())) {
            throw new CustomException("不是待审核状态，无法审核！");
        }
    }

    /**
     * 将附件转正字符串输出
     *
     * @param sid            学校id
     * @param uniqueId       表格id
     * @param attachmentType 附件类型
     * @return java.lang.String
     * @author majuehao
     * @date 2022/1/24 10:14
     **/
    protected String attachmentToString(Long sid, Long uniqueId, EnumAttachmentType attachmentType) {
        checkSid(sid);

        StringBuilder attachListBuilder = new StringBuilder();

        if (uniqueId == null || attachmentType == null) {
            return attachListBuilder.toString();
        }

        // 查询附件列表
        List<DatabaseAttachment> attachList = databaseAttachmentDao.findListBySidUniqueIdType(
                sid, uniqueId, attachmentType.getValue());

        // 列表转String
        for (DatabaseAttachment attachment : attachList) {
            String filePath = attachment.getFilePath();
            attachListBuilder.append(Constant.SplitChar.LINE_FEED_CHAR)
                    .append(FILE_URL).append(filePath);
        }

        return attachListBuilder.toString();
    }


}