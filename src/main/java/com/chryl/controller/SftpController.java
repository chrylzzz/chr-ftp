package com.chryl.controller;

import com.chryl.util.SftpUtil2026;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by Chr.yl on 2026/5/29.
 *
 * @author Chr.yl
 */


@RestController
public class SftpController {

    @Resource
    private SftpUtil2026 sftpUtil;

    /**
     * 文件上传到 SFTP
     *
     * @param file 前端传的文件
     * @return 结果
     */
    @GetMapping("/sftp/upload")
    public String upload(@RequestParam("file") MultipartFile file) {

        try {
            // 1. 判空
            if (file.isEmpty()) {
                return "请选择文件";
            }

            // 2. 生成唯一文件名（防止重复）
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uuidFileName = UUID.randomUUID() + suffix;

            // 3. SFTP 服务端路径（自己改）
            String remotePath = "/home/upload/" + uuidFileName;

            // 4. 直接用文件流上传到 SFTP
            sftpUtil.upload(remotePath, file.getInputStream());

            return "上传成功：" + remotePath;

        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败：" + e.getMessage();
        }

    }

}