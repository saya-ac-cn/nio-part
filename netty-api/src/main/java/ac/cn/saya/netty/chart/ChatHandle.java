package ac.cn.saya.netty.chart;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Title: ChatHandle
 * @ProjectName nio-part
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2020-05-02 16:51
 * @Description:
 */

public class ChatHandle extends SimpleChannelInboundHandler<String> {

    /**
     * 定义一个channel组，管理所有的额channel，不用自己写类似的List<Channel>
     * GlobalEventExecutor.INSTANCE是全局的事件执行器，是一个单例
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * handlerAdded 表示连接建立，一旦连接，第一个被执行
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //将该客户加入聊天的信息推送给其他的在线客户端
        //该方法将channelGroup中所有的channel进行遍历，发送消息
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("【客户端】"+channel.remoteAddress()+"加入回话\n");
        channelGroup.add(channel);
    }

    /**
     * 表示channel 处于活动状态，提示上线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"上线了");
    }

    /**
     * 表示channel 处于不活动状态，提示离线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"离线了");
    }


    /**
     * 断开连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("【客户端】"+channel.remoteAddress()+"退出回话\n");
    }


    /**
     * 读取数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(_channel -> {
            if (_channel != channel){
                _channel.writeAndFlush("【客户】"+channel.remoteAddress()+"发送了消息"+ msg +"\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
