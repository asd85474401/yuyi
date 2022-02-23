package com.kcidea.erms.service.database.impl;

import com.google.common.collect.Maps;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.util.BeanListCopyUtils;
import com.kcidea.erms.common.util.FormatUtil;
import com.kcidea.erms.dao.database.*;
import com.kcidea.erms.dao.ers.VdatabaseDao;
import com.kcidea.erms.domain.database.DatabaseBaseInfo;
import com.kcidea.erms.domain.ers.Agent;
import com.kcidea.erms.domain.ers.Company;
import com.kcidea.erms.enums.database.EnumAttachmentType;
import com.kcidea.erms.enums.database.EnumEvaluationResult;
import com.kcidea.erms.model.company.AccessUrlInfoModel;
import com.kcidea.erms.model.company.ContactPeopleInfoModel;
import com.kcidea.erms.model.database.info.DatabaseInfoInsertModel;
import com.kcidea.erms.model.database.detail.DataBaseDetailModel;
import com.kcidea.erms.model.database.detail.DataBaseTitleDetailModel;
import com.kcidea.erms.model.database.detail.DatabaseSuShiModel;
import com.kcidea.erms.model.database.detail.DatabaseUsingStatisticsModel;
import com.kcidea.erms.model.database.AttachmentModel;
import com.kcidea.erms.model.database.evaluation.DatabaseEvaluationInfoModel;
import com.kcidea.erms.model.database.DatabasePropertyModel;
import com.kcidea.erms.model.database.AttachmentDontCheckModel;
import com.kcidea.erms.model.subject.SubjectAndCategoryModel;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.database.DataBaseDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/30
 **/
@Service
public class DataBaseDetailServiceImpl extends BaseService implements DataBaseDetailService {

    @Resource
    private VdatabaseDao vdatabaseDao;

    @Resource
    private DatabaseBaseInfoDao databaseBaseInfoDao;

    @Resource
    private DatabaseEvaluationDao databaseEvaluationDao;

    @Resource
    private DatabaseAttachmentDao databaseAttachmentDao;

    @Resource
    private DatabaseContactPeopleDao databaseContactPeopleDao;

    @Resource
    private DatabaseBuyInfoDao databaseBuyInfoDao;

    @Resource
    private DatabaseAccessUrlDao databaseAccessUrlDao;

    /**
     * 查询数据库标题栏信息
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库标题栏信息
     * @author majuehao
     * @date 2021/11/30 14:40
     **/
    @Override
    public DataBaseTitleDetailModel findDataBaseTitleDetail(Long sid, Long did) {
        // 校验参数
        super.checkSidDid(sid, did);

        // 查询数据
        DatabaseBaseInfo info = databaseBaseInfoDao.findOneBySidDid(sid, did);

        // 返回模型
        DataBaseTitleDetailModel model = new DataBaseTitleDetailModel();

        // 数据库id
        model.setDId(did);

        // 查询数据库名称
        model.setDName(super.findDatabaseName(sid, did));

        // 语种 资源类型
        DatabasePropertyModel databasePropertyModel = super.findDatabaseProperty(sid, did);
        model.setProperties(databasePropertyModel.getProperties());
        model.setLanguage(databasePropertyModel.getLanguage());

        if (info != null) {
            // 供应商
            Map<Long, Company> companyMap = Maps.newHashMap();
            model.setCompanyId(info.getCompanyId());
            model.setCompanyName(super.findCompanyName(info.getCompanyId(), companyMap));

            // 代理商
            Map<Long, Agent> agentMap = Maps.newHashMap();
            model.setAgentId(info.getAgentId());
            model.setAgentName(super.findAgentName(info.getAgentId(), agentMap));

            // 数据时间
            model.setArea(info.getArea());

            // 是否全文
            model.setFulltextFlag(FormatUtil.formatTrueFlagValue(info.getFulltextFlag()));
        }

        return model;
    }

    /**
     * 查询数据库信息
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库数据库信息
     * @author majuehao
     * @date 2021/11/30 14:40
     **/
    @Override
    public DataBaseDetailModel findDataBaseDetail(Long sid, Long did) {
        super.checkSidDid(sid, did);
        DataBaseDetailModel model = new DataBaseDetailModel();

        // 查询数据库基本信息、使用统计获取、sushi收割
        BeanUtils.copyProperties(getDataBaseBaseDetail(sid, did), model);
        // 查询数据库访问信息
        model.setAccessDetailModelList(findAccessDetailModelList(sid, did));
        // 查询供应商联系人信息
        model.setCompanyContactList(findDatabaseContactPeopleList(sid, did, Constant.CompanyWrite.COMPANY_CONTACT));
        // 查询代理商联系人信息
        model.setAgentContactList(findDatabaseContactPeopleList(sid, did, Constant.CompanyWrite.AGENT_CONTACT));
        // 查询内容介绍
        model.setRemark(getDataBaseIntroduction(sid, did));

        return model;
    }

