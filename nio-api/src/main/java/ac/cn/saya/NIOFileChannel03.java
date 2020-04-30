package ac.cn.saya;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Title: NIOFileChannel03
 * @ProjectName netty-start
 * @Description: TODO
 * @Author Administrator
 * @Date: 2020/4/29 0029 15:51
 * @Description:
 */

public class NIOFileChannel03 {

    private static final String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    /**
     * 操作顺序：目标文件 -》 读channel -》 buffer -》 写channel -》 持久化文件
     * @param args
     */
    public static void main(String[] args) {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            inputStream = new FileInputStream(path + File.separator + "01.txt");
            FileChannel readChannel = inputStream.getChannel();

            outputStream = new FileOutputStream(path+File.separator+"02.txt");
            FileChannel writeChannel = outputStream.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (true){
                // 清空
                buffer.clear();
                int read = readChannel.read(buffer);
                if (-1 == read){
                    return;
                }
                // 切换
                buffer.flip();
                writeChannel.write(buffer);
            }
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
