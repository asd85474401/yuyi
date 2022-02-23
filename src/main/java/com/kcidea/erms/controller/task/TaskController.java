package com.kcidea.erms.controller.task;

import com.kcidea.erms.aop.ActionRights;
import com.kcidea.erms.common.result.MultipleResult;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.result.Result;
import com.kcidea.erms.controller.common.BaseController;
import com.kcidea.erms.enums.menu.EnumMenu;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.task.TaskInfoModel;
import com.kcidea.erms.model.task.TaskModel;
import com.kcidea.erms.service.common.DownloadService;
import com.kcidea.erms.service.task.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/29
 **/
@Slf4j
@RestController
@RequestMapping("/task")
public class TaskController extends BaseController {

    @Resource
    private TaskService taskService;

    @Resource
    private DownloadService downloadService;

    /**
     * 获取任务类型下拉集合
     *
     * @return 任务类型下拉集合
     * @author majuehao
     * @date 2021/11/29 11:09
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.任务列表)
    @GetMapping(value = "/findTaskTypeDropDownList")
    public MultipleResult<IdNameModel> findTaskTypeDropDownList() {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(taskService.findTaskTypeDropDownList());
    }

    /**
     * 获取任务状态下拉集合
     *
     * @return 任务状态下拉集合
     * @author majuehao
     * @date 2021/11/29 11:09
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.任务列表)
    @GetMapping(value = "/findTaskStateDropDownList")
    public MultipleResult<IdNameModel> findTaskStateDropDownList() {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(taskService.findTaskStateDropDownList());
    }

    /**
     * 查询任务列表
     *
     * @param taskName  任务名称
     * @param taskType  任务类型
     * @param taskState 任务状态
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @return 任务列表
     * @author majuehao
     * @date 2021/11/29 13:21
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.任务列表)
    @GetMapping(value = "/findTaskList")
    public PageResult<TaskModel> findTaskList(
            @RequestParam(value = "taskName") String taskName,
            @RequestParam(value = "taskType") Integer taskType,
            @RequestParam(value = "taskState") Integer taskState,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        super.saveInfoLog();
        return taskService.findTaskList(taskName, taskType, taskState, getSid(), pageNum, pageSize);
    }

    /**
     * 删除任务
     *
     * @param taskId 任务id
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/29 13:44
     **/
    @ActionRights(userAction = EnumUserAction.删除, menu = EnumMenu.任务列表)
    @DeleteMapping(value = "/deleteTask")
    public Result<String> deleteTask(@RequestParam(value = "taskId") Long taskId) {
        super.saveInfoLog();
        return new Result<String>().success(taskService.deleteTask(taskId, getSid()));
    }

    /**
     * 查看任务
     *
     * @param taskId 任务id
     * @return 任务详情
     * @author majuehao
     * @date 2021/11/29 14:34
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.任务列表)
    @GetMapping(value = "/findOneTask")
    public Result<TaskInfoModel> findOneTask(@RequestParam(value = "taskId") Long taskId) {
        super.saveInfoLog();
        return new Result<TaskInfoModel>().success(taskService.findOneTask(taskId, getSid()));
    }
}
