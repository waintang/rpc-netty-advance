package ml.wpxm.java.rpc.netty.advance.spring.service;

import ml.wpxm.java.IRegistryService;
import ml.wpxm.java.RegistryFactory;
import ml.wpxm.java.RegistryType;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RpcServerProperties.class)
public class RpcServerAutoConfiguration {

    @Bean
    public SpringRpcProviderBean springRpcProviderBean(RpcServerProperties rpcServerProperties){
        IRegistryService registryService = RegistryFactory.createRegistryService(rpcServerProperties.getRegistryAddress(), RegistryType.findByCode(rpcServerProperties.getRegistryType()));
        return new SpringRpcProviderBean(rpcServerProperties.getServerHost(),rpcServerProperties.getServerPort(),registryService);
    }
}
