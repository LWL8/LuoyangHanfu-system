package com.luoyang.hanfu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyang.hanfu.entity.ReservationOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 预约订单表 Mapper 接口
 */
@Mapper
public interface ReservationOrderMapper extends BaseMapper<ReservationOrder> {
}
