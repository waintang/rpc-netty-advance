package ml.wpxm.java.rpc.netty.advance.spring.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "wp.rpc")
public class RpcServerProperties {

    private String serverHost;
    private int serverPort;
}
