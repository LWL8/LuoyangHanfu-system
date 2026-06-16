package com.luoyang.hanfu.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 新品推荐配置实体类
 */
@Data
@TableName("recommend_config")
@Schema(description = "新品推荐配置实体")
public class RecommendConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "汉服ID")
    private Long hanfuId;

    @Schema(description = "是否新品：1是，0否")
    private Integer isNew;

    @Schema(description = "是否推荐：1是，0否")
    private Integer recommended;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
}
