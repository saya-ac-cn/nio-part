package ac.cn.saya;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @Title: NIOFileChannel04
 * @ProjectName netty-start
 * @Description: TODO
 * @Author Administrator
 * @Date: 2020/4/29 0029 16:15
 * @Description: 文件零拷贝
 */

public class NIOFileChannel04 {

    private static final String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    public static void main(String[] args) {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            inputStream = new FileInputStream(path + File.separator + "01.txt");
            FileChannel sourceChannel = inputStream.getChannel();

            outputStream = new FileOutputStream(path+File.separator+"02.txt");
            FileChannel destChannel = outputStream.getChannel();

            destChannel.transferFrom(sourceChannel,0,sourceChannel.size());
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
