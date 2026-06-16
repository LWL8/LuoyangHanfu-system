package com.luoyang.hanfu.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luoyang.hanfu.entity.Admin;
import com.luoyang.hanfu.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 启动时初始化默认管理员账号（仅当不存在时创建）。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminDataInitializer implements CommandLineRunner {

    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Admin existed = adminMapper.selectOne(
                new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, "admin"));
        if (existed != null) {
            return;
        }

        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.setRole("ADMIN");
        admin.setStatus(1);
        adminMapper.insert(admin);
        log.warn("已自动初始化管理员账号：admin / 123456（请尽快修改密码）");
    }
}
