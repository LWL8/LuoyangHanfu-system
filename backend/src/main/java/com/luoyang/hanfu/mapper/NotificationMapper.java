package com.luoyang.hanfu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyang.hanfu.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知记录表 Mapper 接口
 */
@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
}
