package com.luoyang.hanfu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 预约订单实体类
 * 
 * @author Backend Architect
 */
@Data
@TableName(value = "reservation_order", autoResultMap = true)
@Schema(description = "汉服预约订单详细信息实体")
public class ReservationOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "汉服ID")
    private Long hanfuId;

    @Schema(description = "预约日期")
    private LocalDate reservationDate;

    @Schema(description = "时间段（如 09:00-12:00 / 13:00-17:00 / 全天）")
    private String timeSlot;

    @Schema(description = "预约数量")
    private Integer quantity;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "附加服务ID列表")
    private List<Long> serviceIds;

    @Schema(description = "使用人姓名")
    private String customerName;

    @Schema(description = "使用人手机号")
    private String customerPhone;

    @Schema(description = "总金额")
    private BigDecimal totalAmount;

    @Schema(description = "状态：RESERVED/USED/CANCELLED/COMPLETED")
    private String status;

    @Schema(description = "支付方式：BALANCE/OFFLINE")
    private String payType;

    @Schema(description = "支付状态：UNPAID/PAID/REFUNDED")
    private String payStatus;

    @Schema(description = "备注")
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

    @TableLogic
    @Schema(description = "逻辑删除标记")
    private Integer deleted;
}
