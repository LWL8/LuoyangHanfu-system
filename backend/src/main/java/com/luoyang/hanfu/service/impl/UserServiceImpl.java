package com.luoyang.hanfu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyang.hanfu.common.BusinessException;
import com.luoyang.hanfu.dto.LoginDTO;
import com.luoyang.hanfu.dto.RegisterDTO;
import com.luoyang.hanfu.entity.User;
import com.luoyang.hanfu.mapper.UserMapper;
import com.luoyang.hanfu.service.UserService;
import com.luoyang.hanfu.util.JwtUtils;
import com.luoyang.hanfu.vo.TokenVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务实现类
 * 
 * @author Backend Architect
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public TokenVO login(LoginDTO loginDTO) {
        // 1. 根据用户名查询用户
        User user = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, loginDTO.getUsername()));

        if (user == null) {
            throw new BusinessException(400, "用户不存在");
        }

        // 2. 校验密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(400, "密码错误");
        }

        // 3. 校验状态
        if (user.getStatus() != 1) {
            throw new BusinessException(400, "账号已禁用，请联系管理员");
        }

        // 4. 生成 Token
        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), "USER");

        // 5. 组装返回对象
        return TokenVO.builder()
                .token(token)
                .tokenPrefix("Bearer ")
                .userInfo(TokenVO.UserInfoVO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .role("USER")
                        .avatar(user.getAvatar())
                        .build())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDTO registerDTO) {
        // 1. 判断用户名是否已存在
        long count = this.count(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, registerDTO.getUsername()));
        if (count > 0) {
            throw new BusinessException(400, "用户名已存在");
        }

        // 2. 判断手机号是否已存在
        count = this.count(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, registerDTO.getPhone()));
        if (count > 0) {
            throw new BusinessException(400, "手机号已注册");
        }

        // 3. 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setNickname(
                registerDTO.getNickname() == null || registerDTO.getNickname().isBlank()
                        ? registerDTO.getUsername()
                        : registerDTO.getNickname()
        );
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setPhone(registerDTO.getPhone());
        user.setEmail(registerDTO.getEmail());
        user.setStatus(1); // 默认启用

        this.save(user);
    }
}
