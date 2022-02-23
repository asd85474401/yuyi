package com.kcidea.erms.service.database.impl;

import com.alibaba.excel.EasyExcel;
import com.google.common.collect.Lists;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.util.DownloadUtil;
import com.kcidea.erms.common.util.MinioFileUtil;
import com.kcidea.erms.dao.database.DatabaseAttachmentDao;
import com.kcidea.erms.dao.database.DatabaseBaseInfoDao;
import com.kcidea.erms.dao.database.DatabaseEvaluationDao;
import com.kcidea.erms.dao.ers.*;
import com.kcidea.erms.domain.database.DatabaseAttachment;
import com.kcidea.erms.domain.database.DatabaseBaseInfo;
import com.kcidea.erms.domain.database.DatabaseEvaluation;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import com.kcidea.erms.enums.database.EnumAttachmentType;
import com.kcidea.erms.enums.database.EnumEvaluationResult;
import com.kcidea.erms.enums.database.EnumLanguageType;
import com.kcidea.erms.enums.database.EnumLoginType;
import com.kcidea.erms.enums.fund.EnumOrderType;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.database.AttachmentModel;
import com.kcidea.erms.model.database.DatabasePropertyModel;
import com.kcidea.erms.model.database.evaluation.*;
import com.kcidea.erms.model.database.detail.DatabaseInfoModel;
import com.kcidea.erms.model.subject.SubjectAndCategoryModel;
import com.kcidea.erms.service.common.BaseService;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/24
 **/
@Slf4j
@Service
public class DatabaseEvaluationServiceImpl extends BaseService implements DatabaseEvaluationService {

    @Value("${my-config.temp-path}")
    private String tempPath;

    @Resource
    private DatabaseEvaluationDao databaseEvaluationDao;

    @Resource
    private VdatabaseDao vdatabaseDao;

    @Resource
    private DatabaseService databaseService;

    @Resource
    private SchoolDatabaseSubjectRelDao schoolDatabaseSubjectRelDao;

    @Resource
    private AgentDao agentDao;

    @Resource
    private CompanyDao companyDao;

    @Resource
    private DatabaseBaseInfoDao databaseBaseInfoDao;

    @Resource
    private SchoolDatabaseSubjectRelService schoolDatabaseSubjectRelService;

    @Resource
    private DatabaseAttachmentDao databaseAttachmentDao;


    /**
     * 查询学校有评估的数据库下拉框集合
     *
     * @param sid   学校id
     * @param vYear 年份
     * @return 数据库下拉集合
     * @author yeweiwei
     * @date 2021/11/24 14:21
     */
    @Override
    public List<IdNameModel> findDatabaseSelectList(Long sid, Integer vYear) {
        super.checkSid(sid);

        //根据sid查询数据库id集合
        Set<Long> didSet = databaseEvaluationDao.findDidSetBySidYear(sid, vYear);

        return super.findDatabaseIdNameList(didSet, sid);
    }

    /**
     * 查询评估列表
     *
     * @param sid          学校id
     * @param vYear        年份
     * @param did          数据库id
     * @param language     语种
     * @param fulltextFlag 全文标识
     * @param type         资源类型
     * @param resultType   评估结果
     * @param pageNum      页码
     * @param pageSize     每页数量
     * @return 评估列表
     * @author yeweiwei
     * @date 2021/11/24 15:26
     */
    @Override
    public PageResult<DatabaseEvaluationInfoModel> findDatabaseEvaluationPage(Long sid, Integer vYear, Long did,
                                                                              Long language, Integer fulltextFlag,
                                                                              Long type, Integer resultType,
                                                                              Integer pageNum, Integer pageSize) {
        super.checkPageParam(pageNum, pageSize);
        super.checkSid(sid);

        //按条件筛选评估
        List<DatabaseEvaluationInfoModel> modelList = getDatabaseEvaluationModelList(sid, vYear,
                did, language, fulltextFlag, type, resultType);

        int total = modelList.size();

        //分页
        modelList = modelList.stream().skip(Math.min(pageNum * pageSize - pageSize, total))
                .limit(pageSize).collect(Collectors.toList());

        // 查询数据库名称
        Map<Long, String> dbIdNameMap = super.getDidNameMapBySid(sid);

        List<DatabaseEvaluationInfoModel> list = getDatabaseEvaluationPageModels(sid, modelList, dbIdNameMap);

        PageResult<DatabaseEvaluationInfoModel> result = new PageResult<>();
        result.success(list, total);
        return result;
    }

