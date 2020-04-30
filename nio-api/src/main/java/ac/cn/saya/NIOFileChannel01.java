package ac.cn.saya;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Title: NIOFileChannel01
 * @ProjectName netty-start
 * @Description: TODO
 * @Author Administrator
 * @Date: 2020/4/29 0029 15:01
 * @Description: 文件写入
 */

public class NIOFileChannel01 {

    private static final String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    /**
     * 操作顺序：你的程序（文本） -》 buffer -》 channel -》 目标文件
     * @param args
     */
    public static void main(String[] args) {
        // 要写入到的文本
        String str = "hello,saya";
        // 创建一个缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 将字符串放入到buffer
        buffer.put(str.getBytes());
        FileOutputStream outputStream = null;
        try {
            // 创建一个channel，fileOutPutStream包裹了channel，
            // 这里需要通过fileOutPutStream获取对应的FileChannel
            outputStream = new FileOutputStream(path+File.separator+"01.txt");
            FileChannel channel = outputStream.getChannel();

            buffer.flip();
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != outputStream){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
