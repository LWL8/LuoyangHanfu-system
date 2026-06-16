package com.luoyang.hanfu.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.entity.Notice;
import com.luoyang.hanfu.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员端 - 公告管理控制器
 */
@Tag(name = "管理员模块 - 公告管理")
@RestController
@RequestMapping("/api/admin/notice")
@RequiredArgsConstructor
public class AdminNoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "分页查询公告")
    @GetMapping("/list")
    public R<Page<Notice>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(required = false) String title,
                                @RequestParam(required = false) String type) {
        Page<Notice> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<Notice>()
                .like(StrUtil.isNotBlank(title), Notice::getTitle, title)
                .eq(StrUtil.isNotBlank(type), Notice::getType, type)
                .orderByDesc(Notice::getCreateTime);

        return R.ok(noticeService.page(page, queryWrapper));
    }

    @Operation(summary = "新增公告")
    @PostMapping("/add")
    public R<Void> add(@RequestBody Notice notice) {
        notice.setViewCount(0);
        noticeService.save(notice);
        return R.ok();
    }

    @Operation(summary = "修改公告")
    @PutMapping("/update")
    public R<Void> update(@RequestBody Notice notice) {
        noticeService.updateById(notice);
        return R.ok();
    }

    @Operation(summary = "删除公告")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        noticeService.removeById(id);
        return R.ok();
    }
}
