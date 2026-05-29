package com.chryl.controller;

import com.chryl.util.SftpUtil2026;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpATTRS;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by Chr.yl on 2026/5/29.
 *
 * @author Chr.yl
 */


@RestController
@RequestMapping("sftp")
public class SftpController {

    @Resource
    private SftpUtil2026 sftpUtil;

    /**
     * 文件上传到 SFTP
     *
     * @param file 前端传的文件
     * @return 结果
     */
    @GetMapping("/upload")
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


    /**
     * 删除 SFTP 服务器上的文件
     *
     * @param filePath 要删除的文件路径（例：/home/upload/test.txt）
     * @return 删除结果
     */
    @DeleteMapping("/delete")
    public String deleteFile(@RequestParam("filePath") String filePath) throws JSchException {
        ChannelSftp sftp = null;

        try {
            // 1. 路径判空
            if (StringUtils.isBlank(filePath)) {
                return "错误：文件路径不能为空";
            }

            // 2. 获取连接
            sftp = sftpUtil.getChannel();

            // 3. 判断文件是否存在（controller 里判断）
            SftpATTRS attrs;
            try {
                attrs = sftp.lstat(filePath);
            } catch (Exception e) {
                return "错误：文件不存在 → " + filePath;
            }

            // 4. 如果是目录，不让删（安全控制）
            if (attrs.isDir()) {
                return "错误：不能删除目录 → " + filePath;
            }

            // 5. 执行删除
            sftp.rm(filePath);

            return "删除成功：" + filePath;

        } catch (Exception e) {
            e.printStackTrace();
            return "删除失败：" + e.getMessage();
        } finally {
            // 关闭连接
            if (sftp != null) {
                sftpUtil.disconnect(sftp);
            }
        }

    }

}