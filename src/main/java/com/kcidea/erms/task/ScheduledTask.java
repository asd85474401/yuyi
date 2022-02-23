package com.kcidea.erms.task;

import com.kcidea.erms.service.task.PayRemindTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/17
 **/
@Slf4j
@Component
public class ScheduledTask {

    @Resource
    private PayRemindTaskService payRemindTaskService;

    /**
     * 合同付款提醒定时任务，每天早上10点执行
     *
     * @author yeweiwei
     * @date 2021/11/17 15:16
     */
    @Scheduled(cron = "0 0 10 * * ? ")
    private void remindPay() {
        log.info("定时任务，执行付款提醒定时任务");
        try {
            payRemindTaskService.executePayRemindTask();
        } catch (Exception e) {
            log.error("执行付款提醒定时任务失败：" + e.getMessage(), e);
        }
        log.info("执行付款提醒定时任务结束");
    }
}