    /**
     * 查询评估列表
     *
     * @param sid          学校id
     * @param vYear        年份
     * @param did          数据库id
     * @param language     语种
     * @param fulltextFlag 全文标识
     * @param type         资源类型
     * @param resultType   评估结果
     * @return 评估列表
     * @author yeweiwei
     * @date 2021/12/27 13:13
     */
    private List<DatabaseEvaluationInfoModel> getDatabaseEvaluationModelList(Long sid, Integer vYear, Long did,
                                                                             Long language, Integer fulltextFlag, Long type,
                                                                             Integer resultType) {
        List<DatabaseEvaluationInfoModel> modelList = databaseEvaluationDao
                .findListBySidDidYearResultType(sid, did, vYear, fulltextFlag, resultType);

        if (!language.equals(Constant.ALL_LONG_VALUE) || !type.equals(Constant.ALL_LONG_VALUE)) {
            //查询符合语种、资源类型的数据库id，筛选
            Set<Long> didSet = super.getDidSetBySidPropertyIdLanguageId(sid, type, language);
            modelList = modelList.stream().filter(s -> didSet.contains(s.getDid())).collect(Collectors.toList());
        }

        return modelList;
    }

    /**
     * 删除数据库评估
     *
     * @param id  数据库评估id
     * @param sid 学校id
     * @return 删除的结果
     * @author yeweiwei
     * @date 2021/11/24 18:46
     */
    @Override
    public String deleteDatabaseEvaluation(Long id, Long sid) {
        checkSid(sid);

        if (null == id) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        //如果是评估通过的，则不允许删除
        DatabaseEvaluation evaluationModel = databaseEvaluationDao.selectById(id);
        if (evaluationModel.getResultType() == EnumEvaluationResult.通过.getValue()) {
            throw new CustomException("很抱歉，该数据库已通过评估，无法删除！");
        }

        databaseEvaluationDao.deleteByIdSid(id, sid);

        return Vm.DELETE_SUCCESS;
    }

    /**
     * 新增数据库评估
     *
     * @param model  数据库评估
     * @param sid    学校id
     * @param userId 操作人id
     * @return 添加的结果
     * @author yeweiwei
     * @date 2021/11/26 8:43
     */
    @Override
    public String addDatabaseEvaluation(DatabaseEvaluationInfoModel model, Long sid, Long userId) {

        checkSidUserId(sid, userId);

        //根据年份和数据库id查询是否已有评估记录
        Long did = model.getDid();
        Integer vYear = model.getVyear();

        int count = databaseEvaluationDao.findCountBySidDidYear(sid, did, vYear);
        if (count > 0) {
            throw new CustomException("检测到您选择的数据库已存在评估记录，请重新选择");
        }

        // 更新数据库
        DatabaseInfoModel databaseInfoModel = model.getDatabaseInfoModel();
        databaseInfoModel.setDid(did);
        databaseService.updateDatabase(sid, databaseInfoModel, userId);

        //修改学科覆盖
        List<Long> subjectList = model.getSubjectList();
        schoolDatabaseSubjectRelService.updateDatabaseSubjectRel(sid, did, subjectList, userId);

        //新增数据库评估
        DatabaseEvaluation evaluation = new DatabaseEvaluation().create(sid, did, vYear,
                model.getOrderType(), EnumEvaluationResult.未评估.getValue(), userId, LocalDateTime.now());
        databaseEvaluationDao.insert(evaluation);

        //是否新增数据库代理商和数据库供应商
//        databaseService.addCompanyAndAgent(model, sid, userId);
//        databaseService.findCompanyId(model, sid, userId);
//        databaseService.findAgentId(model, sid, userId);
        //查看以前有没有评估信息
        DatabaseBaseInfo databaseBaseInfo = databaseBaseInfoDao.findOneBySidDid(sid, did);
        addOrUpdateDatabaseEvaluationInfo(model, sid, userId, did, databaseBaseInfo);

        return Vm.INSERT_SUCCESS;
    }

