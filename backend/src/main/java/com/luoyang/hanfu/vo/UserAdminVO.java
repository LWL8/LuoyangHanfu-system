package com.luoyang.hanfu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 管理员端 - 用户信息 VO
 * 用于适配前端特定的字段命名习惯 (如 yonghuPhoto, newMoney 等)
 */
@Data
@Schema(description = "管理员端用户信息视图对象")
public class UserAdminVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "用户名/账号")
    private String username;

    @Schema(description = "姓名/昵称")
    private String yonghuName;

    @Schema(description = "头像地址")
    private String yonghuPhoto;

    @Schema(description = "手机号")
    private String yonghuPhone;

    @Schema(description = "账户余额")
    private BigDecimal newMoney;

    @Schema(description = "账号状态：1-启用，0-禁用")
    private Integer status;

    @Schema(description = "注册时间")
    private String createTime;
}
