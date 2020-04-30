package ac.cn.saya;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @Title: ScatteringAndGathering1
 * @ProjectName nio-part
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2020-04-29 21:15
 * @Description:
 * Scattering:将数据写入到buffer时，可以采用buffer数组，分散写入
 * Gathering：从buffer读取数据时，聚合一次读
 */

public class ScatteringAndGathering1 {

    public static void main(String[] args) throws Exception{

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(9000);
        serverSocketChannel.socket().bind(inetSocketAddress);
        // 创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        // 假定葱客户端传来的字符串的长度是8
        int messageLenth = 8;
        while (true){
            // 累计读取的字符数
            int byteRead = 0;
            while (byteRead < messageLenth){
                long length = socketChannel.read(byteBuffers);
                byteRead += length;
                System.out.println("buyeRead="+byteRead);
                // 打印
                Arrays.asList(byteBuffers).stream().map(buffer ->"postion="+buffer.position()+",limit="+buffer.limit()).forEach(System.out::println);
            }
            // buffer 切换
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());
            Arrays.asList(byteBuffers).forEach(buffer -> System.out.println("msg:"+new String(buffer.array())));
//            // 累计读取的字符数
//            int byteWrite = 0;
//            // 读出并打印
//            while (byteWrite < messageLenth){
//                long length = socketChannel.write(byteBuffers);
//                byteWrite += length;
//            }
            // buffer 切换
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());
            //System.out.println("，byteWrite="+byteWrite+"，messageLenth="+messageLenth);
        }
    }

}
