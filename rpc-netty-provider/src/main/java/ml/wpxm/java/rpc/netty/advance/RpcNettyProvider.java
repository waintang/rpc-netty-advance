package ml.wpxm.java.rpc.netty.advance;

import ml.wpxm.java.rpc.netty.advance.protocol.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

// 曾犯错一：ComponentScan 也要包括当前文件夹
@ComponentScan(basePackages = {"ml.wpxm.java.rpc.netty.advance.spring.service","ml.wpxm.java.rpc.netty.advance.service"})
@SpringBootApplication
public class RpcNettyProvider {
    public static void main(String[] args) {
        SpringApplication.run(RpcNettyProvider.class,args);
//        new NettyServer("127.0.0.1",8888).startNettyServer();
    }
}