    /**
     * 修改数据库评估
     *
     * @param model  数据库评估
     * @param sid    学校id
     * @param userId 操作人id
     * @return 修改的结果
     * @author yeweiwei
     * @date 2021/11/26 13:44
     */
    @Override
    public String updateDatabaseEvaluation(DatabaseEvaluationInfoModel model, Long sid, Long userId) {
        super.checkSidUserId(sid, userId);

        Long evaluationId = model.getEvaluationId();
        if (null == evaluationId) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        Long did = model.getDid();

        // 更新数据库
        DatabaseInfoModel databaseInfoModel = model.getDatabaseInfoModel();
        databaseInfoModel.setDid(did);
        databaseService.updateDatabase(sid, databaseInfoModel, userId);

        //修改学科覆盖
        List<Long> subjectList = model.getSubjectList();
        schoolDatabaseSubjectRelService.updateDatabaseSubjectRel(sid, did, subjectList, userId);

        //是否新增数据库代理商和数据库供应商
        // TODO 删除
//        databaseService.addCompanyAndAgent(model, sid, userId);

        LocalDateTime now = LocalDateTime.now();
        //更新数据库评估
        DatabaseEvaluation evaluation = new DatabaseEvaluation();
        evaluation.setId(evaluationId).setOrderType(model.getOrderType()).setUpdatedBy(userId).setUpdatedTime(now);
        databaseEvaluationDao.updateById(evaluation);

        //更新评估信息
        DatabaseBaseInfo databaseBaseInfo = databaseBaseInfoDao.findOneBySidDid(sid, model.getDid());
        this.addOrUpdateDatabaseEvaluationInfo(model, sid, userId, did, databaseBaseInfo);

        return Vm.UPDATE_SUCCESS;
    }

    /**
     * 新增或编辑评估信息
     *
     * @param model            数据库评估模型
     * @param sid              学校id
     * @param userId           操作人id
     * @param did              数据库id
     * @param databaseBaseInfo 数据库基本信息
     * @author yeweiwei
     * @date 2021/12/13 13:35
     */
    private void addOrUpdateDatabaseEvaluationInfo(DatabaseEvaluationInfoModel model, Long sid, Long userId, Long did,
                                                   DatabaseBaseInfo databaseBaseInfo) {
        if (null == databaseBaseInfo) {
            //新增评估信息
            databaseBaseInfo = new DatabaseBaseInfo();
            BeanUtils.copyProperties(model, databaseBaseInfo);
            databaseBaseInfo.setSId(sid).setDId(did).setCreatedBy(userId).setCreatedTime(LocalDateTime.now());
            databaseBaseInfoDao.insert(databaseBaseInfo);
        } else {
            //更新
            BeanUtils.copyProperties(model, databaseBaseInfo);
            databaseBaseInfo.setSId(sid).setDId(did).setUpdatedBy(userId).setUpdatedTime(LocalDateTime.now());
            databaseBaseInfoDao.updateById(databaseBaseInfo);
        }
    }

    /**
     * 编辑回显
     *
     * @param id     数据库评估id
     * @param sid    学校id
     * @param userId 操作人id
     * @return 数据库评估信息
     * @author yeweiwei
     * @date 2021/11/29 14:20
     */
    @Override
    public DatabaseEvaluationInfoModel findOneDatabaseEvaluation(Long id, Long sid, Long userId) {
        DatabaseEvaluation model = databaseEvaluationDao.findOneById(id);

        if (null == model) {
            throw new CustomException(Vm.NO_DATA);
        }

        Long did = model.getDId();

        DatabaseEvaluationInfoModel infoModel = new DatabaseEvaluationInfoModel();
        BeanUtils.copyProperties(model, infoModel);
        infoModel.setEvaluationId(id);

        //数据库详情
        DatabaseInfoModel databaseInfo = databaseService.findDatabaseInfo(sid, did);
        infoModel.setDatabaseInfoModel(databaseInfo);

        //学科覆盖id集合
        SubjectAndCategoryModel subjectModel = super.findDatabaseSubjects(sid, did);
        infoModel.setSubjects(subjectModel.getSubject());
        infoModel.setSubjectList(super.findDatabaseSubjectIdList(sid, did));
        return infoModel;
    }

