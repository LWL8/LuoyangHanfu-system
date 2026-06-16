package com.luoyang.hanfu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyang.hanfu.dto.LoginDTO;
import com.luoyang.hanfu.dto.RegisterDTO;
import com.luoyang.hanfu.entity.User;
import com.luoyang.hanfu.vo.TokenVO;

/**
 * 用户服务接口
 * 
 * @author Backend Architect
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     */
    TokenVO login(LoginDTO loginDTO);

    /**
     * 用户注册
     */
    void register(RegisterDTO registerDTO);
}
