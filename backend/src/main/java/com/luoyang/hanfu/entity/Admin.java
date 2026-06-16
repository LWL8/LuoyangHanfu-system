package com.luoyang.hanfu.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 管理员实体类
 * 
 * @author Backend Architect
 */
@Data
@TableName("admin")
@Schema(description = "后台系统管理员账户实体")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "管理员账号")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "角色权限（SUPER_ADMIN/ADMIN）")
    private String role;

    @Schema(description = "账户状态：1-正常，0-禁用")
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
