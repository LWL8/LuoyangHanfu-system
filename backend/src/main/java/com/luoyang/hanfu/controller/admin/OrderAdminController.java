package com.luoyang.hanfu.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.entity.ReservationOrder;
import com.luoyang.hanfu.service.ReservationOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员端 - 订单管理控制器
 * 
 * @author Backend Architect
 */
@Tag(name = "管理员模块 - 订单管理")
@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class OrderAdminController {

    private final ReservationOrderService orderService;
    private final com.luoyang.hanfu.service.HanfuService hanfuService;

    @Operation(summary = "分页查询订单列表")
    @GetMapping("/list")
    public R<Page<java.util.Map<String, Object>>> list(@RequestParam(defaultValue = "1") Integer current,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          @RequestParam(required = false) String status,
                                          @RequestParam(required = false) String keyword,
                                          @RequestParam(required = false) String startDate,
                                          @RequestParam(required = false) String endDate) {
        Page<ReservationOrder> page = new Page<>(current, size);
        LambdaQueryWrapper<ReservationOrder> queryWrapper = new LambdaQueryWrapper<ReservationOrder>();
        
        // 状态筛选
        queryWrapper.eq(StringUtils.hasText(status), ReservationOrder::getStatus, status);
        
        // 模糊搜索：订单号、姓名、手机号
        queryWrapper.and(StringUtils.hasText(keyword), w -> w
                .like(ReservationOrder::getOrderNo, keyword)
                .or()
                .like(ReservationOrder::getCustomerName, keyword)
                .or()
                .like(ReservationOrder::getCustomerPhone, keyword)
        );
        
        // 日期范围筛选 (基于预约日期)
        queryWrapper.ge(StringUtils.hasText(startDate), ReservationOrder::getReservationDate, startDate);
        queryWrapper.le(StringUtils.hasText(endDate), ReservationOrder::getReservationDate, endDate);
        
        queryWrapper.orderByDesc(ReservationOrder::getCreateTime);
        
        Page<ReservationOrder> orderPage = orderService.page(page, queryWrapper);
        
        Page<java.util.Map<String, Object>> voPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        java.util.List<java.util.Map<String, Object>> voList = orderPage.getRecords().stream().map(order -> {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", order.getId());
            map.put("orderNo", order.getOrderNo());
            map.put("customerName", order.getCustomerName());
            map.put("customerPhone", order.getCustomerPhone());
            map.put("totalAmount", order.getTotalAmount());
            map.put("reservationDate", order.getReservationDate());
            map.put("timeSlot", order.getTimeSlot());
            if (order.getCreateTime() != null) {
                map.put("createTime", order.getCreateTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            
            // 映射前端状态
            int orderStatus = 101; // 默认 RESERVED
            if ("USED".equals(order.getStatus())) orderStatus = 103;
            else if ("COMPLETED".equals(order.getStatus())) orderStatus = 106;
            else if ("CANCELLED".equals(order.getStatus())) orderStatus = 104;
            map.put("orderStatus", orderStatus);
            
            // 获取汉服信息
            com.luoyang.hanfu.entity.Hanfu hanfu = hanfuService.getById(order.getHanfuId());
            if (hanfu != null) {
                map.put("hanfuName", hanfu.getName());
                if (hanfu.getImageList() != null && !hanfu.getImageList().isEmpty()) {
                    map.put("hanfuPhoto", hanfu.getImageList().get(0));
                }
            }
            return map;
        }).collect(java.util.stream.Collectors.toList());
        
        voPage.setRecords(voList);
        return R.ok(voPage);
    }

    @Operation(summary = "确认订单已使用")
    @PostMapping("/markUsed/{id}")
    public R<Void> markUsed(@PathVariable Long id) {
        orderService.markAsUsed(id);
        return R.ok();
    }

    @Operation(summary = "取消/作废订单")
    @PostMapping("/cancel/{id}")
    public R<Void> cancel(@PathVariable Long id) {
        // 管理员取消不需要用户权限校验，传入 null 或特定标识
        ReservationOrder order = orderService.getById(id);
        orderService.cancelOrder(id, order.getUserId());
        return R.ok();
    }

    @Operation(summary = "更新订单状态")
    @PutMapping("/{id}/status")
    public R<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        ReservationOrder order = orderService.getById(id);
        if (order == null) {
            return R.error("订单不存在");
        }
        // 前端状态映射: 101待确认, 102已拒绝, 103进行中(已使用), 104已取消, 106已完成
        String statusStr = "RESERVED";
        if (status == 103) statusStr = "USED";
        else if (status == 104) statusStr = "CANCELLED";
        else if (status == 106) statusStr = "COMPLETED";
        
        order.setStatus(statusStr);
        orderService.updateById(order);
        return R.ok();
    }

    @Operation(summary = "删除订单")
    @DeleteMapping("/{id}")
    public R<Void> deleteOrder(@PathVariable Long id) {
        orderService.removeById(id);
        return R.ok();
    }
}
