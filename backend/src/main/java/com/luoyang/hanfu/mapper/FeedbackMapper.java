package com.luoyang.hanfu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyang.hanfu.entity.Feedback;
import org.apache.ibatis.annotations.Mapper;

/**
 * 留言反馈表 Mapper 接口
 */
@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {
}
