package com.kcidea.erms.service.feedback;

import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.feedback.FeedbackInfoModel;
import com.kcidea.erms.model.feedback.FeedbackModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/22
 **/
public interface FeedbackDisposeService {

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
    PageResult<FeedbackModel> findFeedbackList(Long sid, Long did, String title, Long typeId, Integer checkState,
                                               boolean checkFlag, Integer pageNum, Integer pageSize);

    /**
     * 获取反馈类型下拉框
     *
     * @param sid 学校id
     * @return 反馈类型下拉框
     * @author majuehao
     * @date 2021/11/22 11:03
     **/
    List<IdNameModel> findFeedbackTypeList(Long sid);

    /**
     * 删除反馈
     *
     * @param sid        学校id
     * @param feedbackId 反馈id
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/22 11:03
     **/
    String deleteFeedback(Long sid, Long feedbackId);

    /**
     * 回复反馈回显
     *
     * @param sid        学校id
     * @param feedbackId 反馈id
     * @return 反馈详情
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    FeedbackInfoModel findOneById(Long sid, Long feedbackId);

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
    String addOrUpdateFeedback(Long sid, Long adminId, FeedbackInfoModel feedbackInfoModel, HttpServletRequest request);

    /**
     * 获取学校所有数据库下拉框
     *
     * @param sid 学校id
     * @return 所有数据库下拉框
     * @author majuehao
     * @date 2021/11/22 11:03
     **/
    List<IdNameModel> findAllDatabaseDropDown(Long sid);

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
    String updateFeedbackCheck(Long sid, Long userId, Long feedbackId, Integer checkState, String remark);
}
