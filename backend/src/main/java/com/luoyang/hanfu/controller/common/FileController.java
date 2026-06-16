package com.luoyang.hanfu.controller.common;

import com.luoyang.hanfu.common.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 通用 - 文件上传控制器
 * 
 * @author Backend Architect
 */
@Slf4j
@Tag(name = "通用模块 - 文件管理")
@RestController
@RequestMapping("/api/common")
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Operation(summary = "单文件上传")
    @PostMapping("/upload")
    public R<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return R.error("文件不能为空");
        }

        try {
            // 确定绝对路径
            String absolutePath = Paths.get(uploadDir).toAbsolutePath().toString();
            File dir = new File(absolutePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.lastIndexOf(".") != -1) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + extension;

            // 保存文件
            File targetFile = new File(dir, fileName);
            file.transferTo(targetFile);

            log.info("文件上传成功: {}", targetFile.getAbsolutePath());
            
            // 返回访问路径 (根据 application.yml 中的 static-locations 配置)
            return R.ok("/uploads/hanfu/" + fileName);
            
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return R.error("文件上传发生错误");
        }
    }
}
