package ac.cn.saya.netty.rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @Title: RPCClientHandler
 * @ProjectName nio-part
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2020-05-03 21:49
 * @Description:
 */

public class RPCClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;
    private String param;
    private String result;

    /**
     * @param ctx
     * @throws Exception
     * 第一个被调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    /**
     * @param ctx
     * @param msg
     * @throws Exception
     * 第四个被调用
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    /**
     * 被代理对象调用，发送数据给服务器，-》wait -》 等待被唤醒(channelRead)-》返回结果
     * 第三个被调用
     */
    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(param);
        wait();
        return result;
    }

    /**
     * @param param
     * 第二个被调用
     */
    public void setParam(String param) {
        this.param = param;
    }
}
