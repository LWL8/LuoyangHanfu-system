package com.luoyang.hanfu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyang.hanfu.entity.Notification;
import com.luoyang.hanfu.mapper.NotificationMapper;
import com.luoyang.hanfu.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 通知服务实现类 (模拟逻辑)
 * 
 * @author Backend Architect
 */
@Slf4j
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Override
    public void sendNotification(Long userId, Long orderId, String channel, String title, String content) {
        log.info("============== [通知模拟发送] ==============");
        log.info("渠道: {}", channel);
        log.info("用户ID: {}", userId);
        log.info("标题: {}", title);
        log.info("内容: {}", content);
        log.info("===========================================");

        // 记录到数据库
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setOrderId(orderId);
        notification.setChannel(channel);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setStatus("SENT");
        
        this.save(notification);
    }
}
