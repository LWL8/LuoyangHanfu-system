package com.luoyang.hanfu.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luoyang.hanfu.common.BusinessException;
import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.dto.ReservationDTO;
import com.luoyang.hanfu.entity.ReservationOrder;
import com.luoyang.hanfu.entity.User;
import com.luoyang.hanfu.service.ReservationOrderService;
import com.luoyang.hanfu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户端 - 预约下单控制器
 * 
 * @author Backend Architect
 */
@Tag(name = "用户模块 - 预约下单")
@RestController
@RequestMapping("/api/user/reservation")
@RequiredArgsConstructor
public class ReservationUserController {

    private final ReservationOrderService orderService;
    private final UserService userService;
    private final com.luoyang.hanfu.service.HanfuService hanfuService;

    private Long getCurrentUserId() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.query().eq("username", username).one();
        if (user == null) {
            throw new BusinessException(400, "管理员账号不能进行预约，请使用普通用户账号登录");
        }
        return user.getId();
    }

    @Operation(summary = "提交预约单")
    @PostMapping("/submit")
    public R<ReservationOrder> submit(@Valid @RequestBody ReservationDTO dto) {
        return R.ok(orderService.createReservation(getCurrentUserId(), dto));
    }

    @Operation(summary = "查询我的预约列表")
    @GetMapping("/my")
    public R<List<java.util.Map<String, Object>>> myOrders() {
        List<ReservationOrder> orders = orderService.list(new LambdaQueryWrapper<ReservationOrder>()
                .eq(ReservationOrder::getUserId, getCurrentUserId())
                .orderByDesc(ReservationOrder::getCreateTime));
                
        List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();
        for (ReservationOrder order : orders) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", order.getId());
            map.put("orderNo", order.getOrderNo());
            map.put("customerName", order.getCustomerName());
            map.put("customerPhone", order.getCustomerPhone());
            map.put("totalAmount", order.getTotalAmount());
            map.put("reservationDate", order.getReservationDate());
            map.put("timeSlot", order.getTimeSlot());
            map.put("status", order.getStatus());
            map.put("payStatus", order.getPayStatus());
            if (order.getCreateTime() != null) {
                map.put("createTime", order.getCreateTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            
            com.luoyang.hanfu.entity.Hanfu hanfu = hanfuService.getById(order.getHanfuId());
            if (hanfu != null) {
                map.put("hanfuName", hanfu.getName());
                if (hanfu.getImageList() != null && !hanfu.getImageList().isEmpty()) {
                    map.put("hanfuPhoto", hanfu.getImageList().get(0));
                }
            }
            result.add(map);
        }
        return R.ok(result);
    }

    @Operation(summary = "获取预约详情")
    @GetMapping("/{id}")
    public R<java.util.Map<String, Object>> getDetail(@PathVariable Long id) {
        ReservationOrder order = orderService.getById(id);
        if (order == null || !order.getUserId().equals(getCurrentUserId())) {
            throw new BusinessException(404, "订单不存在或无权访问");
        }
        
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("id", order.getId());
        map.put("orderNo", order.getOrderNo());
        map.put("customerName", order.getCustomerName());
        map.put("customerPhone", order.getCustomerPhone());
        map.put("totalAmount", order.getTotalAmount());
        map.put("reservationDate", order.getReservationDate());
        map.put("timeSlot", order.getTimeSlot());
        map.put("status", order.getStatus());
        map.put("payStatus", order.getPayStatus());
        if (order.getCreateTime() != null) {
            map.put("createTime", order.getCreateTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        
        // 获取汉服信息
        com.luoyang.hanfu.entity.Hanfu hanfu = hanfuService.getById(order.getHanfuId());
        if (hanfu != null) {
            map.put("hanfuName", hanfu.getName());
            if (hanfu.getImageList() != null && !hanfu.getImageList().isEmpty()) {
                map.put("hanfuPhoto", hanfu.getImageList().get(0));
            }
        }
        return R.ok(map);
    }

    @Operation(summary = "取消预约")
    @PostMapping("/cancel/{id}")
    public R<Void> cancel(@PathVariable Long id) {
        orderService.cancelOrder(id, getCurrentUserId());
        return R.ok();
    }
}
