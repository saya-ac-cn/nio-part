package ac.cn.saya;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Title: NIOFileChannel02
 * @ProjectName netty-start
 * @Description: TODO
 * @Author Administrator
 * @Date: 2020/4/29 0029 15:25
 * @Description: 文件读取
 */

public class NIOFileChannel02 {

    private static final String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    /**
     * 操作顺序：目标文件 -》 channel -》 buffer -》 打印
     * @param args
     */
    public static void main(String[] args) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path + File.separator + "01.txt");
            FileChannel channel = inputStream.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer);

            System.out.println(new String(buffer.array()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
