package ac.cn.saya;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Title: MappedByteBuffer01
 * @ProjectName netty-start
 * @Description: TODO
 * @Author Administrator
 * @Date: 2020/4/29 0029 17:21
 * @Description: MappedByteBuffer 可以在内存中直接修改文件，避免拷贝
 */

public class MappedByteBuffer01 {

    private static final String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    public static void main(String[] args) {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(path + File.separator + "02.txt", "rw");
            FileChannel channel = randomAccessFile.getChannel();
            /**
             * 参数1：FileChannel.MapMode.READ_WRITE 使用的读写模式
             * 参数2：可以直接修改的其实位置
             * 参数3：映射到内存的大小，即 有多少字节映射到内存中
             */
            MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
            mappedByteBuffer.put(0,(byte)'0');
            mappedByteBuffer.put(3,(byte)'3');
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != randomAccessFile){
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
