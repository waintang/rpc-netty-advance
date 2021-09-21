package ml.wpxm.java.rpc.netty.advance.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Header implements Serializable {

    private short magic; // 2
    private byte serialType; //序列化类型 1
    private byte reqType; //消息类型 1
    private long requestId; // 请求id 8
    private int length; // 消息体长度 4
}
