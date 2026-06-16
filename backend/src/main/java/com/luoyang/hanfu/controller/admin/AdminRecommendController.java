package com.luoyang.hanfu.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.entity.RecommendConfig;
import com.luoyang.hanfu.service.RecommendConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员端 - 新品推荐配置控制器
 */
@Tag(name = "管理员模块 - 新品推荐管理")
@RestController
@RequestMapping("/api/admin/recommend")
@RequiredArgsConstructor
public class AdminRecommendController {

    private final RecommendConfigService recommendConfigService;

    @Operation(summary = "获取所有推荐配置")
    @GetMapping("/list")
    public R<List<RecommendConfig>> list() {
        return R.ok(recommendConfigService.list());
    }

    @Operation(summary = "批量更新推荐配置")
    @PutMapping("/update")
    public R<Void> updateBatch(@RequestBody List<RecommendConfig> list) {
        if (list == null || list.isEmpty()) {
            return R.ok();
        }
        
        for (RecommendConfig config : list) {
            LambdaQueryWrapper<RecommendConfig> queryWrapper = new LambdaQueryWrapper<RecommendConfig>()
                    .eq(RecommendConfig::getHanfuId, config.getHanfuId());
            RecommendConfig existing = recommendConfigService.getOne(queryWrapper);
            
            if (existing != null) {
                config.setId(existing.getId());
                recommendConfigService.updateById(config);
            } else {
                recommendConfigService.save(config);
            }
        }
        return R.ok();
    }
}
