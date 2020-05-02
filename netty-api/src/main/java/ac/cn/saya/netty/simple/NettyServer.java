package ac.cn.saya.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Title: NettyServer
 * @ProjectName nio-part
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2020-05-01 19:36
 * @Description:netty服务端
 */

public class NettyServer {

    public static void main(String[] args) {
        // 创建BossGroup 和 WorkGroup
        //说明：
        //1.创建两个线程池 BossGroup 和 WorkGroup
        //2.BossGroup只是处理连接请求，具体的和客户端业务处理，交由WorkGroup处理
        //3.两个都是无限循环
        //4.BossGroup 和 WorkGroup含有的子线程NioEventLoop的个数 默认：实际cpu核数*2
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            // 创建服务器的启动对象
            ServerBootstrap bootstrap = new ServerBootstrap();

            /**
             * .childHandler 和 .handler 的区别
             * .childHandler 作用在 workGroup上
             * .handler 作用在 boosGroup上
             */
            // 使用链式的方式进行配置
            bootstrap.group(boosGroup, workGroup)// 设置两个线程组
                    .channel(NioServerSocketChannel.class)// 使用NioServerSocketChannel作为服务器通道的实现
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel _channel) throws Exception {
                            _channel.pipeline().addLast(new NettyServerHandle());
                        }
                    });// 设置绑定处理器
            System.out.println("server is ready");

            // 绑定一个端口并且同步
            // 启动服务器
            ChannelFuture channelFuture = bootstrap.bind(9000).sync();
            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