    /**
     * 导出评估列表
     *
     * @param sid          学校id
     * @param vYear        年份
     * @param did          数据库id
     * @param language     语种
     * @param fulltextFlag 全文标识
     * @param type         资源类型
     * @param resultType   评估结果
     * @return 导出文件
     * @author yeweiwei
     * @date 2021/11/29 14:41
     */
    @Override
    public ResponseEntity<byte[]> exportEvaluationList(Long sid, Integer vYear, Long did, Long language,
                                                       Integer fulltextFlag, Long type, Integer resultType) {
        super.checkSid(sid);

        // 导出的文件名称和路径
        String filePath = tempPath + System.currentTimeMillis();
        String fileName = "数据库评估列表";

        //先去评估表查询符合条件的
        List<DatabaseEvaluationInfoModel> modelList =
                getDatabaseEvaluationModelList(sid, vYear, did, language, fulltextFlag, type, resultType);

        //查询数据库名称
        List<IdNameModel> didNameList = vdatabaseDao.findDidNameListBySid(sid);
        Map<Long, String> dbIdNameMap = didNameList.stream()
                .collect(Collectors.toMap(IdNameModel::getId, IdNameModel::getName));

        List<DatabaseEvaluationInfoModel> list = getExportModelList(sid, modelList, dbIdNameMap);

        // 导出
        EasyExcel.write(filePath, DatabaseEvaluationInfoModel.class).sheet(fileName).doWrite(list);
        return DownloadUtil.getResponseEntityCanDeleteFile(filePath,
                fileName.concat(Constant.Suffix.XLSX_WITH_POINT), EnumTrueFalse.是.getValue());
    }

    /**
     * 数据库评估
     *
     * @param sid             学校id
     * @param userId          操作人id
     * @param evaluationModel 评估内容
     * @return 评估结果
     * @author yeweiwei
     * @date 2021/12/1 11:07
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String evaluateDatabase(Long sid, Long userId, EvaluationModel evaluationModel) {
        super.checkSidUserId(sid, userId);
        LocalDateTime now = LocalDateTime.now();

        Long evaluationId = evaluationModel.getEvaluationId();
        //更新评估表
        DatabaseEvaluation evaluation = new DatabaseEvaluation();
        evaluation.setId(evaluationId).setResultType(evaluationModel.getResultType()).setTime(now)
                .setRemark(evaluationModel.getRemark()).setUpdatedBy(userId).setUpdatedTime(now);
        databaseEvaluationDao.updateById(evaluation);

        //更新附件表
        updateAttachment(sid, evaluationId, EnumAttachmentType.评估附件.getValue(), evaluationModel.getAttachList(),
                userId);

        return Vm.EVALUATE_SUCCESS;
    }


    /**
     * 回显评估内容
     *
     * @param sid          学校id
     * @param evaluationId 评估id
     * @return 评估内容
     * @author yeweiwei
     * @date 2021/12/1 13:44
     */
    @Override
    public EvaluationModel findOneEvaluation(Long sid, Long evaluationId) {
        super.checkSid(sid);

        EvaluationModel model = new EvaluationModel();

        // 查询评估结果
        DatabaseEvaluation databaseEvaluation = databaseEvaluationDao.findOneById(evaluationId);
        Integer resultType = databaseEvaluation.getResultType();
        String remark = databaseEvaluation.getRemark();

        model.setEvaluationId(evaluationId).setResultType(resultType).setRemark(remark);

        // 查询附件表
        List<AttachmentModel> attachList =
                databaseAttachmentDao.findNamePathListBySidUniqueIdType(sid, evaluationId,
                        EnumAttachmentType.评估附件.getValue());
        model.setAttachList(attachList);
        return model;
    }

