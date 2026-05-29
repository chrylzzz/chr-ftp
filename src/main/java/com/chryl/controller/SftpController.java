package com.chryl.controller;

import com.chryl.util.SftpUtil2026;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileInputStream;

/**
 * Created by Chr.yl on 2026/5/29.
 *
 * @author Chr.yl
 */


@RestController
public class SftpController {

    @Resource
    private SftpUtil2026 sftpUtil;

    // 上传示例
    @GetMapping("/sftp/upload")
    public String upload() {
        try (FileInputStream fis = new FileInputStream("D:/test.txt")) {
            sftpUtil.upload("/sftp/sftpuser/upload/chryl/test.txt", fis);
            return "上传成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败：" + e.getMessage();
        }
    }
}