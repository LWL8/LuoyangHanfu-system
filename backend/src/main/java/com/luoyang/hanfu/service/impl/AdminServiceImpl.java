package com.luoyang.hanfu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyang.hanfu.common.BusinessException;
import com.luoyang.hanfu.dto.LoginDTO;
import com.luoyang.hanfu.entity.Admin;
import com.luoyang.hanfu.mapper.AdminMapper;
import com.luoyang.hanfu.service.AdminService;
import com.luoyang.hanfu.util.JwtUtils;
import com.luoyang.hanfu.vo.TokenVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 管理员服务实现类
 * 
 * @author Backend Architect
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public TokenVO login(LoginDTO loginDTO) {
        // 1. 查询管理员
        Admin admin = this.getOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getUsername, loginDTO.getUsername()));

        if (admin == null) {
            throw new BusinessException(400, "管理员账号不存在");
        }

        // 2. 校验密码
        // 注意：生产环境初始化时需使用 BCrypt 加密，示例数据若是明文需同步加密
        if (!passwordEncoder.matches(loginDTO.getPassword(), admin.getPassword())) {
            throw new BusinessException(400, "密码错误");
        }

        // 3. 校验状态
        if (admin.getStatus() != 1) {
            throw new BusinessException(400, "账号已禁用");
        }

        // 4. 生成 Token
        String token = jwtUtils.generateToken(admin.getId(), admin.getUsername(), admin.getRole());

        // 5. 返回
        return TokenVO.builder()
                .token(token)
                .tokenPrefix("Bearer ")
                .userInfo(TokenVO.UserInfoVO.builder()
                        .id(admin.getId())
                        .username(admin.getUsername())
                        .role(admin.getRole())
                        .build())
                .build();
    }
}
