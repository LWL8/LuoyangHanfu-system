package com.luoyang.hanfu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyang.hanfu.dto.LoginDTO;
import com.luoyang.hanfu.entity.Admin;
import com.luoyang.hanfu.vo.TokenVO;

/**
 * 管理员服务接口
 * 
 * @author Backend Architect
 */
public interface AdminService extends IService<Admin> {

    /**
     * 管理员登录
     */
    TokenVO login(LoginDTO loginDTO);
}
