package ac.cn.saya.netty.rpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Title: RPCServer
 * @ProjectName nio-part
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2020-05-03 21:36
 * @Description: rpc 提供者
 */

public class RPCServer {

    public static void main(String[] args) {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            // 使用链式的方式进行配置
            bootstrap.group(boosGroup, workGroup)// 设置两个线程组
                    .channel(NioServerSocketChannel.class)// 使用NioServerSocketChannel作为服务器通道的实现
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel _channel) throws Exception {
                            _channel.pipeline().addLast(new StringDecoder());
                            _channel.pipeline().addLast(new StringEncoder());
                            // 自定义的义务处理
                            _channel.pipeline().addLast(new RPCServerHandler());

                        }
                    });// 设置绑定处理器

            ChannelFuture future = bootstrap.bind("127.0.0.1",9000).sync();

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
