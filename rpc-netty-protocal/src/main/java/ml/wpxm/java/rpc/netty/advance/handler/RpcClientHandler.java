package ml.wpxm.java.rpc.netty.advance.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import ml.wpxm.java.rpc.netty.advance.core.RequestHolder;
import ml.wpxm.java.rpc.netty.advance.core.RpcFuture;
import ml.wpxm.java.rpc.netty.advance.core.RpcProtocol;
import ml.wpxm.java.rpc.netty.advance.core.RpcResponse;

/**
 * 按我理解：
 * 客户端 接收的是  Response，不需要再后续处理（不需要再response）
 */
@Slf4j
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> rpcResponseRpcProtocol) throws Exception {
        log.info("receive Rpc server Result");
        long requestId = rpcResponseRpcProtocol.getHeader().getRequestId();
        RpcFuture<RpcResponse> future = RequestHolder.REQUEST_MAP.remove(requestId);
        future.getPromise().setSuccess(rpcResponseRpcProtocol.getContent());
    }
}