    /**
     * 更新附件表
     *
     * @param sid        学校id
     * @param uniqueId   表id
     * @param type       附件类型
     * @param attachList 要更新的附件列表
     * @param userId     用户id
     * @author majuehao
     * @date 2022/1/14 13:04
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAttachment(Long sid, Long uniqueId, Integer type, List<AttachmentModel> attachList, Long userId) {
        // 更新附件表
        List<DatabaseAttachment> attachmentModelList = databaseAttachmentDao.findListBySidUniqueIdType(sid, uniqueId,
                type);
        Map<String, DatabaseAttachment> filePathMap = attachmentModelList.stream().collect(Collectors
                .toMap(DatabaseAttachment::getFilePath, a -> a));
        // 更新附件表
        for (AttachmentModel attachmentModel : attachList) {
            String filePath = attachmentModel.getFilePath();
            if (filePathMap.containsKey(filePath)) {
                filePathMap.remove(filePath);
            } else {
                // 新增
                DatabaseAttachment attachment = new DatabaseAttachment().create(sid, uniqueId,
                        type, attachmentModel.getFileName(), filePath,
                        userId, LocalDateTime.now());
                databaseAttachmentDao.insert(attachment);
            }
        }

        //filePathMap中剩余的是以前上传的附件，已经不用了，删除
        if (!CollectionUtils.isEmpty(filePathMap)) {
            for (Map.Entry<String, DatabaseAttachment> entry : filePathMap.entrySet()) {
                DatabaseAttachment attachment = entry.getValue();
                databaseAttachmentDao.deleteById(attachment.getId());
                try {
                    MinioFileUtil.removeObject(Constant.MinIoBucketName.ERMS, attachment.getFilePath());
                } catch (Exception e) {
                    log.error("删除文件出错：" + e.getMessage(), e);
                    throw new CustomException(Vm.SYSTEM_ERROR_PLEASE_REFRESH_AND_RETRY);
                }
            }
        }
    }

    /**
     * 根据学校id、数据库id查询数据库评估信息
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库评估信息
     * @author yeweiwei
     * @date 2021/12/8 13:54
     */
    @Override
    public DatabaseEvaluationInfoModel findDatabaseEvaluationInfo(Long sid, Long did) {
        super.checkSid(sid);

        DatabaseInfoModel databaseInfo = databaseService.findDatabaseInfo(sid, did);

        DatabaseEvaluationInfoModel model = new DatabaseEvaluationInfoModel();
        model.setDatabaseInfoModel(databaseInfo);

        DatabaseBaseInfo databaseBaseInfo = databaseBaseInfoDao.findOneBySidDid(sid, did);
        if (databaseBaseInfo != null) {
            BeanUtils.copyProperties(databaseBaseInfo, model);
        }

        Set<Long> subjectSet = schoolDatabaseSubjectRelDao.findSubjectIdSetBySidDid(sid, did);
        model.setSubjectList(Lists.newArrayList(subjectSet));
        return model;
    }

    /**
     * 新增评估的数据库
     *
     * @param sid    学校id
     * @param userId 用户id
     * @param did    数据库id
     * @param vYear  年份
     * @return 新增的结果
     * @author huxubin
     * @date 2022/1/20 10:40
     */
    @Override
    public String addEvaluationDatabase(Long sid, Long userId, Long did, Integer vYear) {

        if (did == null || did <= 0) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        int count = databaseEvaluationDao.findCountBySidDidYear(sid, did, vYear);
        if (count > 0) {
            throw new CustomException("检测到".concat(Integer.toString(vYear).concat("年已存在数据库的评估记录，无法重复新增")));
        }

        //新增评估数据库
        DatabaseEvaluation databaseEvaluation = new DatabaseEvaluation();
        databaseEvaluation.create(sid, did, vYear, EnumOrderType.新订数据库.getValue(),
                EnumEvaluationResult.未评估.getValue(), userId, LocalDateTime.now());
        databaseEvaluationDao.insert(databaseEvaluation);
        return Vm.INSERT_SUCCESS;
    }

