package com.luoyang.hanfu.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.entity.Banner;
import com.luoyang.hanfu.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员端 - 轮播图管理控制器
 */
@Tag(name = "管理员模块 - 轮播图管理")
@RestController
@RequestMapping("/api/admin/banner")
@RequiredArgsConstructor
public class AdminBannerController {

    private final BannerService bannerService;

    @Operation(summary = "获取所有轮播图")
    @GetMapping("/list")
    public R<List<Banner>> list() {
        LambdaQueryWrapper<Banner> queryWrapper = new LambdaQueryWrapper<Banner>()
                .orderByAsc(Banner::getSort);
        return R.ok(bannerService.list(queryWrapper));
    }

    @Operation(summary = "新增轮播图")
    @PostMapping("/add")
    public R<Void> add(@RequestBody Banner banner) {
        bannerService.save(banner);
        return R.ok();
    }

    @Operation(summary = "修改轮播图")
    @PutMapping("/update")
    public R<Void> update(@RequestBody Banner banner) {
        if (banner.getId() == null) {
            return R.error("ID不能为空");
        }
        bannerService.updateById(banner);
        return R.ok();
    }

    @Operation(summary = "删除轮播图")
    @DeleteMapping("/delete/{id}")
    public R<Void> delete(@PathVariable Long id) {
        bannerService.removeById(id);
        return R.ok();
    }
}
