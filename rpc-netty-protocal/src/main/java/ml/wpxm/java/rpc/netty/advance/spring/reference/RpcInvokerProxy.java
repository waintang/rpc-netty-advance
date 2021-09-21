package ml.wpxm.java.rpc.netty.advance.spring.reference;

import io.netty.channel.DefaultEventLoop;
import ml.wpxm.java.rpc.netty.advance.constants.ReqType;
import ml.wpxm.java.rpc.netty.advance.constants.SerialType;
import ml.wpxm.java.rpc.netty.advance.core.*;
import ml.wpxm.java.rpc.netty.advance.protocol.NettyClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import io.netty.util.concurrent.*;

import static ml.wpxm.java.rpc.netty.advance.constants.RpcConstant.MAGIC;

public class RpcInvokerProxy implements InvocationHandler {
    private String host;
    private int port;

    public RpcInvokerProxy(String host ,int port){
        this.host = host;
        this.port = port;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setParams(args);

        long requestId= RequestHolder.REQUEST_ID.incrementAndGet();
        RpcProtocol rpcProtocal = new RpcProtocol();
        rpcProtocal.setHeader(new Header(MAGIC, SerialType.JSON_SERIAL.code(), ReqType.REQUEST.code(),requestId,0));
        rpcProtocal.setContent(rpcRequest);

        NettyClient nettyClient = new NettyClient(host,port);
        nettyClient.sendRequest(rpcProtocal);

        RpcFuture<RpcResponse> rpcFuture = new RpcFuture<>(new DefaultPromise<RpcResponse>(new DefaultEventLoop()));
        RequestHolder.REQUEST_MAP.put(requestId,rpcFuture);
        // todo 什么时候关闭 客户端连接？
        nettyClient.closeClient();

        // todo 此处 阻塞、响应的原理是啥？
        return rpcFuture.getPromise().get().getData();
    }
}
