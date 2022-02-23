package com.kcidea.erms.service.feedback.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.util.FormatUtil;
import com.kcidea.erms.common.util.IpUtil;
import com.kcidea.erms.dao.ers.SchoolDatabaseListDao;
import com.kcidea.erms.dao.feedback.QuestionFeedbackDao;
import com.kcidea.erms.dao.feedback.QuestionFeedbackTypeDao;
import com.kcidea.erms.domain.feedback.QuestionFeedback;
import com.kcidea.erms.domain.feedback.QuestionFeedbackType;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import com.kcidea.erms.enums.database.EnumCheckState;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.feedback.FeedbackInfoModel;
import com.kcidea.erms.model.feedback.FeedbackModel;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.feedback.FeedbackDisposeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/22
 **/
@Service
public class FeedbackDisposeServiceImpl extends BaseService implements FeedbackDisposeService {

    @Resource
    private QuestionFeedbackDao questionFeedbackDao;

    @Resource
    private SchoolDatabaseListDao schoolDatabaseListDao;

    @Resource
    private QuestionFeedbackTypeDao questionFeedbackTypeDao;

    /**
     * 反馈列表
     *
     * @param sid        学校id
     * @param did        数据库id
     * @param title      反馈标题
     * @param typeId     反馈类型
     * @param checkState 审核状态
     * @param checkFlag  审核标识
     * @param pageNum    页码
     * @param pageSize   每页数量
     * @return 反馈列表
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @Override
    public PageResult<FeedbackModel> findFeedbackList(Long sid, Long did, String title, Long typeId, Integer checkState,
                                                      boolean checkFlag, Integer pageNum, Integer pageSize) {
        // 校验参数
        super.checkPageParam(pageNum, pageSize);
        super.checkSid(sid);

        Page<FeedbackModel> page = new Page<>(pageNum, pageSize);

        // 查询数据
        List<FeedbackModel> list = questionFeedbackDao.findListBySidDidTitleTypeIdCheckStateCheckFlagPage(sid, did,
                title, typeId, checkState, checkFlag, page);

        // 循环组类型名称
        for (FeedbackModel model : list) {
            // 反馈类型
            String typeName = questionFeedbackTypeDao.findNameBySidTypeId(sid, model.getTypeId());
            model.setTypeName(typeName == null ? "" : typeName);

            // 审核状态
            EnumCheckState enumCheckState = EnumCheckState.getCheckState(model.getCheckState());
            model.setCheckStateName(enumCheckState == null ? "" : enumCheckState.getName());

            // 数据库名字
            if (model.getDid() != null) {
                model.setDName(super.findDatabaseName(sid, model.getDid()));
            }
        }

        PageResult<FeedbackModel> result = new PageResult<>();
        return result.success(list, page.getTotal());
    }

    /**
     * 获取反馈类型下拉框
     *
     * @param sid 学校id
     * @return 反馈类型下拉框
     * @author majuehao
     * @date 2021/11/22 11:03
     **/
    @Override
    public List<IdNameModel> findFeedbackTypeList(Long sid) {
        List<IdNameModel> list =
                Lists.newArrayList(new IdNameModel().create(Constant.ALL_LONG_VALUE, Constant.ALL_STRING_VALUE));
        list.addAll(questionFeedbackTypeDao.findIdNameListBySid(sid));
        return list;
    }

    /**
     * 删除反馈
     *
     * @param sid        学校id
     * @param feedbackId 反馈id
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/22 11:03
     **/
    @Override
    public String deleteFeedback(Long sid, Long feedbackId) {
        super.checkSid(sid);

        // 查询要删除的数据
        QuestionFeedback questionFeedback = questionFeedbackDao.findOneByIdSid(sid, feedbackId);

        // 判断删除的对象是否为空
        if (questionFeedback == null) {
            throw new CustomException(Vm.NO_DATA);
        }

        // 待审核和未通过的才可以删除
        if (questionFeedback.getCheckState() != EnumCheckState.待审核.getValue() &&
                questionFeedback.getCheckState() != EnumCheckState.审核未通过.getValue()) {
            throw new CustomException("只有待审核和未通过的才可以删除");
        }

        questionFeedbackDao.deleteById(questionFeedback);
        return Vm.DELETE_SUCCESS;
    }