    /**
     * 查询数据库基本信息、使用统计获取、sushi收割
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库基本信息
     * @author majuehao
     * @date 2021/11/30 14:40
     **/
    private DataBaseDetailModel getDataBaseBaseDetail(Long sid, Long did) {
        // 校验参数
        super.checkSidDid(sid, did);

        // 查询基本信息
        DatabaseBaseInfo info = databaseBaseInfoDao.findOneBySidDid(sid, did);

        // 返回模型
        DataBaseDetailModel model = new DataBaseDetailModel();
        // 基本信息
        DatabaseInfoInsertModel baseModel = new DatabaseInfoInsertModel();
        // 使用统计
        DatabaseUsingStatisticsModel usingStatisticsModel = new DatabaseUsingStatisticsModel();
        // sushi
        DatabaseSuShiModel suShiModel = new DatabaseSuShiModel();

        if (info == null) {
            return model;
        }

        // 数据库基本信息
        SubjectAndCategoryModel subjectAndCategoryModel = super.findDatabaseSubjects(sid, did);
        // 学科门类
        baseModel.setCategorySubject(subjectAndCategoryModel.getSubjectCategory());
        // 一级学科
        baseModel.setOneSubject(subjectAndCategoryModel.getSubject());
        // 使用指南
        baseModel.setAttachList(BeanListCopyUtils.copyListProperties(databaseAttachmentDao.
                        findNamePathListBySidUniqueIdType(sid, info.getId(), EnumAttachmentType.使用指南.getValue())
                , AttachmentDontCheckModel::new));
        // 复制的属性：数据库性质、数据时间、资源总量、资源容量、更新频率、检索功能、并发数
        BeanUtils.copyProperties(info, baseModel);

        // 组装model返回
        model.setDataBaseBaseDetailModel(baseModel);
        model.setUsingStatisticsModel(checkObjAllFieldsIsNull(usingStatisticsModel) ? null : usingStatisticsModel);
        model.setSuShiModel(checkObjAllFieldsIsNull(suShiModel) ? null : suShiModel);
        return model;
    }

    /**
     * 查询数据库访问信息列表
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库访问信息列表
     * @author majuehao
     * @date 2021/11/30 14:40
     **/
    @Override
    public List<AccessUrlInfoModel> findAccessDetailModelList(Long sid, Long did) {
        // 查询访问信息并返回
        return databaseAccessUrlDao.findListBySidDid(sid, did);
    }

    /**
     * 查询数据库联系人信息
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库联系人信息
     * @author majuehao
     * @date 2021/11/30 14:40
     **/
    @Override
    public List<ContactPeopleInfoModel> findDatabaseContactPeopleList(Long sid, Long did, Integer peopleType) {
        // 查询供应商联系人信息
        return databaseContactPeopleDao.findListBySidDidType(sid, did, peopleType);
    }


    /**
     * 查询数据库内容介绍
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 内容介绍
     * @author majuehao
     * @date 2021/11/30 14:40
     **/
    private String getDataBaseIntroduction(Long sid, Long did) {
        // 数据库介绍
        DatabaseBaseInfo databaseBaseInfo = databaseBaseInfoDao.findOneBySidDid(sid, did);
        return databaseBaseInfo == null ? "" : databaseBaseInfo.getIntroduce();
    }

    /**
     * 查询数据库历年评估记录
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 历年评估记录
     * @author majuehao
     * @date 2021/11/30 14:40
     **/
    @Override
    public List<DatabaseEvaluationInfoModel> findDataBaseEvaluationList(Long sid, Long did) {
        // 查询历年评估记录
        List<DatabaseEvaluationInfoModel> list = databaseEvaluationDao.findListBySidDid(sid, did);

        for (DatabaseEvaluationInfoModel model : list) {
            EnumEvaluationResult enumEvaluationResult = EnumEvaluationResult.getEnumEvaluationResult(model.getResultType());
            model.setResultTypeName(enumEvaluationResult == null ? "" : enumEvaluationResult.getName());
            List<AttachmentModel> attachments = databaseAttachmentDao.findNamePathListBySidUniqueIdType(sid, model.getId(),
                    EnumAttachmentType.评估附件.getValue());
            model.setAttachmentModelList(attachments);
        }

        return list;
    }
}
