package ac.cn.saya.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @Title: HttpServerHandle
 * @ProjectName nio-part
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2020-05-02 09:24
 * @Description: SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter的子类
 * HttpObject 客户端和服务器间相互通信的数据封装成HttpObject
 */

public class HttpServerHandle extends SimpleChannelInboundHandler<HttpObject> {


    /**
     * 读取客户端数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpObject) {

//            HttpRequest httpRequest = (HttpRequest) msg;
//            String url = new URI(httpRequest.uri()).getPath();
//            if ("/favicon.ico".equals(url)) {
//                // 请求网站图标不予处理
//                return;
//            }
            System.out.println("msg 类型：" + msg.getClass());
            System.out.println("客户端地址：" + ctx.channel().remoteAddress());
            // 回复信息给浏览器【http协议】
            ByteBuf content = Unpooled.copiedBuffer("来自服务器的响应", CharsetUtil.UTF_8);
            // 构造一个http的响应，即，Httpresponse
            FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            // 将构件号的response返回
            ctx.writeAndFlush(httpResponse);
        }
    }
}
