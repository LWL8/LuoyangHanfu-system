package com.luoyang.hanfu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyang.hanfu.entity.Guide;
import com.luoyang.hanfu.mapper.GuideMapper;
import com.luoyang.hanfu.service.GuideService;
import org.springframework.stereotype.Service;

/**
 * 洛阳穿搭攻略 服务实现类
 */
@Service
public class GuideServiceImpl extends ServiceImpl<GuideMapper, Guide> implements GuideService {
}
