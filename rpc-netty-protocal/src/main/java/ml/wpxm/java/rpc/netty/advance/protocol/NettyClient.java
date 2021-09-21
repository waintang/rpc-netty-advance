package ml.wpxm.java.rpc.netty.advance.protocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import ml.wpxm.java.rpc.netty.advance.core.RpcProtocol;
import ml.wpxm.java.rpc.netty.advance.core.RpcRequest;
import ml.wpxm.java.rpc.netty.advance.handler.RpcClientInitializer;

@Slf4j
public class NettyClient {

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup= new NioEventLoopGroup();
    private ChannelFuture channelFuture =null;
    private String serverAddress;
    private int serverPort;

    public NettyClient(String serverAddress,int serverPort){
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new RpcClientInitializer());
    }

    public void sendRequest(RpcProtocol<RpcRequest> rpcProtocol) throws InterruptedException {
        this.channelFuture = bootstrap.connect(this.serverAddress,this.serverPort).sync();

        channelFuture.addListener(listener->{
            if(channelFuture.isSuccess()){
                log.info("connect rpc server {} sucess",this.serverAddress);
            }else{
                log.error("connect rpc server {} failed",this.serverAddress);
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
