package com.luoyang.hanfu.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.entity.Hanfu;
import com.luoyang.hanfu.service.HanfuService;
import com.luoyang.hanfu.vo.HanfuAdminVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理员端 - 汉服管理控制器
 * 已适配前端 AdminHanfuList.vue 的字段命名习惯
 */
@Tag(name = "管理员模块 - 汉服管理")
@RestController
@RequestMapping("/api/admin/hanfu")
@RequiredArgsConstructor
public class HanfuAdminController {

    private final HanfuService hanfuService;

    @Operation(summary = "分页查询汉服")
    @GetMapping("/list")
    public R<Page<HanfuAdminVO>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                      @RequestParam(required = false) String hanfuName,
                                      @RequestParam(required = false) String hanfuTypes) {
        Page<Hanfu> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Hanfu> queryWrapper = new LambdaQueryWrapper<Hanfu>()
                .like(StrUtil.isNotBlank(hanfuName), Hanfu::getName, hanfuName)
                .eq(StrUtil.isNotBlank(hanfuTypes), Hanfu::getDynasty, hanfuTypes)
                .orderByDesc(Hanfu::getCreateTime);

        Page<Hanfu> hanfuPage = hanfuService.page(page, queryWrapper);
        
        // 转换为 VO 适配前端
        Page<HanfuAdminVO> voPage = new Page<>(hanfuPage.getCurrent(), hanfuPage.getSize(), hanfuPage.getTotal());
        List<HanfuAdminVO> voList = hanfuPage.getRecords().stream().map(h -> {
            HanfuAdminVO vo = new HanfuAdminVO();
            vo.setId(h.getId());
            vo.setHanfuName(h.getName());
            vo.setHanfuTypes(h.getDynasty());
            vo.setHanfuPrice(h.getRentPrice());
            vo.setHanfuNewMoney(BigDecimal.valueOf(500)); // 默认押金，表结构无此字段
            vo.setShangjiaTypes(h.getOnShelf() == 1 ? 1 : 2); // 1-上架, 2-下架
            vo.setStock(h.getStock());
            vo.setHanfuContent(h.getDescription());
            
            // 取第一张图作为封面
            List<String> images = h.getImageList();
            if (images != null && !images.isEmpty()) {
                vo.setHanfuPhoto(images.get(0));
            }
            return vo;
        }).collect(Collectors.toList());
        
        voPage.setRecords(voList);
        return R.ok(voPage);
    }

    @Operation(summary = "新增汉服")
    @PostMapping("/add")
    public R<Void> add(@RequestBody Map<String, Object> params) {
        Hanfu hanfu = new Hanfu();
        hanfu.setName((String) params.get("hanfuName"));
        hanfu.setDynasty((String) params.get("hanfuTypes"));
        hanfu.setRentPrice(new BigDecimal(params.get("hanfuPrice").toString()));
        hanfu.setDescription((String) params.get("hanfuContent"));
        Number stock = (Number) params.getOrDefault("stock", 0);
        hanfu.setStock(stock.intValue());
        hanfu.setOnShelf(1); // 默认上架
        
        // 处理单张图片为图片列表
        String photo = (String) params.get("hanfuPhoto");
        if (StrUtil.isNotBlank(photo)) {
            hanfu.setImageList(List.of(photo));
        }
        
        hanfuService.save(hanfu);
        return R.ok();
    }

    @Operation(summary = "修改汉服")
    @PutMapping("/update")
    public R<Void> update(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        Hanfu hanfu = hanfuService.getById(id);
        if (hanfu == null) return R.error("汉服不存在");
        
        if (params.containsKey("hanfuName")) hanfu.setName((String) params.get("hanfuName"));
        if (params.containsKey("hanfuTypes")) hanfu.setDynasty((String) params.get("hanfuTypes"));
        if (params.containsKey("hanfuPrice")) hanfu.setRentPrice(new BigDecimal(params.get("hanfuPrice").toString()));
        if (params.containsKey("hanfuContent")) hanfu.setDescription((String) params.get("hanfuContent"));
        if (params.containsKey("stock")) hanfu.setStock(((Number) params.get("stock")).intValue());
        if (params.containsKey("shangjiaTypes")) {
            hanfu.setOnShelf(Integer.valueOf(params.get("shangjiaTypes").toString()) == 1 ? 1 : 0);
        }
        
        if (params.containsKey("hanfuPhoto")) {
            String photo = (String) params.get("hanfuPhoto");
            if (StrUtil.isNotBlank(photo)) {
                hanfu.setImageList(List.of(photo));
            }
        }
        
        hanfuService.updateById(hanfu);
        return R.ok();
    }

    @Operation(summary = "删除汉服")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        hanfuService.removeById(id);
        return R.ok();
    }

    @Operation(summary = "切换上下架状态")
    @PostMapping("/toggle/{id}")
    public R<Void> toggle(@PathVariable Long id) {
        Hanfu hanfu = hanfuService.getById(id);
        if (hanfu != null) {
            hanfu.setOnShelf(hanfu.getOnShelf() == 1 ? 0 : 1);
            hanfuService.updateById(hanfu);
        }
        return R.ok();
    }
}
