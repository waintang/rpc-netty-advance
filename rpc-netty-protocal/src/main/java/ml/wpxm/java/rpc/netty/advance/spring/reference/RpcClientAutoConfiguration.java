package ml.wpxm.java.rpc.netty.advance.spring.reference;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class RpcClientAutoConfiguration implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public SpringRpcReferencePostProcessor springRpcReferencePostProcessor(){
        RpcClientProperties rc=new RpcClientProperties();
        rc.setServerHost(environment.getProperty("wp.rpc.serverHost"));
        rc.setServerPort(Integer.parseInt(environment.getProperty("wp.rpc.serverPort")));
        return new SpringRpcReferencePostProcessor(rc);
    }
}
