package com.luoyang.hanfu.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 公告留言与咨询实体类
 * 
 * @author Backend Architect
 */
@Data
@TableName("feedback")
@Schema(description = "用户咨询与反馈留言详情")
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "用户ID（访客可为空）")
    private Long userId;

    @Schema(description = "留言内容")
    private String content;

    @Schema(description = "类型（MESSAGE-留言 / QA-咨询）")
    private String type;

    @Schema(description = "评分 (1-5)")
    private Integer rating;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "图片列表 (JSON)")
    private String images;

    @Schema(description = "回复内容")
    private String replyContent;

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
