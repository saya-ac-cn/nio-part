package ac.cn.saya.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @Title: ChatServer
 * @ProjectName nio-part
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2020-04-30 21:06
 * @Description:
 */

public class ChatServer {

    // 定义相关属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 9000;

    /**
     * 服务器初始化
     */
    public ChatServer() {
        try {
            // 得到选择器
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            // 绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            // 设置非阻塞
            listenChannel.configureBlocking(false);
            // 注册到选择器
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听
     */
    public void listen(){
        try {
            while (true){
                int count = selector.select(2000);
                if (count > 0){
                    // 有事件处理
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()){
                            SocketChannel _channel = listenChannel.accept();
                            _channel.configureBlocking(false);
                            // 注册
                            _channel.register(selector,SelectionKey.OP_READ);
                            // 提示
                            System.out.println(_channel.getRemoteAddress() + "上线");
                        }
                        if (key.isReadable()){

                        }

                        // 当前的key删除，防止重复处理
                        iterator.remove();
                    }
                }else {
                    System.out.println("等待中");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读客户端消息
     * @param key
     */
    public void  readData(SelectionKey key){
        SocketChannel channel = null;
        try {
            // 得到channel
            channel = (SocketChannel)key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            // 读到了数据
            if (count > 0){
                // 得到缓冲区数据
                String msg = new String(buffer.array());
                // 输出消息
                System.out.println("from 客户端" + msg);
                // 向其他客户端发送消息
                sendMsgToOtherClient(msg,channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress()+"下线");
                key.cancel();
                channel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void sendMsgToOtherClient(String msg,SocketChannel self){
        System.out.println("服务器转发消息");
        try {
            //遍历所有注册到selector的socketchannel，并排除自己
            for (SelectionKey key:selector.keys()) {
                SelectableChannel targetChannel = key.channel();
                // 把自己排除掉
                if (targetChannel instanceof SocketChannel && targetChannel != self){
                    SocketChannel dest = (SocketChannel) targetChannel;
                    ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                    dest.write(buffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
