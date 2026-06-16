package com.luoyang.hanfu.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 洛阳景点推荐实体类
 * 
 * @author Backend Architect
 */
@Data
@TableName("attraction")
@Schema(description = "洛阳当地景点介绍与拍摄建议实体")
public class Attraction implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "景点名称")
    private String name;

    @Schema(description = "景点地址")
    private String address;

    @Schema(description = "推荐搭配汉服ID")
    private Long recommendHanfuId;

    @Schema(description = "景点图片")
    private String image;

    @Schema(description = "景点简介")
    private String description;

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
