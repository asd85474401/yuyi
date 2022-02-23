package com.kcidea.erms.service.common.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.util.RateUtil;
import com.kcidea.erms.common.util.RedisUtil;
import com.kcidea.erms.enums.common.EnumWebSocketApi;
import com.kcidea.erms.model.user.UserModel;
import com.kcidea.erms.model.websocket.WebSocketDataModel;
import com.kcidea.erms.model.websocket.WebSocketProgressModel;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.common.FactoryService;
import com.kcidea.erms.service.websocket.WebSocketTaskService;
import com.kcidea.erms.websocket.WebSocketClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/12/9
 **/
@Slf4j
@Service
public class FactoryServiceImpl extends BaseService implements FactoryService {

    @Resource
    private List<WebSocketTaskService> taskServiceList;

    /**
     * 执行WebSocket任务
     *
     * @param uuid               请求用户的uuid
     * @param webSocketDataModel 请求的数据
     * @author huxubin
     * @date 2021/11/25 15:52
     */
    @Override
    public void executeTask(String uuid, WebSocketDataModel webSocketDataModel) {

        String api = webSocketDataModel.getApi();
        EnumWebSocketApi webSocketApi = EnumWebSocketApi.getWebSocketApi(api);
        WebSocketProgressModel progressModel;

        if (webSocketApi == null) {
            progressModel = new WebSocketProgressModel().create(null, null,
                    RateUtil.find100PointRatioByTwoInteger(null, null, 2),
                    "很抱歉，您请求的Api不存在");
            WebSocketClient.sendInfo(JSON.toJSONString(progressModel), uuid);
            throw new CustomException("很抱歉，您请求的Api不存在");
        }

        //根据uuid获取user字符串
        String json = RedisUtil.getStringByKey(Constant.RedisKey.TOKEN_REDIS_KEY.concat(uuid));

        if (Strings.isNullOrEmpty(json)) {
            progressModel = new WebSocketProgressModel().create(null, null,
                    RateUtil.find100PointRatioByTwoInteger(null, null, 2),
                    Vm.DATA_NOT_EXIST);
            WebSocketClient.sendInfo(JSON.toJSONString(progressModel), uuid);
            throw new CustomException(Vm.DATA_NOT_EXIST);
        }

        //user字符串 转对象
        UserModel userModel = JSONObject.parseObject(json, UserModel.class);
        Long sid = userModel.getSid();
        Long userId = userModel.getId();
        this.checkSidUserId(sid, userId);

        WebSocketTaskService webSocketTaskService = this.getWebSocketTaskService(webSocketApi);
        webSocketTaskService.checkAdminMenu(uuid, userModel);
        webSocketTaskService.executeTask(uuid, userModel, webSocketDataModel.getData());
    }

    /**
     * 根据任务枚举获取对应的工厂实现
     *
     * @param webSocketApi 请求的API接口
     * @return 对应的工厂实现
     * @author huxubin
     * @date 2021/11/25 16:09
     */
    private WebSocketTaskService getWebSocketTaskService(EnumWebSocketApi webSocketApi) {
        WebSocketTaskService retService = null;
        for (WebSocketTaskService tmpService : taskServiceList) {
            if (tmpService.getWebSocketApi() == webSocketApi) {
                retService = tmpService;
                break;
            }
        }
        if (retService == null) {
            throw new CustomException(webSocketApi.toString() + "类型的任务接口尚未实现");
        }
        return retService;
    }
}
