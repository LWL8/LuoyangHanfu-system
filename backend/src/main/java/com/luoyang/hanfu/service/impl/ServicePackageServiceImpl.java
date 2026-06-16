package com.luoyang.hanfu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyang.hanfu.entity.ServicePackage;
import com.luoyang.hanfu.mapper.ServicePackageMapper;
import com.luoyang.hanfu.service.ServicePackageService;
import org.springframework.stereotype.Service;

/**
 * 附加服务套餐服务实现类
 */
@Service
public class ServicePackageServiceImpl extends ServiceImpl<ServicePackageMapper, ServicePackage> implements ServicePackageService {
}
