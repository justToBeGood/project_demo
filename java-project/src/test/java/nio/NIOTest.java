package nio;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName NIOTest
 * @Author wengweixin
 * @Date 2020/11/11 11:53
 **/
public class NIOTest {
    @Test
    public void channeAndBufferlTest(){
        try {
            RandomAccessFile accessFile=new RandomAccessFile("fromFile.txt","rw");
            FileChannel inChannel=accessFile.getChannel();
            ByteBuffer buffer=ByteBuffer.allocate(48);
            byte[] byteArr=new byte[(int)inChannel.size()];
            int bytesRead=inChannel.read(buffer);
            int i=0;
            while(bytesRead!=-1){
                buffer.flip();
                while (buffer.hasRemaining()){
                    byteArr[i]=buffer.get();
                    i++;
                }
                buffer.clear();
                bytesRead=inChannel.read(buffer);
            }
            System.out.println(new String(byteArr,"utf-8"));
            accessFile.close();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Test
    public void channelTransfer(){
        try{
            //覆盖写
            RandomAccessFile fromFile=new RandomAccessFile("fromFile.txt","rw");
            FileChannel fromChannel=fromFile.getChannel();

            RandomAccessFile toFile=new RandomAccessFile("toFile.txt","rw");
            FileChannel toChannel=toFile.getChannel();

            long position=0;
            long count = fromChannel.size();
            toChannel.transferFrom(fromChannel,position,count);

            fromChannel.transferTo(position,count,toChannel);

            fromFile.close();
            toFile.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
