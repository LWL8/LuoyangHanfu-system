package com.luoyang.hanfu.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.entity.Banner;
import com.luoyang.hanfu.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户端 - 轮播图查询
 */
@Tag(name = "普通用户模块 - 轮播图")
@RestController
@RequestMapping("/api/user/banner")
@RequiredArgsConstructor
public class UserBannerController {

    private final BannerService bannerService;

    @Operation(summary = "首页展示列表")
    @GetMapping("/list")
    public R<List<Banner>> list() {
        LambdaQueryWrapper<Banner> queryWrapper = new LambdaQueryWrapper<Banner>()
                .eq(Banner::getEnabled, 1) // 仅显示已启用的
                .orderByAsc(Banner::getSort);
        return R.ok(bannerService.list(queryWrapper));
    }
}
