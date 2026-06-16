package com.luoyang.hanfu.controller.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.entity.Notice;
import com.luoyang.hanfu.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通用接口 - 公告展示
 */
@Tag(name = "通用模块 - 公告展示")
@RestController
@RequestMapping("/api/common/notices")
@RequiredArgsConstructor
public class PublicNoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "获取启用公告列表")
    @GetMapping("")
    public R<List<Notice>> list(@RequestParam(required = false) String type) {
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<Notice>()
                .eq(Notice::getEnabled, 1)
                .eq(type != null, Notice::getType, type)
                .orderByDesc(Notice::getCreateTime);
        return R.ok(noticeService.list(queryWrapper));
    }

    @Operation(summary = "获取公告详情")
    @GetMapping("/{id}")
    public R<Notice> getById(@PathVariable Long id) {
        Notice notice = noticeService.getById(id);
        if (notice != null) {
            // 简单阅读量增加
            notice.setViewCount(notice.getViewCount() + 1);
            noticeService.updateById(notice);
        }
        return R.ok(notice);
    }
}
