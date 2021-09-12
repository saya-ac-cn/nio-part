package ac.cn.saya.netty.heart;
import java.util.concurrent.TimeUnit;

import ac.cn.saya.netty.socket.WebSocketHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
/**
 * @Title: HeartNettyServerFilter
 * @ProjectName nio-part
 * @Description: TODO
 * @Author liunengkai
 * @Date: 9/13/21 05:47
 * @Description:
 * 服务端过滤器，如编解码和心跳的设置
 */

public class HeartNettyServerFilter extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        ChannelPipeline cp = sc.pipeline();
        // 因为是基于http协议，使用http的编码和解码器
        cp.addLast(new HttpServerCodec());
        // 是以块的方式写，添加ChunkedWriteHandler处理器
        cp.addLast(new ChunkedWriteHandler());
        // http数据在传输过程中是分段的，HttpObjectAggregator就是将多个分段聚合
        cp.addLast(new HttpObjectAggregator(8192));
        cp.addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));

        // 解码和编码，应和客户端一致
        cp.addLast(new StringDecoder());
        cp.addLast(new StringEncoder());
        /**
         * 对应websocket，它的数据是以帧的形式传递
         * 浏览器请求时 ws://localhost:9000/home 表示请求的url
         * WebSocketServerProtocolHandler 核心功能是将http协议升级为ws协议，保持长连接
         */
        cp.addLast(new WebSocketServerProtocolHandler("/home"));
        // 处理服务端的业务逻辑
        cp.addLast(new HeartNettyServerHandler());
    }
}

