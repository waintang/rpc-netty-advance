package ml.wpxm.java.rpc.netty.advance.spring.reference;

import lombok.Data;

@Data
public class RpcClientProperties {

    private String serverHost="127.0.0.1";
    private int serverPort = 8888;
}
