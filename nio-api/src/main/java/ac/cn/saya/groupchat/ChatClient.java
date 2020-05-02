package ac.cn.saya.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @Title: ChatClient
 * @ProjectName nio-part
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2020-05-01 10:35
 * @Description:
 */

public class ChatClient {

    /**
     * 定义属性
     */
    private final String HOST = "localhost";

    private final int PORT = 9000;
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    public ChatClient() {
        try {
            selector = Selector.open();
            // 连接服务器
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST,PORT));
            // 设置非阻塞
            socketChannel.configureBlocking(false);
            // 得到用户名
            userName = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println("客户端准备就绪");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(String msg){
        msg = userName + "说" + msg;
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void takeData(){
        try {
            int readChannels = selector.select(2000);
            if (readChannels > 0){
                // 有可用的通道
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if (key.isReadable()){
                        // 得到相关通道
                        SocketChannel _channel = (SocketChannel) key.channel();
                        // 得到buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        // 读取
                        _channel.read(buffer);
                        System.out.println(new String(buffer.array()).trim());
                    }
                    // 移除当前key，防止重复操作
                    iterator.remove();
                }
            }else {
                ///System.out.println("等待接收数据");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        // 启动一个线程，每隔3秒，读取一次
        new Thread(()->{
            try {
                while (true){
                    client.takeData();
                    Thread.currentThread().sleep(3000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            client.sendData(line.trim());
        }
    }

}
