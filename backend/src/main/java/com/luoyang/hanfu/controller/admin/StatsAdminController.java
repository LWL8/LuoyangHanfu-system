package com.luoyang.hanfu.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.entity.Hanfu;
import com.luoyang.hanfu.entity.ReservationOrder;
import com.luoyang.hanfu.service.HanfuService;
import com.luoyang.hanfu.service.ReservationOrderService;
import com.luoyang.hanfu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理员端 - 数据统计控制器
 * 
 * @author Backend Architect
 */
@Tag(name = "管理员模块 - 数据统计")
@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
public class StatsAdminController {

    private final ReservationOrderService orderService;
    private final HanfuService hanfuService;
    private final UserService userService;

    @Operation(summary = "仪表盘综合统计数据")
    @GetMapping("/dashboard")
    public R<Map<String, Object>> dashboard() {
        Map<String, Object> data = new HashMap<>();
        
        // 1. 活跃用户
        data.put("activeUsers", userService.count());
        
        // 2. 总订单数
        List<ReservationOrder> allOrders = orderService.list();
        data.put("totalOrders", allOrders.size());
        
        // 3. 总营收
        BigDecimal totalRevenue = allOrders.stream()
                .filter(o -> !"CANCELLED".equals(o.getStatus()))
                .map(ReservationOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        data.put("totalRevenue", totalRevenue.multiply(new BigDecimal("100")).longValue());
        
        // 4. 订单趋势 (近7天)
        java.util.LinkedHashMap<String, Long> orderTrend = new java.util.LinkedHashMap<>();
        java.time.LocalDate today = java.time.LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            java.time.LocalDate date = today.minusDays(i);
            long count = allOrders.stream()
                    .filter(o -> o.getCreateTime() != null && o.getCreateTime().toLocalDate().equals(date))
                    .count();
            orderTrend.put(date.format(java.time.format.DateTimeFormatter.ISO_DATE), count);
        }
        data.put("orderTrend", orderTrend);
        
        // 5. 热门汉服
        List<Map<String, Object>> hotHanfu = hanfuService.list(new LambdaQueryWrapper<Hanfu>()
                .orderByDesc(Hanfu::getHotScore)
                .last("limit 5"))
                .stream().map(h -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", h.getName());
                    map.put("sales", h.getHotScore());
                    return map;
                }).collect(Collectors.toList());
        data.put("hotHanfu", hotHanfu);
        
        return R.ok(data);
    }

    @Operation(summary = "首页概览统计")
    @GetMapping("/summary")
    public R<Map<String, Object>> summary() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalUsers", userService.count());
        data.put("totalOrders", orderService.count());
        
        BigDecimal totalRevenue = orderService.list().stream()
                .filter(o -> !"CANCELLED".equals(o.getStatus()))
                .map(ReservationOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        data.put("totalRevenue", totalRevenue);
        
        return R.ok(data);
    }

    @Operation(summary = "每日预约趋势")
    @GetMapping("/daily")
    public R<List<Map<String, Object>>> dailyStats() {
        // 简单分组统计：日期 -> 计数
        List<ReservationOrder> orders = orderService.list(new LambdaQueryWrapper<ReservationOrder>()
                .orderByAsc(ReservationOrder::getReservationDate));
        
        Map<String, Long> grouped = orders.stream()
                .collect(Collectors.groupingBy(o -> o.getReservationDate().toString(), Collectors.counting()));
        
        List<Map<String, Object>> result = grouped.entrySet().stream()
                .map(e -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("date", e.getKey());
                    item.put("count", e.getValue());
                    return item;
                })
                .sorted((a, b) -> ((String)a.get("date")).compareTo((String)b.get("date")))
                .collect(Collectors.toList());
                
        return R.ok(result);
    }

    @Operation(summary = "汉服预约热度排行")
    @GetMapping("/hot")
    public R<List<Hanfu>> hotRanking() {
        return R.ok(hanfuService.list(new LambdaQueryWrapper<Hanfu>()
                .orderByDesc(Hanfu::getHotScore)
                .last("limit 10")));
    }
}
