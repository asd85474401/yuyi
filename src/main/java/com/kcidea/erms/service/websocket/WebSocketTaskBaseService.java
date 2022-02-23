package com.kcidea.erms.service.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.util.DateTimeUtil;
import com.kcidea.erms.common.util.RateUtil;
import com.kcidea.erms.domain.task.ErmsTask;
import com.kcidea.erms.enums.task.EnumTaskState;
import com.kcidea.erms.enums.task.EnumTaskType;
import com.kcidea.erms.model.common.TaskParamModel;
import com.kcidea.erms.model.common.WebSocketTaskModel;
import com.kcidea.erms.model.user.UserModel;
import com.kcidea.erms.model.websocket.WebSocketProgressModel;
import com.kcidea.erms.service.task.TaskService;
import com.kcidea.erms.websocket.WebSocketClient;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/12/9
 **/
public abstract class WebSocketTaskBaseService {
    @Resource
    private TaskService taskService;

    /**
     * 添加任务
     *
     * @param userModel    用户信息
     * @param jsonData     任务json数据
     * @param enumTaskType 任务类型
     * @return 任务
     * @author yeweiwei
     * @date 2021/12/9 9:22
     */
    protected ErmsTask addTask(UserModel userModel, Object jsonData, EnumTaskType enumTaskType) {
        Long sid = userModel.getSid();
        Long userId = userModel.getId();

        // 解析jsonData
        WebSocketTaskModel webSocketTaskModel = JSONObject.toJavaObject((JSON) jsonData, WebSocketTaskModel.class);
        Integer vYear = webSocketTaskModel.getVYear();
        String fileName = webSocketTaskModel.getFileName();
        String filePath = webSocketTaskModel.getFilePath();

        //校验年份
        Integer startYear = userModel.getStartYear();
        int endYear = LocalDate.now().getYear() + 1;
        if (enumTaskType == EnumTaskType.数据库评估列表同步) {
            endYear = endYear - 1;
        }
        if (vYear < startYear) {
            throw new CustomException("很抱歉，选则的年份不能小于学校开始年份！");
        }
        if (vYear > endYear) {
            throw new CustomException("很抱歉，选则的年份不能大于最大年份！");
        }

        //构造任务参数
        TaskParamModel taskParamModel = new TaskParamModel();
        String taskName = "";
        switch (enumTaskType) {
            case 数据库采购列表导入:
            case 数据库评估列表同步:
                taskParamModel.setSid(sid).setVYear(vYear);
                taskName = (vYear + 1) + "年" + enumTaskType.getName();
                break;
            default:
                break;
        }

        return taskService.addTask(sid, taskName, enumTaskType.getValue(), EnumTaskState.正在执行.getValue(),
                JSON.toJSONString(taskParamModel), fileName, filePath, userId, Boolean.FALSE);
    }

    /**
     * 发送任务进度消息
     *
     * @param uuid         操作人uuid
     * @param total        总数
     * @param number       已执行
     * @param successCount 执行成功的数量
     * @param msg          消息内容
     * @author yeweiwei
     * @date 2021/12/9 10:12
     */
    protected void sendWebSocketProgressMsg(String uuid, Integer total, Integer number,
                                            Integer successCount, String msg) {
        WebSocketProgressModel progressModel = new WebSocketProgressModel().create(total, number,
                RateUtil.find100PointRatioByTwoInteger(successCount, total, 2), msg);
        WebSocketClient.sendInfo(JSON.toJSONString(progressModel), uuid);
    }

    /**
     * 表格数据非空校验
     *
     * @param field    校验的数据
     * @param errorMsg 错误信息
     * @author yeweiwei
     * @date 2021/12/2 17:40
     */
    protected void checkIsEmpty(String field, String errorMsg) {
        if (Strings.isNullOrEmpty(field)) {
            throw new CustomException(errorMsg);
        }
    }

    /**
     * 校验最大长度限制
     *
     * @param field    校验的数据
     * @param maxSize  最大长度
     * @param errorMsg 错误信息
     * @author yeweiwei
     * @date 2021/12/2 17:53
     */
    protected void checkLength(String field, Integer maxSize, String errorMsg) {
        if (field.trim().length() > maxSize) {
            throw new CustomException(errorMsg);
        }
    }

    /**
     * 正则校验
     *
     * @param field    校验的数据
     * @param regexp   正则表达式
     * @param errorMsg 错误信息
     * @author yeweiwei
     * @date 2021/12/2 17:53
     */
    protected void checkPattern(String field, String regexp, String errorMsg) {
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(field.trim());
        if (!m.matches()) {
            throw new CustomException(errorMsg);
        }
    }

    /**
     * 校验时间格式
     *
     * @param time     校验的时间
     * @param errorMsg 错误信息
     * @author yeweiwei
     * @date 2021/12/2 17:53
     */
    protected void checkTimeFormat(String time, String errorMsg) {
        LocalDateTime dateTime = DateTimeUtil.strToDate(time);
        if (dateTime == null) {
            throw new CustomException(errorMsg);
        }
    }

}
