package ml.wpxm.java.rpc.netty.advance.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ml.wpxm.java.rpc.netty.advance.core.Header;
import ml.wpxm.java.rpc.netty.advance.core.RpcProtocol;
import ml.wpxm.java.rpc.netty.advance.core.RpcRequest;
import ml.wpxm.java.rpc.netty.advance.core.RpcResponse;
import ml.wpxm.java.rpc.netty.advance.spring.SpringBeanManager;
import ml.wpxm.java.rpc.netty.advance.spring.service.ProviderMediator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static ml.wpxm.java.rpc.netty.advance.constants.ReqType.RESPONSE;

/**
 * 按目前 我的理解：
 * 服务端 接收的是 请求，此处处理完，返回Response
 *
 * 此前 已经 decode
 * 此后 会自动 encode
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcRequest> rpcRequestRpcProtocol) throws Exception {
        RpcProtocol<RpcResponse> resProtocol = new RpcProtocol<>();
        Header header = rpcRequestRpcProtocol.getHeader();
        header.setReqType(RESPONSE.code());
//        Object response = invoke(rpcRequestRpcProtocol.getContent());
        Object response = ProviderMediator.getInstance().process(rpcRequestRpcProtocol.getContent());
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setData(response);
        rpcResponse.setMsg("success.");
        // header五参数，其三不变 magic、serialType、requestId  其一reqType 必为响应 其一后续encode时，才能确认
        resProtocol.setHeader(header);
        resProtocol.setContent(rpcResponse);
        // netty会自动 调用 encoder方法 进行加密
        channelHandlerContext.writeAndFlush(resProtocol);
    }

    @Deprecated
    private Object invoke(RpcRequest rpcRequest){
        try {
            Class<?> clz = Class.forName(rpcRequest.getClassName());
            Object bean = SpringBeanManager.getBean(clz);
            Method method = clz.getDeclaredMethod(rpcRequest.getMethodName(),rpcRequest.getParameterTypes());
            return method.invoke(bean,rpcRequest.getParams());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
