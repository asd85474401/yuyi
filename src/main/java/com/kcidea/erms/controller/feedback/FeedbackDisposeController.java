package com.kcidea.erms.controller.feedback;

import com.kcidea.erms.aop.ActionRights;
import com.kcidea.erms.aop.LoginCheck;
import com.kcidea.erms.common.result.MultipleResult;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.result.Result;
import com.kcidea.erms.controller.common.BaseController;
import com.kcidea.erms.enums.menu.EnumMenu;
import com.kcidea.erms.enums.user.EnumLoginAction;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.common.FileModel;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.feedback.FeedbackInfoModel;
import com.kcidea.erms.model.feedback.FeedbackModel;
import com.kcidea.erms.service.common.SelectListService;
import com.kcidea.erms.service.common.UploadService;
import com.kcidea.erms.service.feedback.FeedbackDisposeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/22
 **/
@Slf4j
@RestController
@RequestMapping("/feedback")
public class FeedbackDisposeController extends BaseController {

    @Resource
    private FeedbackDisposeService feedbackService;

    @Resource
    private SelectListService selectListService;

    @Resource
    private UploadService uploadService;

    /**
     * 反馈列表
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
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.问题反馈)
    @GetMapping(value = "/findFeedbackList")
    public PageResult<FeedbackModel> findFeedbackList(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "typeId", defaultValue = "999") Long typeId,
            @RequestParam(value = "checkState", defaultValue = "999") Integer checkState,
            @RequestParam(value = "did", defaultValue = "999") Long did,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        super.saveInfoLog();
        return feedbackService.findFeedbackList(getSid(), did, title, typeId, checkState, false,
                pageNum, pageSize);
    }

    /**
     * 获取反馈类型下拉框
     *
     * @return 反馈类型下拉框
     * @author majuehao
     * @date 2021/11/22 11:03
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.问题反馈)
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
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.问题反馈)
    @GetMapping(value = "/findDatabaseDropDown")
    public MultipleResult<IdNameModel> findDatabaseDropDown() {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(selectListService.findFeedbackDatabaseSelectList(getSid()));
    }

    /**
     * 获取学校所有数据库下拉框
     *
     * @return 所有数据库下拉框
     * @author majuehao
     * @date 2021/11/22 11:03
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.问题反馈)
    @GetMapping(value = "/findAllDatabaseDropDown")
    public MultipleResult<IdNameModel> findAllDatabaseDropDown() {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(feedbackService.findAllDatabaseDropDown(getSid()));
    }

    /**
     * 删除反馈
     *
     * @param feedbackId 反馈id
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/22 11:03
     **/
    @ActionRights(userAction = EnumUserAction.删除, menu = EnumMenu.问题反馈)
    @DeleteMapping(value = "/deleteFeedback")
    public Result<String> deleteFeedback(@RequestParam(value = "feedbackId") Long feedbackId) {

        super.saveInfoLog();
        return new Result<String>().success(feedbackService.deleteFeedback(getSid(), feedbackId));
    }

    /**
     * 回显编辑反馈
     *
     * @param feedbackId 反馈id
     * @return 反馈详情
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.问题反馈)
    @GetMapping(value = "/findOneById")
    public Result<FeedbackInfoModel> findOneById(@RequestParam(value = "feedbackId") Long feedbackId) {
        super.saveInfoLog();
        return new Result<FeedbackInfoModel>().success(feedbackService.findOneById(getSid(), feedbackId));
    }

    /**
     * 新增反馈
     *
     * @param feedbackInfoModel 反馈详情
     * @param request           request
     * @return 新增的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.新增, menu = EnumMenu.问题反馈)
    @PostMapping(value = "/addFeedback")
    public Result<String> addFeedback(@RequestBody @Valid FeedbackInfoModel feedbackInfoModel,
                                      HttpServletRequest request) {
        super.saveInfoLog();
        return new Result<String>()
                .success(feedbackService.addOrUpdateFeedback(getSid(), getUserId(), feedbackInfoModel, request));
    }

    /**
     * 编辑反馈
     *
     * @param feedbackInfoModel 反馈详情
     * @param request           request
     * @return 编辑的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.问题反馈)
    @PostMapping(value = "/updateFeedback")
    public Result<String> updateFeedback(@RequestBody @Valid FeedbackInfoModel feedbackInfoModel,
                                         HttpServletRequest request) {
        super.saveInfoLog();
        return new Result<String>()
                .success(feedbackService.addOrUpdateFeedback(getSid(), getUserId(), feedbackInfoModel, request));
    }

    /**
     * 上传导入反馈文件
     *
     * @param file 表格
     * @return 文件信息
     * @author majuehao
     * @date 2021/12/21 13:31
     **/
    @LoginCheck(action = EnumLoginAction.Normal)
    @PostMapping(value = "/uploadFeedbackExcelFile")
    public Result<FileModel> uploadFeedbackExcelFile(@RequestParam MultipartFile file) {
        super.saveInfoLog();
        return new Result<FileModel>().success(uploadService.uploadExcelFile(file));
    }
}
