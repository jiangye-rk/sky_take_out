package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")

@Slf4j
public class CommonController {
    @Value("${file.upload.path:./uploads/}")
    private String uploadPath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);

        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }

            // 原始文件名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                return Result.error("文件名不能为空");
            }

            // 截取原始文件名的后缀
            String extension = "";
            if (originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 生成唯一文件名
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String fileName = uuid + extension;

            // 按日期创建目录
            String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String fullPath = uploadPath + datePath;

            // 创建目录
            File dir = new File(fullPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 保存文件
            Path filePath = Paths.get(fullPath, fileName);
            Files.write(filePath, file.getBytes());

            // 构建访问URL - 使用相对路径
            String accessUrl = "/upload/" + datePath + "/" + fileName;

            log.info("文件上传成功，保存路径：{}", filePath.toString());
            return Result.success(accessUrl);

        } catch (IOException e) {
            log.error("文件上传失败：{}", e.getMessage(), e);
            return Result.error(MessageConstant.UPLOAD_FAILED);
        } catch (Exception e) {
            log.error("文件上传异常：{}", e.getMessage(), e);
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }
}
