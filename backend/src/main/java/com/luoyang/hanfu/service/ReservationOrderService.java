package com.luoyang.hanfu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyang.hanfu.dto.ReservationDTO;
import com.luoyang.hanfu.entity.ReservationOrder;

/**
 * 预约订单服务接口
 * 
 * @author Backend Architect
 */
public interface ReservationOrderService extends IService<ReservationOrder> {

    /**
     * 创建预约订单
     * @param userId 用户ID
     * @param reservationDTO 预约参数
     * @return 订单对象
     */
    ReservationOrder createReservation(Long userId, ReservationDTO reservationDTO);

    /**
     * 取消订单
     */
    void cancelOrder(Long orderId, Long userId);

    /**
     * 管理员确认订单已使用
     */
    void markAsUsed(Long orderId);
}
