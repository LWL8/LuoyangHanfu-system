package com.luoyang.hanfu.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据库自动修复配置 - 确保 service_package 表具备必要的列
 */
@Slf4j
@Configuration
public class DbAutoFixConfig {

    @Bean
    public CommandLineRunner autoFixServiceTable(JdbcTemplate jdbcTemplate) {
        return args -> {
            log.info("Checking database schema for service_package...");
            try {
                // 检查 subtitle
                try {
                    jdbcTemplate.execute("ALTER TABLE service_package ADD COLUMN subtitle varchar(200) AFTER name");
                    log.info("Successfully added 'subtitle' column to service_package table.");
                } catch (Exception e) {
                    if (!e.getMessage().contains("Duplicate column")) {
                        log.debug("Subtitle column might already exist or table missing: {}", e.getMessage());
                    }
                }

                // 检查 image
                try {
                    jdbcTemplate.execute("ALTER TABLE service_package ADD COLUMN image varchar(255) AFTER description");
                    log.info("Successfully added 'image' column to service_package table.");
                } catch (Exception e) {
                    if (!e.getMessage().contains("Duplicate column")) {
                        log.debug("Image column might already exist or table missing: {}", e.getMessage());
                    }
                }
            } catch (Exception e) {
                log.error("Failed to auto-fix database schema: {}", e.getMessage());
            }
        };
    }
}
