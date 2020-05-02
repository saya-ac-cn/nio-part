package ac.cn.saya.netty.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Title: NettyServer
 * @ProjectName nio-part
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2020-05-02 19:40
 * @Description:
 */

public class NettyServer {


    public static void main(String[] args) {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            // 使用链式的方式进行配置
            bootstrap.group(boosGroup, workGroup)// 设置两个线程组
                    .channel(NioServerSocketChannel.class)// 使用NioServerSocketChannel作为服务器通道的实现
                    .handler(new LoggingHandler(LogLevel.INFO))// 设置bossGroup的日志级别
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel _channel) throws Exception {
                            // 因为是基于http协议，使用http的编码和解码器
                            _channel.pipeline().addLast(new HttpServerCodec());
                            // 是以块的方式写，添加ChunkedWriteHandler处理器
                            _channel.pipeline().addLast(new ChunkedWriteHandler());
                            // http数据在传输过程中是分段的，HttpObjectAggregator就是将多个分段聚合
                            _channel.pipeline().addLast(new HttpObjectAggregator(8192));
                            /**
                             * 对应websocket，它的数据是以帧的形式传递
                             * 浏览器请求时 ws://localhost:9000/home 表示请求的url
                             * WebSocketServerProtocolHandler 核心功能是将http协议升级为ws协议，保持长连接
                             */
                            _channel.pipeline().addLast(new WebSocketServerProtocolHandler("/home"));
                            // 自定义的义务处理
                            _channel.pipeline().addLast(new WebSocketHandler());

                        }
                    });// 设置绑定处理器

            ChannelFuture future = bootstrap.bind(9000).sync();

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
