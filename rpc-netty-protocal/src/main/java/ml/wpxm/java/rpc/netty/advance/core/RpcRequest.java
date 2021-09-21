package ml.wpxm.java.rpc.netty.advance.core;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcRequest implements Serializable {
    private String className;
    private String methodName;
    private Object[] params;
    private Class<?>[] parameterTypes;
}
