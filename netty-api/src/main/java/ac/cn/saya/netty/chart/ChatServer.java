package ac.cn.saya.netty.chart;

import ac.cn.saya.netty.simple.NettyServerHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @Title: ChatServer
 * @ProjectName nio-part
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2020-05-02 17:07
 * @Description:
 */

public class ChatServer {

    public int PORT;

    public ChatServer(int PORT) {
        this.PORT = PORT;
    }

    public void run(){
        NioEventLoopGroup boosGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            // 使用链式的方式进行配置
            bootstrap.group(boosGroup, workGroup)// 设置两个线程组
                    .channel(NioServerSocketChannel.class)// 使用NioServerSocketChannel作为服务器通道的实现
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .handler(new LoggingHandler(LogLevel.INFO))// 设置bossGroup的日志级别
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel _channel) throws Exception {
                            // 加入 string 编码 解码器
                            _channel.pipeline().addLast("MyStringEncoder",new StringEncoder());
                            _channel.pipeline().addLast("MyStringDecoder",new StringDecoder());
                            // 加入自定义的处理器
                            _channel.pipeline().addLast("MyChatHandle",new ChatHandle());
                            /**
                             * IdleStateHandler 是netty提供的一个空闲处理器
                             *  readerIdleTime,多长时间没有读，就会发送一个心跳检测包检测是否连接
                             *  writerIdleTime,多长时间没有写，就会发送一个心跳检测包检测是否连接
                             *  allIdleTime,多长时间没有读写，就会发送一个心跳检测包检测是否连接
                             * 当IdleStateHandler 触发后，就会传递给管道的下一个handler去处理，通过调用下一个handler的userEventTiggered，在该方法中去处理
                             */
                            _channel.pipeline().addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                            // 加入一个队空闲检测的处理器
                            _channel.pipeline().addLast(new IdleStateHandle());
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

    public static void main(String[] args) {
        ChatServer server = new ChatServer(9000);
        server.run();
    }

}
