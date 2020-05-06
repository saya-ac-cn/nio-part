package ac.cn.saya.netty.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Title: RPCClient
 * @ProjectName nio-part
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2020-05-03 21:47
 * @Description:
 */

public class RPCClient {

    /**
     * 创建一个线程池
     */
    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static RPCClientHandler client;

    public Object getBean(final Class<?> sreviceClass,final String providerName){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class<?>[] {sreviceClass},(proxy,method,args)->{
            if (null == client){
                initClient();
            }
            client.setParam(args[0].toString());
            return executorService.submit(client).get();
        });
    }

    public static void initClient(){
        client = new RPCClientHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            // 使用链式的方式进行配置
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel _channel) throws Exception {
                            _channel.pipeline().addLast(new StringDecoder());
                            _channel.pipeline().addLast(new StringEncoder());
                            // 自定义的义务处理
                            _channel.pipeline().addLast(client);

                        }
                    });// 设置绑定处理器

            bootstrap.connect("127.0.0.1",9000).sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 不要加group.shutdownGracefully(); 否则提供者收不到消息，服务异常

    }
}