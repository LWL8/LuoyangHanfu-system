package com.luoyang.hanfu.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知记录实体类
 * 
 * @author Backend Architect
 */
@Data
@TableName("notification")
@Schema(description = "系统发送的短信/邮件通知记录实体")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "关联订单ID")
    private Long orderId;

    @Schema(description = "通知渠道（SMS/EMAIL）")
    private String channel;

    @Schema(description = "通知标题")
    private String title;

    @Schema(description = "通知内容")
    private String content;

    @Schema(description = "发送状态（SENT/FAILED）")
    private String status;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
