package com.luoyang.hanfu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyang.hanfu.entity.Attraction;
import com.luoyang.hanfu.mapper.AttractionMapper;
import com.luoyang.hanfu.service.AttractionService;
import org.springframework.stereotype.Service;

@Service
public class AttractionServiceImpl extends ServiceImpl<AttractionMapper, Attraction> implements AttractionService {
}
