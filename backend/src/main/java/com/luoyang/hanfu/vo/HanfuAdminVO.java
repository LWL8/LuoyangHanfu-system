package com.luoyang.hanfu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 管理员端 - 汉服信息 VO
 * 用于适配前端特定的字段命名习惯 (如 hanfuName, hanfuPhoto, shangjiaTypes 等)
 */
@Data
@Schema(description = "管理员端汉服信息视图对象")
public class HanfuAdminVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "汉服名称")
    private String hanfuName;

    @Schema(description = "封面图")
    private String hanfuPhoto;

    @Schema(description = "朝代分类")
    private String hanfuTypes;

    @Schema(description = "日租金")
    private BigDecimal hanfuPrice;

    @Schema(description = "押金 (后端暂存 stock 字段或固定值，如需持久化建议修改表结构)")
    private BigDecimal hanfuNewMoney;

    @Schema(description = "上架状态：1-上架，2-下架")
    private Integer shangjiaTypes;

    @Schema(description = "库存数量")
    private Integer stock;

    @Schema(description = "汉服详情描述")
    private String hanfuContent;
}
