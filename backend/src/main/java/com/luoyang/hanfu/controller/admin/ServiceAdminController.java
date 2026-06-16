package com.luoyang.hanfu.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.entity.ServicePackage;
import com.luoyang.hanfu.service.ServicePackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理员端 - 附加服务管理控制器
 */
@Tag(name = "管理员模块 - 服务套餐管理")
@RestController
@RequestMapping("/api/admin/service")
@RequiredArgsConstructor
public class ServiceAdminController {

    private final ServicePackageService servicePackageService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Operation(summary = "获取所有服务套餐列表")
    @GetMapping("/list")
    public R<List<Map<String, Object>>> list() {
        List<ServicePackage> list = servicePackageService.list(
                new LambdaQueryWrapper<ServicePackage>().orderByDesc(ServicePackage::getUpdateTime)
        );
        
        List<Map<String, Object>> voList = list.stream().map(item -> {
            Map<String, Object> map = BeanUtil.beanToMap(item);
            // 适配前端 category 字段
            map.put("category", item.getType());
            // 适配前端 enabled 字段
            map.put("enabled", item.getStatus() == 1);
            // 适配前端 updatedAt 字段
            if (item.getUpdateTime() != null) {
                map.put("updatedAt", item.getUpdateTime().format(DATE_FORMATTER));
            }
            return map;
        }).collect(Collectors.toList());
        
        return R.ok(voList);
    }

    @Operation(summary = "新增服务套餐")
    @PostMapping("/add")
    public R<Void> add(@RequestBody Map<String, Object> params) {
        ServicePackage servicePackage = new ServicePackage();
        servicePackage.setName((String) params.get("name"));
        servicePackage.setSubtitle((String) params.get("subtitle"));
        // 支持前端传 category 或 type
        String category = params.containsKey("category") ? (String) params.get("category") : (String) params.get("type");
        servicePackage.setType(category);
        
        Object priceObj = params.get("price");
        if (priceObj != null) {
            servicePackage.setPrice(new BigDecimal(priceObj.toString()));
        }
        
        servicePackage.setDescription((String) params.get("description"));
        // 显式设置图片字段
        servicePackage.setImage((String) params.get("image"));
        
        // 状态处理
        Object enabled = params.get("enabled");
        if (enabled != null) {
            servicePackage.setStatus(Boolean.TRUE.equals(enabled) ? 1 : 0);
        } else {
            Object status = params.get("status");
            servicePackage.setStatus(status != null ? Integer.parseInt(status.toString()) : 1);
        }

        servicePackage.setDeleted(0);
        servicePackageService.save(servicePackage);
        return R.ok();
    }

    @Operation(summary = "修改服务套餐")
    @PutMapping("/update")
    public R<Void> update(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        ServicePackage servicePackage = servicePackageService.getById(id);
        if (servicePackage == null) {
            return R.error("套餐不存在");
        }

        if (params.containsKey("name")) servicePackage.setName((String) params.get("name"));
        if (params.containsKey("subtitle")) servicePackage.setSubtitle((String) params.get("subtitle"));
        if (params.containsKey("category")) servicePackage.setType((String) params.get("category"));
        if (params.containsKey("type")) servicePackage.setType((String) params.get("type"));
        
        if (params.containsKey("price")) {
            servicePackage.setPrice(new BigDecimal(params.get("price").toString()));
        }
        
        if (params.containsKey("description")) servicePackage.setDescription((String) params.get("description"));
        // 显式更新图片字段
        if (params.containsKey("image")) servicePackage.setImage((String) params.get("image"));
        
        if (params.containsKey("enabled")) {
            servicePackage.setStatus(Boolean.TRUE.equals(params.get("enabled")) ? 1 : 0);
        } else if (params.containsKey("status")) {
            servicePackage.setStatus(Integer.parseInt(params.get("status").toString()));
        }

        servicePackageService.updateById(servicePackage);
        return R.ok();
    }

    @Operation(summary = "删除服务套餐")
    @DeleteMapping("/delete/{id}")
    public R<Void> delete(@PathVariable Long id) {
        servicePackageService.removeById(id);
        return R.ok();
    }
    @GetMapping("/fix-db")
    public R<String> fixDb() {
        try {
            // 尝试增加 image 列
            try {
                servicePackageService.getBaseMapper().delete(new LambdaQueryWrapper<ServicePackage>().last("LIMIT 0")); 
                // 上面一行只是为了让 MP 初始化，下面执行原生 SQL
            } catch (Exception ignore) {}
            
            String[] sqls = {
                "ALTER TABLE service_package ADD COLUMN IF NOT EXISTS image varchar(255) COMMENT '图片' AFTER description",
                "ALTER TABLE service_package ADD COLUMN IF NOT EXISTS subtitle varchar(200) COMMENT '副标题' AFTER name"
            };
            
            for (String sql : sqls) {
                try {
                    // 使用原生 SQL 执行
                    servicePackageService.getBaseMapper().insert(null); // 触发一下
                } catch (Exception e) {
                    // 忽略错误，因为我们是通过这种方式触发
                }
            }
            // 真正执行
            java.sql.Connection conn = java.sql.DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/luoyang_hanfu?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC&useSSL=false",
                "root", "liwenlong"
            );
            java.sql.Statement stmt = conn.createStatement();
            stmt.execute("ALTER TABLE service_package ADD COLUMN IF NOT EXISTS image varchar(255) COMMENT '图片' AFTER description");
            stmt.execute("ALTER TABLE service_package ADD COLUMN IF NOT EXISTS subtitle varchar(200) COMMENT '副标题' AFTER name");
            stmt.close();
            conn.close();
            
            return R.ok("数据库结构修复完成");
        } catch (Exception e) {
            return R.error("修复失败: " + e.getMessage());
        }
    }
}
