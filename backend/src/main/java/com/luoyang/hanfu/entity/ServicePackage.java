package com.luoyang.hanfu.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 附加服务套餐实体类
 * 
 * @author Backend Architect
 */
@Data
@TableName("service_package")
@Schema(description = "摄影、妆造等附加服务套餐信息实体")
public class ServicePackage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "服务类型（妆造/发型/摄影/道具）")
    private String type;

    @Schema(description = "套餐名")
    private String name;

    @Schema(description = "副标题/简短描述")
    private String subtitle;

    @Schema(description = "套餐价格")
    private BigDecimal price;

    @Schema(description = "套餐描述")
    private String description;

    @Schema(description = "套餐图片地址")
    private String image;

    @Schema(description = "状态：1-上架，0-下架")
    private Integer status;

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
