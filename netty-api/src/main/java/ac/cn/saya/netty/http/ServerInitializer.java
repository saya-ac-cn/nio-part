package ac.cn.saya.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Title: ServerInitializer
 * @ProjectName nio-part
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2020-05-02 09:08
 * @Description:
 */

public class ServerInitializer  extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        // 向管道加入处理器
        // 得到管道
        ChannelPipeline pipeline = channel.pipeline();
        // 加入一个netty提供的httpServerCodec
        // httpServerCodec是netty提供的http编解码处理器
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        pipeline.addLast("MyHttpServerHandle",new HttpServerHandle());
    }
}
