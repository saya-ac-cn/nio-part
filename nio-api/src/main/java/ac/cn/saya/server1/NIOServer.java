package ac.cn.saya.server1;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Title: NIOServer
 * @ProjectName netty-start
 * @Description: TODO
 * @Author Administrator
 * @Date: 2020/4/30 0030 09:29
 * @Description:
 */

public class NIOServer {

    public static void main(String[] args) throws Exception {
        // 创建ServerSocketChannel -》 ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 得到selector
        Selector selector = Selector.open();

        // 绑定一个端口，用于服务器连接
        serverSocketChannel.socket().bind(new InetSocketAddress(9000));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 把serverSocketChannel注册到selector，关心事件为 OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端的连接
        while (true) {
            // 等待1s，如果没有事件发生，返回
            if (0 == selector.select(1000)) {
                System.out.println("轮询1s，无事件，返回");
                continue;
            }
            // 如果返回的>0，表示获取到相关关注的事件，就获取相关的selectionKey集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                // 获取到SelectionKey
                SelectionKey key = keyIterator.next();
                //根据key 对 对应通道发生的事件进行相应的处理
                if (key.isAcceptable()) {
                    System.out.println("客户端连接成功");
                    // 如果有新的客户端连接，则生成一个SocketChannel，并注册到selector
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 设置为非阻塞
                    socketChannel.configureBlocking(false);
                    // 注册到selector，并设置关心的事件OP_READ，并关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (key.isReadable()) {
                    // 发生OP_READ，通过key，反向获取对应channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 获取到该buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("来自客户端的消息："+ new String(buffer.array()));
                }
                // 手动从集合移除当前的selectKey，防止重复操作
                keyIterator.remove();
            }
        }
    }

}
