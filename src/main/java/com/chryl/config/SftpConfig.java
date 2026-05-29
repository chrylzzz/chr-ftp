package com.chryl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Chr.yl on 2026/5/29.
 *
 * @author Chr.yl
 */

@Component
@ConfigurationProperties(prefix = "sftp")
@Data
public class SftpConfig {
    private String host;
    private int port;
    private String username;
    private String password;
    private int sessionTimeout;
    private int channelTimeout;
}
