package ml.wpxm.java.rpc.netty.advance.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import ml.wpxm.java.rpc.netty.advance.handler.RpcServerInitializer;

@Slf4j
public class NettyServer {

    private String serverHost;
    private int serverPort;

    public NettyServer(String serverHost, int serverPort){
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void startNettyServer(){
        log.info("begin start Netty server.");
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss,worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new RpcServerInitializer());
        try {
            ChannelFuture channelFuture = bootstrap.bind(this.serverHost,this.serverPort);
            log.info("Server started Success on port:{}",this.serverPort);
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
        log.info("end Netty server.");
    }
}
