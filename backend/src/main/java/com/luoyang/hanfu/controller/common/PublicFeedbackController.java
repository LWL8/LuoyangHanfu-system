package com.luoyang.hanfu.controller.common;

import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.entity.Feedback;
import com.luoyang.hanfu.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用接口 - 意见反馈提交
 */
@Tag(name = "通用模块 - 意见反馈")
@RestController
@RequestMapping("/api/common/feedback")
@RequiredArgsConstructor
public class PublicFeedbackController {

    private final FeedbackService feedbackService;

    @Operation(summary = "提交意见反馈")
    @PostMapping("/submit")
    public R<Void> submit(@RequestBody Feedback feedback) {
        feedbackService.save(feedback);
        return R.ok();
    }
}
