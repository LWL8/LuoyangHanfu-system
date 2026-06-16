package com.luoyang.hanfu.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 首页轮播图实体类
 */
@Data
@TableName("banner")
@Schema(description = "首页轮播图实体")
public class Banner implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "图片地址")
    private String image;

    @Schema(description = "跳转链接")
    private String link;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "启用状态：1启用，0禁用")
    private Integer enabled;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

    @TableLogic
    @Schema(description = "逻辑删除：0-未删除，1-已删除")
    private Integer deleted;
}
