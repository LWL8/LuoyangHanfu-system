package com.luoyang.hanfu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyang.hanfu.entity.Notification;

/**
 * 通知服务接口（模拟短信/邮件）
 * 
 * @author Backend Architect
 */
public interface NotificationService extends IService<Notification> {

    /**
     * 发送通知
     * @param userId 用户ID
     * @param orderId 关联订单ID
     * @param channel 渠道（SMS/EMAIL）
     * @param title 标题
     * @param content 内容
     */
    void sendNotification(Long userId, Long orderId, String channel, String title, String content);
}
