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

import com.example.spm.pojo.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

// 根据路径返回图片的接口
    @GetMapping("/get")
    public ResponseEntity<Resource> getImage(
            @RequestParam("path") String path) {

        // 验证路径是否为空
        if (StringUtils.isEmpty(path)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // 构建文件的绝对路径
        Path targetPath = Paths.get(uploadDir, path).toAbsolutePath().normalize();
        File file = targetPath.toFile();

        // 检查文件是否存在
        if (!file.exists() || !file.isFile()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // 检查文件是否是图片（可选）
        String extension = getFileExtension(path);
        if (!isImageExtension(extension)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // 创建资源对象
        try {
            Resource resource = new UrlResource(targetPath.toUri());
            if (resource.exists()) {
                // 设置响应头
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"");
                headers.add(HttpHeaders.CONTENT_TYPE, getMediaTypeForFile(file));

                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(file.length())
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 获取文件扩展名
    private String getFileExtension(String path) {
        if (path == null) {
            return null;
        }
        int lastDotIndex = path.lastIndexOf('.');
        if (lastDotIndex <= 0) {
            return null;
        }
        return path.substring(lastDotIndex + 1).toLowerCase();
    }

    // 检查是否是图片扩展名
    private boolean isImageExtension(String extension) {
        if (extension == null) {
            return false;
        }
        return extension.matches("jpg|jpeg|png|gif|bmp|webp");
    }

    // 根据文件类型动态设置 Content-Type
    private String getMediaTypeForFile(File file) {
        String mimeType = "application/octet-stream"; // 默认值
        String fileName = file.getName();
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            mimeType = MediaType.IMAGE_JPEG_VALUE;
        } else if (fileName.endsWith(".png")) {
            mimeType = MediaType.IMAGE_PNG_VALUE;
        } else if (fileName.endsWith(".gif")) {
            mimeType = MediaType.IMAGE_GIF_VALUE;
        } else if (fileName.endsWith(".bmp")) {
            mimeType = "image/bmp";
        } else if (fileName.endsWith(".webp")) {
            mimeType = "image/webp";
        }
        return mimeType;
    }
}