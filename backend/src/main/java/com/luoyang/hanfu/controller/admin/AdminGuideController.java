package com.luoyang.hanfu.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.entity.Guide;
import com.luoyang.hanfu.service.GuideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理员端 - 洛阳攻略管理控制器
 */
@Tag(name = "管理员模块 - 攻略管理")
@RestController
@RequestMapping("/api/admin/guide")
@RequiredArgsConstructor
public class AdminGuideController {

    private final GuideService guideService;

    @Operation(summary = "分页查询策略")
    @GetMapping("/list")
    public R<Page<Guide>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(required = false) String style) {
        Page<Guide> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Guide> queryWrapper = new LambdaQueryWrapper<Guide>()
                .like(StrUtil.isNotBlank(style), Guide::getStyle, style)
                .orderByDesc(Guide::getCreateTime);

        return R.ok(guideService.page(page, queryWrapper));
    }

    @Operation(summary = "新增攻略")
    @PostMapping("/add")
    public R<Void> add(@RequestBody Guide guide) {
        guideService.save(guide);
        return R.ok();
    }

    @Operation(summary = "修改攻略")
    @PutMapping("/update")
    public R<Void> update(@RequestBody Guide guide) {
        if (guide.getId() == null) {
            return R.error("ID不能为空");
        }
        guideService.updateById(guide);
        return R.ok();
    }

    @Operation(summary = "删除攻略")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        guideService.removeById(id);
        return R.ok();
    }

    @Operation(summary = "根据ID获取详情")
    @GetMapping("/{id}")
    public R<Guide> getById(@PathVariable Long id) {
        return R.ok(guideService.getById(id));
    }
}
