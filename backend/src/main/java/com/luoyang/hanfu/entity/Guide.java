package com.luoyang.hanfu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 洛阳穿搭攻略实体类
 */
@Data
@TableName(value = "guide", autoResultMap = true)
@Schema(description = "洛阳穿搭攻略实体")
public class Guide implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "穿搭风格")
    private String style;

    @Schema(description = "适合景点")
    private String scenic;

    @Schema(description = "搭配理由")
    private String reason;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "拍摄贴士（JSON数组）")
    private List<String> photographyTips;

    @Schema(description = "风格示例图")
    private String imageBefore;

    @Schema(description = "实景效果图")
    private String imageAfter;

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
