package com.luoyang.hanfu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyang.hanfu.entity.Store;
import com.luoyang.hanfu.mapper.StoreMapper;
import com.luoyang.hanfu.service.StoreService;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {
}
