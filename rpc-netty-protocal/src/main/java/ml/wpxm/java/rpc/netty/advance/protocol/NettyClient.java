package ml.wpxm.java.rpc.netty.advance.protocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import ml.wpxm.java.IRegistryService;
import ml.wpxm.java.ServiceInfo;
import ml.wpxm.java.rpc.netty.advance.core.RpcProtocol;
import ml.wpxm.java.rpc.netty.advance.core.RpcRequest;
import ml.wpxm.java.rpc.netty.advance.handler.RpcClientInitializer;

@Slf4j
public class NettyClient {

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup= new NioEventLoopGroup();
    private ChannelFuture channelFuture =null;
//    private String serverAddress;
//    private int serverPort;

//    public NettyClient(String serverAddress,int serverPort){
//        this.serverAddress = serverAddress;
//        this.serverPort = serverPort;
//        bootstrap = new Bootstrap();
//        bootstrap.group(eventLoopGroup)
//                .channel(NioSocketChannel.class)
//                .handler(new RpcClientInitializer());
//    }

    public NettyClient(){
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new RpcClientInitializer());
    }

    public void sendRequest(RpcProtocol<RpcRequest> rpcProtocol, IRegistryService registryService) throws Exception {
        // 曾犯错三：method是interface的方法名，即 只需method.getDeclaringClass().getName() 即可，不需要.getInterfaces()[0]
        ServiceInfo serviceInfo = registryService.discovery(rpcProtocol.getContent().getClassName());
        this.channelFuture = bootstrap.connect(serviceInfo.getServiceAddress(),serviceInfo.getServicePort()).sync();

        channelFuture.addListener(listener->{
            if(channelFuture.isSuccess()){
                log.info("connect rpc server {} sucess",serviceInfo.getServiceAddress());
            }else{
                log.error("connect rpc server {} failed",serviceInfo.getServiceAddress());
                channelFuture.cause().printStackTrace();
                eventLoopGroup.shutdownGracefully();
            }
        });

        log.info("begin transfer data");
        channelFuture.channel().writeAndFlush(rpcProtocol);
    }

    public void closeClient(){
        if(this.channelFuture != null){
            this.channelFuture.channel().closeFuture();
        }
    }
}
