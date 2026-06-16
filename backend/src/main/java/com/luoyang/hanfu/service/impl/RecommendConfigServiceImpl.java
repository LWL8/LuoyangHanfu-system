package com.luoyang.hanfu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyang.hanfu.entity.RecommendConfig;
import com.luoyang.hanfu.mapper.RecommendConfigMapper;
import com.luoyang.hanfu.service.RecommendConfigService;
import org.springframework.stereotype.Service;

/**
 * 新品推荐配置 服务实现类
 */
@Service
public class RecommendConfigServiceImpl extends ServiceImpl<RecommendConfigMapper, RecommendConfig> implements RecommendConfigService {
}