    /**
     * 回复反馈回显
     *
     * @param sid        学校id
     * @param feedbackId 反馈id
     * @return 反馈详情
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @Override
    public FeedbackInfoModel findOneById(Long sid, Long feedbackId) {
        super.checkSid(sid);
        // 查询数据
        QuestionFeedback questionFeedback = questionFeedbackDao.findOneByIdSid(sid, feedbackId);

        // 判断查询的对象是否为空
        if (questionFeedback == null) {
            throw new CustomException(Vm.NO_DATA);
        }

        FeedbackInfoModel feedbackInfoModel = new FeedbackInfoModel();

        // 复制属性
        BeanUtils.copyProperties(questionFeedback, feedbackInfoModel);
        if (feedbackInfoModel.getDId() != null) {
            feedbackInfoModel.setDatabaseName(super.findDatabaseName(sid, feedbackInfoModel.getDId()));
        }

        // 设置反馈类型
        String typeName = questionFeedbackTypeDao.findNameBySidTypeId(sid, feedbackInfoModel.getTypeId());
        feedbackInfoModel.setTypeName(typeName == null ? "" : typeName);

        return feedbackInfoModel;
    }

    /**
     * 新增或编辑反馈
     *
     * @param sid               学校id
     * @param adminId           操作人id
     * @param feedbackInfoModel 反馈详情
     * @param request           request
     * @return 更新的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @Override
    public String addOrUpdateFeedback(Long sid, Long adminId, FeedbackInfoModel feedbackInfoModel,
                                      HttpServletRequest request) {
        // 校验参数
        super.checkSid(sid);

        if (feedbackInfoModel == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        // 新增标识
        boolean addFlag = false;
        if (feedbackInfoModel.getId() == null) {
            addFlag = true;
        }

        // 类型id
        if (feedbackInfoModel.getTypeId() == null) {
            feedbackInfoModel.setTypeName(FormatUtil.formatValue(feedbackInfoModel.getTypeName()));
            if (Strings.isNullOrEmpty(feedbackInfoModel.getTypeName())) {
                throw new CustomException("反馈类型为必填项");
            }
            feedbackInfoModel.setTypeId(getFeedbackType(sid, feedbackInfoModel.getTypeName(), adminId));
        }


        QuestionFeedback questionFeedback;

        if (addFlag) {
            // 审核状态
            Integer checkState = feedbackInfoModel.getCheckFlag() == 0 ?
                    EnumCheckState.审核通过.getValue() : EnumCheckState.待审核.getValue();

            questionFeedback = new QuestionFeedback().create(
                    sid,
                    feedbackInfoModel.getDId(),
                    feedbackInfoModel.getName(),
                    feedbackInfoModel.getIdentity(),
                    feedbackInfoModel.getUnit(),
                    feedbackInfoModel.getEmail(),
                    feedbackInfoModel.getPhone(),
                    feedbackInfoModel.getTypeId(),
                    request == null ? "" : IpUtil.getIpAddress(request),
                    feedbackInfoModel.getFeedbackTitle(),
                    feedbackInfoModel.getFeedbackTime(),
                    feedbackInfoModel.getFeedbackContent(),
                    EnumTrueFalse.是.getValue(),
                    checkState,
                    feedbackInfoModel.getAnswerName(),
                    feedbackInfoModel.getAnswerTime(),
                    feedbackInfoModel.getAnswerContent(),
                    feedbackInfoModel.getCheckFlag(),
                    EnumTrueFalse.否.getValue(),
                    adminId,
                    LocalDateTime.now());

            questionFeedbackDao.insert(questionFeedback);

            return Vm.INSERT_SUCCESS;

        } else {
            // 查询数据
            questionFeedback = questionFeedbackDao.findOneByIdSid(sid, feedbackInfoModel.getId());
            // 判断查询的对象是否为空
            if (questionFeedback == null) {
                throw new CustomException(Vm.NO_DATA);
            }

            if (questionFeedback.getCheckState() == EnumCheckState.审核未通过.getValue() &&
                    feedbackInfoModel.getRecheckFlag() == EnumTrueFalse.是.getValue()) {
                questionFeedback.setCheckState(EnumCheckState.待审核.getValue());
                questionFeedback.setRemark("");
            }

            if (!questionFeedback.getCheckFlag().equals(feedbackInfoModel.getCheckFlag())) {
                throw new CustomException("抱歉,无法编辑存档规则");
            }

            // 复制并更新数据
            BeanUtils.copyProperties(feedbackInfoModel, questionFeedback);
            questionFeedback.setUpdatedBy(adminId);
            questionFeedback.setUpdatedTime(LocalDateTime.now());
            questionFeedbackDao.updateById(questionFeedback);
            return Vm.UPDATE_SUCCESS;
        }
    }


    /**
     * 获取学校所有数据库下拉框
     *
     * @param sid 学校id
     * @return 所有数据库下拉框
     * @author majuehao
     * @date 2021/11/22 11:03
     **/
    @Override
    public List<IdNameModel> findAllDatabaseDropDown(Long sid) {
        // 学校订阅的数据库集合
        Set<Long> didSet = schoolDatabaseListDao.findOrderDidSetBySidYear(sid, null);

        // 查询名字并返回
        return super.findDatabaseIdNameList(didSet, sid);
    }

    /**
     * 审核反馈
     *
     * @param sid        学校id
     * @param userId     用户id
     * @param feedbackId 反馈处理id
     * @param checkState 审核状态
     * @param remark     审核说明
     * @return 修改的结果
     * @author majuehao
     * @date 2021/12/21 9:49
     **/
    @Override
    public String updateFeedbackCheck(Long sid, Long userId, Long feedbackId, Integer checkState, String remark) {
        checkSidUserId(sid, userId);

        QuestionFeedback questionFeedback = questionFeedbackDao.selectById(feedbackId);

        if (questionFeedback == null) {
            throw new CustomException(Vm.NO_DATA);
        }

        // 校验审核状态
        Integer beforeState = questionFeedback.getCheckState();
        super.checkCheckState(beforeState, checkState);

        questionFeedback.setCheckState(checkState);
        questionFeedback.setRemark(remark);
        questionFeedback.setUpdatedBy(userId);
        questionFeedback.setUpdatedTime(LocalDateTime.now());
        questionFeedbackDao.updateById(questionFeedback);

        return Vm.UPDATE_SUCCESS;
    }

    /**
     * 获取反馈类型
     *
     * @param sid      学校id
     * @param typeName 反馈名称
     * @param userId   用户id
     * @return 反馈类型id
     * @author majuehao
     * @date 2021/12/27 10:12
     **/
    private Long getFeedbackType(Long sid, String typeName, Long userId) {
        // 首尾去空格
        typeName = FormatUtil.formatValue(typeName);
        // 查询id
        Long typeId = questionFeedbackTypeDao.findTypeIdBySidName(sid, typeName);
        // 查不到说明没有该类型，新增该类型
        if (typeId == null) {
            QuestionFeedbackType questionFeedbackType = new QuestionFeedbackType().create(
                    sid, typeName, userId, LocalDateTime.now());
            questionFeedbackTypeDao.insert(questionFeedbackType);
            return questionFeedbackType.getId();
        } else {
            return typeId;
        }
    }
}
