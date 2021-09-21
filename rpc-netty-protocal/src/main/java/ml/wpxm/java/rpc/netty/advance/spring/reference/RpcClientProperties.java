package ml.wpxm.java.rpc.netty.advance.spring.reference;

import lombok.Data;

@Data
public class RpcClientProperties {

    //不再写死 服务方地址
//    private String serverHost="127.0.0.1";
//    private int serverPort = 8888;

    private String registryAddress = "127.0.0.1:2181";
    private byte registryType = 0;
}
