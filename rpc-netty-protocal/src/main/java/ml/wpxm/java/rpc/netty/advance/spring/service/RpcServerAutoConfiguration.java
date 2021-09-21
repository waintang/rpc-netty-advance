package ml.wpxm.java.rpc.netty.advance.spring.service;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RpcServerProperties.class)
public class RpcServerAutoConfiguration {

    @Bean
    public SpringRpcProviderBean springRpcProviderBean(RpcServerProperties rpcServerProperties){
        return new SpringRpcProviderBean(rpcServerProperties.getServerHost(),rpcServerProperties.getServerPort());
    }
}
