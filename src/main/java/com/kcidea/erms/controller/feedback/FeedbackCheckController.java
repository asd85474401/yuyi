package com.kcidea.erms.controller.feedback;

import com.kcidea.erms.aop.ActionRights;
import com.kcidea.erms.common.result.MultipleResult;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.result.Result;
import com.kcidea.erms.controller.common.BaseController;
import com.kcidea.erms.enums.menu.EnumMenu;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.feedback.FeedbackInfoModel;
import com.kcidea.erms.model.feedback.FeedbackModel;
import com.kcidea.erms.service.common.SelectListService;
import com.kcidea.erms.service.feedback.FeedbackDisposeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/21
 **/
@Slf4j
@RestController
@RequestMapping("/feedbackCheck")
public class FeedbackCheckController extends BaseController {

    @Resource
    private FeedbackDisposeService feedbackService;

    @Resource
    private SelectListService selectListService;


    /**
     * 反馈审核列表
     *
     * @param title      反馈标题
     * @param typeId     反馈类型
     * @param checkState 审核状态
     * @param did        数据库id
     * @param pageNum    页码
     * @param pageSize   每页数量
     * @return 反馈列表
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.用户反馈审核)
    @GetMapping(value = "/findFeedbackCheckList")
    public PageResult<FeedbackModel> findFeedbackCheckList(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "typeId", defaultValue = "999") Long typeId,
            @RequestParam(value = "checkState", defaultValue = "999") Integer checkState,
            @RequestParam(value = "did", defaultValue = "999") Long did,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        super.saveInfoLog();
        return feedbackService.findFeedbackList(getSid(), did, title, typeId, checkState, true,
                pageNum, pageSize);
    }

    /**
     * 审核反馈
     *
     * @param feedbackId 反馈处理id
     * @param checkState 审核状态
     * @param remark     审核说明
     * @return 修改的结果
     * @author majuehao
     * @date 2021/12/21 9:49
     **/
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.用户反馈审核)
    @PostMapping(value = "/updateFeedbackCheck")
    public Result<String> updateFeedbackCheck(@RequestParam(value = "feedbackId") Long feedbackId,
                                              @RequestParam(value = "checkState") Integer checkState,
                                              @RequestParam(value = "remark", required = false) String remark) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                feedbackService.updateFeedbackCheck(getSid(), getUserId(), feedbackId, checkState, remark));
    }


    /**
     * 获取反馈类型下拉框
     *
     * @return 反馈类型下拉框
     * @author majuehao
     * @date 2021/11/22 11:03
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.用户反馈审核)
    @GetMapping(value = "/findFeedbackTypeList")
    public MultipleResult<IdNameModel> findFeedbackTypeList() {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(feedbackService.findFeedbackTypeList(getSid()));
    }

    /**
     * 获取数据库下拉框
     *
     * @return 数据库下拉框
     * @author majuehao
     * @date 2021/11/22 11:03
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.用户反馈审核)
    @GetMapping(value = "/findDatabaseDropDown")
    public MultipleResult<IdNameModel> findDatabaseDropDown() {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(selectListService.findFeedbackDatabaseSelectList(getSid()));
    }

    /**
     * 回显编辑反馈
     *
     * @param feedbackId 反馈id
     * @return 反馈详情
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.用户反馈审核)
    @GetMapping(value = "/findOneById")
    public Result<FeedbackInfoModel> findOneById(@RequestParam(value = "feedbackId") Long feedbackId) {
        super.saveInfoLog();
        return new Result<FeedbackInfoModel>().success(feedbackService.findOneById(getSid(), feedbackId));
    }

    /**
     * 获取学校所有数据库下拉框
     *
     * @return 所有数据库下拉框
     * @author majuehao
     * @date 2021/11/22 11:03
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.用户反馈审核)
    @GetMapping(value = "/findAllDatabaseDropDown")
    public MultipleResult<IdNameModel> findAllDatabaseDropDown() {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>()
                .success(selectListService.findGenericDatabaseSelectList(getSid(), false));
    }

}
