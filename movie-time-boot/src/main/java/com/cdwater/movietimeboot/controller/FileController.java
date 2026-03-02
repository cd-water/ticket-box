package com.cdwater.movietimeboot.controller;

import com.cdwater.movietimecommon.Result;
import com.cdwater.movietimecommon.enums.RetEnum;
import com.cdwater.movietimecommon.exceptions.BusinessException;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        //文件为空
        if (file == null) {
            throw new BusinessException(RetEnum.UPLOAD_FAIL);
        }
        //生成新文件名
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String objectName = date + "/" + UUID.randomUUID() + extension;

        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
        } catch (Exception e) {
            throw new BusinessException(RetEnum.UPLOAD_FAIL);
        }
        //文件访问URL
        String fileUrl = endpoint + "/" + bucketName + "/" + objectName;
        return Result.success(fileUrl);
    }
}
