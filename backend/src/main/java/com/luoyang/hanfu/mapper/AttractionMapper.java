package com.luoyang.hanfu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyang.hanfu.entity.Attraction;
import org.apache.ibatis.annotations.Mapper;

/**
 * 景点推荐表 Mapper 接口
 */
@Mapper
public interface AttractionMapper extends BaseMapper<Attraction> {
}
