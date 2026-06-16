package com.luoyang.hanfu.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyang.hanfu.common.BusinessException;
import com.luoyang.hanfu.dto.ReservationDTO;
import com.luoyang.hanfu.entity.Hanfu;
import com.luoyang.hanfu.entity.ReservationOrder;
import com.luoyang.hanfu.entity.ServicePackage;
import com.luoyang.hanfu.entity.User;
import com.luoyang.hanfu.mapper.ReservationOrderMapper;
import com.luoyang.hanfu.service.HanfuService;
import com.luoyang.hanfu.service.ReservationOrderService;
import com.luoyang.hanfu.service.ServicePackageService;
import com.luoyang.hanfu.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * 预约订单服务实现类
 * 
 * @author Backend Architect
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationOrderServiceImpl extends ServiceImpl<ReservationOrderMapper, ReservationOrder> implements ReservationOrderService {

    private final HanfuService hanfuService;
    private final ServicePackageService servicePackageService;
    private final UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReservationOrder createReservation(Long userId, ReservationDTO dto) {
        // 1. 获取并锁汉服库存 (悲观锁实现)
        Hanfu hanfu = hanfuService.getById(dto.getHanfuId());
        if (hanfu == null || hanfu.getOnShelf() != 1) {
            throw new BusinessException("该款汉服已下架或不存在");
        }
        if (hanfu.getStock() < dto.getQuantity()) {
            throw new BusinessException("库存不足");
        }

        // 2. 计算总价
        // 汉服租赁费
        BigDecimal hanfuTotal = hanfu.getRentPrice().multiply(new BigDecimal(dto.getQuantity()));
        
        // 附加服务费
        BigDecimal serviceTotal = BigDecimal.ZERO;
        List<Long> serviceIds = dto.getServiceIds();
        if (serviceIds != null && !serviceIds.isEmpty()) {
            List<ServicePackage> packages = servicePackageService.listByIds(serviceIds);
            serviceTotal = packages.stream()
                    .map(ServicePackage::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        
        BigDecimal totalAmount = hanfuTotal.add(serviceTotal);

        // 3. 处理支付（模拟余额支付）
        if ("BALANCE".equals(dto.getPayType())) {
            User user = userService.getById(userId);
            if (user.getBalance().compareTo(totalAmount) < 0) {
                throw new BusinessException("账户余额不足");
            }
            user.setBalance(user.getBalance().subtract(totalAmount));
            userService.updateById(user);
        }

        // 4. 减库存
        hanfu.setStock(hanfu.getStock() - dto.getQuantity());
        hanfu.setHotScore(hanfu.getHotScore() + 1);
        hanfuService.updateById(hanfu);

        // 5. 生成订单
        ReservationOrder order = new ReservationOrder();
        order.setOrderNo(IdUtil.getSnowflakeNextIdStr());
        order.setUserId(userId);
        order.setHanfuId(dto.getHanfuId());
        order.setReservationDate(dto.getReservationDate());
        order.setTimeSlot(dto.getTimeSlot());
        order.setQuantity(dto.getQuantity());
        order.setServiceIds(serviceIds);
        order.setCustomerName(dto.getCustomerName());
        order.setCustomerPhone(dto.getCustomerPhone());
        order.setTotalAmount(totalAmount);
        order.setStatus("RESERVED");
        order.setPayType(dto.getPayType());
        order.setPayStatus("BALANCE".equals(dto.getPayType()) ? "PAID" : "UNPAID");
        order.setRemark(dto.getRemark());

        this.save(order);
        
        log.info("用户 [{}] 预约成功，订单号: {}", userId, order.getOrderNo());
        // TODO: 调用通知服务发送模拟消息
        
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId, Long userId) {
        ReservationOrder order = this.getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (!"RESERVED".equals(order.getStatus())) {
            throw new BusinessException("当前状态不可取消");
        }

        // 1. 退还库存
        Hanfu hanfu = hanfuService.getById(order.getHanfuId());
        if (hanfu != null) {
            hanfu.setStock(hanfu.getStock() + order.getQuantity());
            hanfuService.updateById(hanfu);
        }

        // 2. 退款 (如果是余额支付且已支付)
        if ("BALANCE".equals(order.getPayType()) && "PAID".equals(order.getPayStatus())) {
            User user = userService.getById(userId);
            user.setBalance(user.getBalance().add(order.getTotalAmount()));
            userService.updateById(user);
            order.setPayStatus("REFUNDED");
        }

        // 3. 更新状态
        order.setStatus("CANCELLED");
        this.updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsUsed(Long orderId) {
        ReservationOrder order = this.getById(orderId);
        if (order == null) throw new BusinessException("订单不存在");
        order.setStatus("USED");
        this.updateById(order);
    }
}
