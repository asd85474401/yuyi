package com.kcidea.erms.dao.feedback;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kcidea.erms.domain.feedback.QuestionFeedback;
import com.kcidea.erms.model.feedback.FeedbackInfoModel;
import com.kcidea.erms.model.feedback.FeedbackModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/22 10:11
 **/
public interface QuestionFeedbackDao extends BaseMapper<QuestionFeedback> {


    /**
     * 根据条件，查询反馈列表
     *
     * @param sid        学校id
     * @param did        数据库id
     * @param title      反馈标题
     * @param typeId     反馈类型
     * @param checkState 审核状态
     * @param checkFlag  审核标识
     * @param page       分页
     * @return 反馈列表
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    List<FeedbackModel> findListBySidDidTitleTypeIdCheckStateCheckFlagPage(@Param("sid") Long sid, @Param("did") Long did,
                                                                           @Param("title") String title,
                                                                           @Param("typeId") Long typeId,
                                                                           @Param("checkState") Integer checkState,
                                                                           @Param("checkFlag") boolean checkFlag,
                                                                           @Param("page") Page<FeedbackModel> page);

    /**
     * 根据学校id、反馈id,查询问题反馈详情
     *
     * @param sid        学校id
     * @param feedbackId 反馈id
     * @return 问题反馈实体
     * @author majuehao
     * @date 2021/11/22 13:16
     **/
    QuestionFeedback findOneByIdSid(@Param("sid") Long sid,
                                    @Param("feedbackId") Long feedbackId);


    /**
     * 根据学校id，查询数据库id集合
     *
     * @param sid 学校id
     * @return 数据库下拉框
     * @author majuehao
     * @date 2021/11/22 11:03
     **/
    List<Long> findDidListBySid(@Param("sid") Long sid);

    /**
     * 根据标题查询反馈
     *
     * @param feedbackTitle 反馈标题
     * @return 反馈
     * @author majuehao
     * @date 2021/12/21 18:30
     **/
    List<FeedbackInfoModel> findOneByFeedbackTitle(@Param("feedbackTitle") String feedbackTitle);
}
