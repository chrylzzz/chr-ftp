package com.chryl.util;

import com.chryl.config.SftpConfig;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

/**
 * Created by Chr.yl on 2026/5/29.
 *
 * @author Chr.yl
 */

@Component
public class SftpUtil2026 {

    @Resource
    private SftpConfig sftpConfig;

    /**
     * 获取 SFTP 通道
     */
    public ChannelSftp getChannel() throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(
                sftpConfig.getUsername(),
                sftpConfig.getHost(),
                sftpConfig.getPort()
        );
        session.setPassword(sftpConfig.getPassword());

        // 跳过主机密钥检查
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(sftpConfig.getSessionTimeout());

        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect(sftpConfig.getChannelTimeout());
        return channel;
    }

    /**
     * 关闭连接
     */
    public void disconnect(ChannelSftp channel) throws JSchException {
        if (channel == null) return;
        Session session = channel.getSession();
        channel.disconnect();
        if (session != null) session.disconnect();
    }

    // ====================== 常用操作 ======================

    /**
     * 上传文件
     */
    public void upload(String remotePath, InputStream inputStream) throws Exception {
        ChannelSftp channel = getChannel();
        try {
            channel.put(inputStream, remotePath);
        } finally {
            disconnect(channel);
        }
    }

    /**
     * 下载文件
     */
    public void download(String remotePath, OutputStream outputStream) throws Exception {
        ChannelSftp channel = getChannel();
        try {
            channel.get(remotePath, outputStream);
        } finally {
            disconnect(channel);
        }
    }

    /**
     * 删除文件
     */
    public void delete(String remotePath) throws Exception {
        ChannelSftp channel = getChannel();
        try {
            channel.rm(remotePath);
        } finally {
            disconnect(channel);
        }
    }

    /**
     * 遍历目录文件
     */
    public Vector<ChannelSftp.LsEntry> listFiles(String dir) throws Exception {
        ChannelSftp channel = getChannel();
        try {
            return channel.ls(dir);
        } finally {
            disconnect(channel);
        }
    }
}