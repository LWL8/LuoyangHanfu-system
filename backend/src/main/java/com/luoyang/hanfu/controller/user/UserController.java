package com.luoyang.hanfu.controller.user;

import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.dto.LoginDTO;
import com.luoyang.hanfu.dto.RegisterDTO;
import com.luoyang.hanfu.entity.User;
import com.luoyang.hanfu.service.AdminService;
import com.luoyang.hanfu.service.UserService;
import com.luoyang.hanfu.util.JwtUtils;
import com.luoyang.hanfu.vo.TokenVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户端 - 账户控制器
 * 
 * @author Backend Architect
 */
@Tag(name = "用户模块", description = "处理用户注册、登录、及个人信息")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AdminService adminService;
    private final JwtUtils jwtUtils;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public R<Void> register(@Valid @RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO);
        return R.ok();
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public R<TokenVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        return R.ok(userService.login(loginDTO));
    }

    @Operation(summary = "发送短信验证码（开发模拟）")
    @PostMapping("/sendSms")
    public R<Map<String, String>> sendSms(@RequestParam String phone) {
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            return R.error(400, "手机号格式不正确");
        }
        Map<String, String> data = new HashMap<>();
        data.put("verifyCode", "123456");
        return R.ok(data);
    }

    @Operation(summary = "手机号验证码登录")
    @PostMapping("/loginByPhone")
    public R<TokenVO> loginByPhone(@RequestBody Map<String, String> payload) {
        String phone = payload.get("phone");
        String code = payload.get("code");
        if (phone == null || !phone.matches("^1[3-9]\\d{9}$")) {
            return R.error(400, "手机号格式不正确");
        }
        if (!"123456".equals(code)) {
            return R.error(400, "验证码错误");
        }
        User user = userService.query().eq("phone", phone).one();
        if (user == null) {
            return R.error(400, "该手机号未注册");
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            return R.error(400, "账号已禁用，请联系管理员");
        }

        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), "USER");
        TokenVO vo = TokenVO.builder()
                .token(token)
                .tokenPrefix("Bearer ")
                .userInfo(TokenVO.UserInfoVO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .role("USER")
                        .avatar(user.getAvatar())
                        .build())
                .build();
        return R.ok(vo);
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public R<Object> getInfo() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.query().eq("username", username).one();
        if (user != null) {
            user.setPassword(null); // 安全脱敏
            return R.ok(user);
        }
        
        // 尝试从 admin 表获取
        com.luoyang.hanfu.entity.Admin admin = adminService.query().eq("username", username).one();
        if (admin != null) {
            admin.setPassword(null);
            return R.ok(admin);
        }
        
        return R.error(400, "用户不存在");
    }
    
    @Operation(summary = "修改个人信息")
    @PutMapping("/update")
    public R<Void> update(@RequestBody User user) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.query().eq("username", username).one();
        if (currentUser == null) {
            return R.error(400, "管理员账号不支持修改个人信息");
        }
        user.setId(currentUser.getId());
        // 禁止通过此接口修改权限或敏感字段
        user.setUsername(null);
        user.setPassword(null);
        userService.updateById(user);
        return R.ok();
    }

    @Operation(summary = "用户充值")
    @PostMapping("/recharge")
    public R<Void> recharge(@RequestBody Map<String, Object> params) {
        if (params == null || !params.containsKey("amount")) {
            return R.error(400, "参数缺失");
        }
        
        BigDecimal amount;
        try {
            amount = new BigDecimal(params.get("amount").toString());
        } catch (Exception e) {
            return R.error(400, "金额格式不正确");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return R.error(400, "充值金额必须大于0");
        }
        
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.query().eq("username", username).one();
        if (user == null) {
            return R.error(400, "用户不存在");
        }
        
        BigDecimal currentBalance = user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO;
        user.setBalance(currentBalance.add(amount));
        userService.updateById(user);
        return R.ok();
    }
}
