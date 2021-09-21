package ml.wpxm.java.rpc.netty.advance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"ml.wpxm.java.rpc.netty.advance.spring.reference","ml.wpxm.java.rpc.netty.advance.controller","ml.wpxm.java.rpc.netty.advance.annotation"})
@SpringBootApplication
public class RpcNettyConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RpcNettyConsumerApplication.class,args);
    }
}
