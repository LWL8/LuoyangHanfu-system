package com.luoyang.hanfu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyang.hanfu.entity.Banner;
import com.luoyang.hanfu.mapper.BannerMapper;
import com.luoyang.hanfu.service.BannerService;
import org.springframework.stereotype.Service;

/**
 * 轮播图 服务实现类
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {
}
