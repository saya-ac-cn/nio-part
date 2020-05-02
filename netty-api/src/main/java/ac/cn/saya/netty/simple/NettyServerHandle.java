package ac.cn.saya.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

/**
 * @Title: NettyServerHandle
 * @ProjectName nio-part
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2020-05-01 19:53
 * @Description:
 */

public class NettyServerHandle extends ChannelInboundHandlerAdapter {

    /**
     * 读取实际数据
     * @param ctx 上下文对象，含有pipeline，channel，地址
     * @param msg 客户端放的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 异步执行案例
//        ctx.channel().eventLoop().execute(()->{
//            try {
//                TimeUnit.SECONDS.sleep(10);
//                ctx.writeAndFlush(Unpooled.copiedBuffer("迟到10s后到达的消息",CharsetUtil.UTF_8));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
        System.out.println("server ctx："+ctx);
        ByteBuf buffer = (ByteBuf) msg;
        System.out.println("客户端发送的消息："+buffer.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址："+ctx.channel().remoteAddress());
    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello welcome",CharsetUtil.UTF_8));
    }

    /**
     * 发生异常，及时关闭
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
