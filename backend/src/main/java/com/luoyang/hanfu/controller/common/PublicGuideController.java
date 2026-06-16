package com.luoyang.hanfu.controller.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.entity.Guide;
import com.luoyang.hanfu.service.GuideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 通用接口 - 洛阳攻略控制器
 */
@Tag(name = "通用模块 - 攻略浏览")
@RestController
@RequestMapping("/api/common/guides")
@RequiredArgsConstructor
public class PublicGuideController {

    private final GuideService guideService;

    @Operation(summary = "获取所有攻略列表")
    @GetMapping("")
    public R<List<Guide>> list() {
        // 按照创建时间倒序排列，展示最新的攻略
        LambdaQueryWrapper<Guide> queryWrapper = new LambdaQueryWrapper<Guide>()
                .orderByDesc(Guide::getCreateTime);
        return R.ok(guideService.list(queryWrapper));
    }
}
