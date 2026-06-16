package com.luoyang.hanfu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 洛阳汉服预约系统 - 启动类
 * 
 * @author Backend Architect
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.luoyang.hanfu.mapper")
public class HanfuApplication {

    public static void main(String[] args) {
        SpringApplication.run(HanfuApplication.class, args);
    }
}
