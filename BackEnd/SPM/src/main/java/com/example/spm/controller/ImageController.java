package com.example.spm.controller;

import com.example.spm.pojo.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Value("${image.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public Result uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("usage") String usage,
            @RequestParam("index") int index,
            @RequestParam("for") String forId,
            @RequestHeader("Authorization") String authHeader) {

        Map<String, Object> response = new HashMap<>();

        // 参数校验
        if (file.isEmpty()) {
            return Result.error("File cannot be empty");
        }
        if (index < 0) {
            return Result.error("Index must be non-negative");
        }
        if (!usage.matches("^[a-zA-Z0-9_-]+$")) {
            return Result.error("Invalid usage parameter");
        }
        if (!forId.matches("^\\d+$")) {
            return Result.error("Invalid for parameter, must be numeric");
        }

        // 处理文件名
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (originalFileName.contains("..")) {
            return Result.error("Invalid file name");
        }
        String extension = originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf('.'))
                : "";
        String newFileName = "img" + index + extension;

        // 构建存储路径
        Path targetDir = Paths.get(uploadDir, usage, forId).toAbsolutePath().normalize();
        try {
            Files.createDirectories(targetDir);
        } catch (IOException e) {
            return Result.error("Failed to create directory");
        }

        // 保存文件
        Path filePath = targetDir.resolve(newFileName);
        try {
            file.transferTo(filePath);
        } catch (IOException e) {
            return Result.error("Failed to save file");
        }

        // 构造响应
        String relativePath = Paths.get(usage, forId, newFileName).toString().replace("\\", "/");
        Map<String, Object> data = new HashMap<>();
        data.put("filePath", relativePath);

        response.put("code", 0);
        response.put("message", "File uploaded successfully");
        response.put("data", data);

        return Result.success(response);
    }
}