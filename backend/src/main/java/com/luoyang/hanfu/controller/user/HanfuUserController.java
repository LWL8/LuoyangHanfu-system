package com.luoyang.hanfu.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.entity.Hanfu;
import com.luoyang.hanfu.service.HanfuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 用户端 - 汉服浏览控制器
 * 
 * @author Backend Architect
 */
@Tag(name = "用户模块 - 汉服展示")
@RestController
@RequestMapping("/api/user/hanfu")
@RequiredArgsConstructor
public class HanfuUserController {

    private final HanfuService hanfuService;

    @Operation(summary = "分页查询汉服列表")
    @GetMapping("/list")
    public R<Page<Hanfu>> list(@RequestParam(defaultValue = "1") Integer current,
                               @RequestParam(defaultValue = "10") Integer size,
                               @RequestParam(required = false) String dynasty,
                               @RequestParam(required = false) String keyword) {
        Page<Hanfu> page = new Page<>(current, size);
        LambdaQueryWrapper<Hanfu> queryWrapper = new LambdaQueryWrapper<Hanfu>()
                .eq(Hanfu::getOnShelf, 1)
                .eq(StringUtils.hasText(dynasty), Hanfu::getDynasty, dynasty)
                .like(StringUtils.hasText(keyword), Hanfu::getName, keyword)
                .orderByDesc(Hanfu::getHotScore);
        
        return R.ok(hanfuService.page(page, queryWrapper));
    }

    @Operation(summary = "获取汉服详情")
    @GetMapping("/{id}")
    public R<Hanfu> getById(@PathVariable Long id) {
        return R.ok(hanfuService.getById(id));
    }
}
