package com.luoyang.hanfu.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.entity.Hanfu;
import com.luoyang.hanfu.entity.RecommendConfig;
import com.luoyang.hanfu.service.HanfuService;
import com.luoyang.hanfu.service.RecommendConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户端 - 推荐内容控制器
 */
@Tag(name = "用户模块 - 首页推荐")
@RestController
@RequestMapping("/api/user/recommend")
@RequiredArgsConstructor
public class UserRecommendController {

    private final RecommendConfigService recommendConfigService;
    private final HanfuService hanfuService;

    @Operation(summary = "获取首页推荐汉服列表")
    @GetMapping("/hot")
    public R<List<Hanfu>> getHotRecommends() {
        // 1. 查询所有标记为推荐的配置
        LambdaQueryWrapper<RecommendConfig> queryWrapper = new LambdaQueryWrapper<RecommendConfig>()
                .eq(RecommendConfig::getRecommended, 1);
        List<RecommendConfig> configs = recommendConfigService.list(queryWrapper);
        
        if (configs.isEmpty()) {
            return R.ok(List.of());
        }
        
        // 2. 获取对应的汉服列表
        List<Long> ids = configs.stream().map(RecommendConfig::getHanfuId).collect(Collectors.toList());
        List<Hanfu> list = hanfuService.listByIds(ids);
        
        // 3. 过滤并返回（确保只返回上架的）
        List<Hanfu> filtered = list.stream()
                .filter(h -> h.getOnShelf() != null && h.getOnShelf() == 1)
                .collect(Collectors.toList());
                
        return R.ok(filtered);
    }
}
