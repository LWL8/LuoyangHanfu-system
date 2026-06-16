package com.luoyang.hanfu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录成功的 Token 视图对象
 * 
 * @author Backend Architect
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录成功的令牌视图对象")
public class TokenVO implements Serializable {

    @Schema(description = "Token 字符串")
    private String token;

    @Schema(description = "Token 前缀")
    private String tokenPrefix;

    @Schema(description = "用户信息")
    private UserInfoVO userInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "登录用户信息")
    public static class UserInfoVO {
        private Long id;
        private String username;
        private String role;
        private String avatar;
    }
}
