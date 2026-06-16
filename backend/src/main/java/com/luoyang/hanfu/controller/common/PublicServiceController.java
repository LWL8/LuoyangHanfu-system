package com.luoyang.hanfu.controller.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.entity.ServicePackage;
import com.luoyang.hanfu.service.ServicePackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公共接口 - 附加服务
 */
@Tag(name = "公共模块 - 服务套餐")
@RestController
@RequestMapping("/api/common/service")
@RequiredArgsConstructor
public class PublicServiceController {

    private final ServicePackageService servicePackageService;

    @Operation(summary = "获取所有上架的服务套餐")
    @GetMapping("/list")
    public R<List<ServicePackage>> list() {
        List<ServicePackage> list = servicePackageService.list(
                new LambdaQueryWrapper<ServicePackage>()
                        .ne(ServicePackage::getStatus, 0) // 只要不是明确下架的都显示
                        .orderByAsc(ServicePackage::getType)
        );
        return R.ok(list);
    }
}
