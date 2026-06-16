package com.luoyang.hanfu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyang.hanfu.entity.Hanfu;
import com.luoyang.hanfu.mapper.HanfuMapper;
import com.luoyang.hanfu.service.HanfuService;
import org.springframework.stereotype.Service;

/**
 * 汉服服务实现类
 * 
 * @author Backend Architect
 */
@Service
public class HanfuServiceImpl extends ServiceImpl<HanfuMapper, Hanfu> implements HanfuService {
    // 逻辑实现主要由 MyBatis-Plus 提供，后续可扩展复杂的库管逻辑
}
