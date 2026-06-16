package com.luoyang.hanfu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 预约订单请求 DTO
 * 
 * @author Backend Architect
 */
@Data
@Schema(description = "创建预约订单请求参数")
public class ReservationDTO implements Serializable {

    @NotNull(message = "请选择汉服")
    @Schema(description = "汉服ID")
    private Long hanfuId;

    @NotNull(message = "请选择日期")
    @Schema(description = "预约日期")
    private LocalDate reservationDate;

    @NotBlank(message = "请选择时间段")
    @Schema(description = "时间段（如 09:00-12:00）")
    private String timeSlot;

    @NotNull(message = "请输入预约数量")
    @Min(value = 1, message = "预约数量最少为1")
    @Schema(description = "预约数量")
    private Integer quantity;

    @Schema(description = "所选附加服务ID列表")
    private List<Long> serviceIds;

    @NotBlank(message = "请输入联系人姓名")
    @Schema(description = "使用人姓名")
    private String customerName;

    @NotBlank(message = "请输入联系人电话")
    @Schema(description = "使用人手机号")
    private String customerPhone;

    @Schema(description = "支付方式：BALANCE/OFFLINE")
    private String payType;

    @Schema(description = "备注")
    private String remark;
}
