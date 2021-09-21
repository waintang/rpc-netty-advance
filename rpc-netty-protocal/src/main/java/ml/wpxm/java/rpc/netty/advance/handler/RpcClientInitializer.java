package ml.wpxm.java.rpc.netty.advance.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import ml.wpxm.java.rpc.netty.advance.codec.RpcDecoder;
import ml.wpxm.java.rpc.netty.advance.codec.RpcEncoder;

public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                12,
                4,
                0,
                0))
                .addLast(new RpcEncoder())
                .addLast(new RpcDecoder())
                .addLast(new RpcClientHandler());
    }
}
