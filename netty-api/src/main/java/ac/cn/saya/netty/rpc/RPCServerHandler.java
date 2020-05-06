package ac.cn.saya.netty.rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Title: RPCServerHandler
 * @ProjectName nio-part
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2020-05-03 21:43
 * @Description:
 */

public class RPCServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server channelActive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            System.out.println("msg="+msg);
            if (!"".equals(msg.toString())){
                String result = new UserServiceImpl().get(msg.toString());
                ctx.channel().writeAndFlush(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server channelReadComplete");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println(cause.getMessage());
    }
}
