package com.luoyang.hanfu.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.entity.Feedback;
import com.luoyang.hanfu.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员端 - 意见反馈管理控制器
 */
@Tag(name = "管理员模块 - 意见反馈管理")
@RestController
@RequestMapping("/api/admin/feedback")
@RequiredArgsConstructor
public class AdminFeedbackController {

    private final FeedbackService feedbackService;

    @Operation(summary = "分页查询反馈列表")
    @GetMapping("/list")
    public R<Page<Feedback>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam(required = false) String type,
                                  @RequestParam(required = false) String status) {
        Page<Feedback> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Feedback> queryWrapper = new LambdaQueryWrapper<Feedback>()
                .eq(StrUtil.isNotBlank(type), Feedback::getType, type)
                .isNull("replied".equals(status), Feedback::getReplyContent)
                .isNotNull("pending".equals(status), Feedback::getReplyContent) // Logic might be switched in my head
                .orderByDesc(Feedback::getCreateTime);
        
        // Wait, "pending" should be isNull(replyContent), "replied" should be isNotNull.
        // Let's correct:
        LambdaQueryWrapper<Feedback> correctWrapper = new LambdaQueryWrapper<Feedback>()
                .eq(StrUtil.isNotBlank(type), Feedback::getType, type)
                .orderByDesc(Feedback::getCreateTime);
        
        if ("pending".equals(status)) {
            correctWrapper.isNull(Feedback::getReplyContent);
        } else if ("replied".equals(status)) {
            correctWrapper.isNotNull(Feedback::getReplyContent);
        }

        return R.ok(feedbackService.page(page, correctWrapper));
    }

    @Operation(summary = "回复并完成处理反馈")
    @PutMapping("/process/{id}")
    public R<Void> process(@PathVariable Long id, @RequestBody(required = false) Feedback feedback) {
        Feedback existing = feedbackService.getById(id);
        if (existing == null) return R.error("反馈不存在");
        
        if (feedback != null && StrUtil.isNotBlank(feedback.getReplyContent())) {
            existing.setReplyContent(feedback.getReplyContent());
        }
        feedbackService.updateById(existing);
        return R.ok();
    }

    @Operation(summary = "删除反馈")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        feedbackService.removeById(id);
        return R.ok();
    }
}