    /**
     * 获得导出的数据库评估集合
     *
     * @param sid         学校id
     * @param modelList   model集合
     * @param dbIdNameMap 数据库id名称的map集合
     * @return 数据库评估导出集合
     * @author yeweiwei
     * @date 2021/11/29 15:40
     */
    private List<DatabaseEvaluationInfoModel> getExportModelList(Long sid, List<DatabaseEvaluationInfoModel> modelList,
                                                                 Map<Long, String> dbIdNameMap) {
        //循环填充数据
        List<DatabaseEvaluationInfoModel> list = Lists.newArrayList();

        if (CollectionUtils.isEmpty(modelList)) {
            return list;
        }

        //查询所有的代理商
        List<IdNameModel> agentList = agentDao.findListBySid(sid);
        Map<Long, String> agentIdNameMap = agentList.stream()
                .collect(Collectors.toMap(IdNameModel::getId, IdNameModel::getName));

        //查询所有的数据商
        List<IdNameModel> companyList = companyDao.findListBySid(sid);
        Map<Long, String> companyIdNameMap = companyList.stream()
                .collect(Collectors.toMap(IdNameModel::getId, IdNameModel::getName));


        for (DatabaseEvaluationInfoModel model : modelList) {

            Long dId = model.getDid();
            DatabaseInfoModel databaseInfo = databaseService.findDatabaseInfo(sid, dId);

            String dName = dbIdNameMap.getOrDefault(dId, "");
            String loginType = "";
            if (null != model.getLoginType()) {
                EnumLoginType enumLoginType = EnumLoginType.getEnumLoginType(model.getLoginType());
                loginType = enumLoginType == null ? "" : enumLoginType.getName();
            }

            EnumEvaluationResult enumEvaluationResult =
                    EnumEvaluationResult.getEnumEvaluationResult(model.getResultType());
            String resultType = enumEvaluationResult == null ? "" : enumEvaluationResult.getName();

            EnumLanguageType enumLanguageType = EnumLanguageType.getLanguageType(databaseInfo.getLanguageId());
            String language = enumLanguageType == null ? "" : enumLanguageType.getName();
            String properties = databaseInfo.getProperties();

            String fulltextFlag = "";
            if (null != model.getFulltextFlag()) {
                EnumTrueFalse trueFalse = EnumTrueFalse.getTrueFalse(model.getFulltextFlag());
                fulltextFlag = trueFalse == null ? "" : trueFalse.getName();
            }

            //学科覆盖
            SubjectAndCategoryModel subjectAndCategoryModel = super.findDatabaseSubjects(sid, dId);
            //学科门类
            String subjectCategory = subjectAndCategoryModel.getSubjectCategory();
            //一级学科
            String subjectOne = subjectAndCategoryModel.getSubject();

            //评估附件，附件转字符串导出
            String attachmentStr = super.attachmentToString(sid, model.getId(), EnumAttachmentType.评估附件);

            DatabaseEvaluationInfoModel exportModel = new DatabaseEvaluationInfoModel();
            exportModel = exportModel.export(dName, language, properties, model.getAccessTips(),
                    subjectCategory, subjectOne, model.getDataTime(), companyIdNameMap.get(model.getCompanyId()),
                    agentIdNameMap.get(model.getAgentId()), loginType, resultType, attachmentStr,
                    fulltextFlag);
            list.add(exportModel);
        }
        return list;
    }

    /**
     * 构造数据库评估数据集合
     *
     * @param sid         学校id
     * @param modelList   数据库评估集合
     * @param dbIdNameMap 数据库id名称map
     * @return 数据库评估数据集合
     * @author yeweiwei
     * @date 2021/11/24 18:39
     */
    private List<DatabaseEvaluationInfoModel> getDatabaseEvaluationPageModels(Long sid,
                                                                              List<DatabaseEvaluationInfoModel> modelList,
                                                                              Map<Long, String> dbIdNameMap) {
        //循环填充数据
        List<DatabaseEvaluationInfoModel> list = Lists.newArrayList();

        if (CollectionUtils.isEmpty(modelList)) {
            return list;
        }

        for (DatabaseEvaluationInfoModel model : modelList) {

            Long dId = model.getDid();

            DatabaseEvaluationInfoModel pageModel = new DatabaseEvaluationInfoModel();
            pageModel.setId(model.getId());
            pageModel.setDid(dId);
            pageModel.setDName(dbIdNameMap.getOrDefault(dId, ""));
            pageModel.setFulltextFlag(model.getFulltextFlag());
            if (null != model.getLoginType()) {
                EnumLoginType enumLoginType = EnumLoginType.getEnumLoginType(model.getLoginType());
                pageModel.setLoginTypeName(enumLoginType == null ? "" : enumLoginType.getName());
            }

            pageModel.setResultType(model.getResultType());

            EnumEvaluationResult enumEvaluationResult =
                    EnumEvaluationResult.getEnumEvaluationResult(model.getResultType());
            pageModel.setResultTypeStr(enumEvaluationResult == null ? "" : enumEvaluationResult.getName());

            // 语种 资源类型
            DatabasePropertyModel databasePropertyModel = super.findDatabaseProperty(sid, dId);
            pageModel.setProperties(databasePropertyModel.getProperties());
            pageModel.setLanguage(databasePropertyModel.getLanguage());

            // 学科覆盖
            SubjectAndCategoryModel subjectAndCategoryModel = super.findDatabaseSubjects(sid, dId);
            //学科门类
            pageModel.setSubjectCategory(subjectAndCategoryModel.getSubjectCategory());
            //一级学科
            pageModel.setSubjectOne(subjectAndCategoryModel.getSubject());

            list.add(pageModel);
        }
        return list;
    }
}
