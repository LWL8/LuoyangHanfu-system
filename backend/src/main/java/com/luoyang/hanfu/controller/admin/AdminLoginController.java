package com.luoyang.hanfu.controller.admin;

import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.dto.LoginDTO;
import com.luoyang.hanfu.service.AdminService;
import com.luoyang.hanfu.vo.TokenVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员端 - 登录控制器
 * 
 * @author Backend Architect
 */
@Tag(name = "管理员模块 - 鉴权")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminLoginController {

    private final AdminService adminService;

    @Operation(summary = "管理员登录")
    @PostMapping("/login")
    public R<TokenVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        return R.ok(adminService.login(loginDTO));
    }
}
