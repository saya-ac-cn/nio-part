package ac.cn.saya;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * @Title: ByteBuffer1
 * @ProjectName nio-part
 * @Description: TODO
 * @Author liunengkai
 * @Date: 2020-05-01 11:43
 * @Description:
 */

public class ByteBuffer1 {

    public static void main(String[] args) {
        // 要写入到的文本
        String str = "hello,saya";
        // 创建一个缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 将字符串放入到buffer
        buffer.put(str.getBytes());
        buffer.put(str.getBytes());
        buffer.put(str.getBytes());
        buffer.put(str.getBytes());
        CharBuffer cbuf = buffer.asCharBuffer();
        System.out.println(cbuf.toString());
    }

}
