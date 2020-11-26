package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @ClassName ChatClient
 * @Author wengweixin
 * @Date 2020/11/11 22:34
 **/
public class ChatClient {
    public static Selector selector=null;
    public static SocketChannel socketChannel=null;
    //register()设置通道感兴趣的事件
    //elect()是否返回与selectedKeys集合有关。当selectedKeys集合不为空时，select()会立即返回，但是其返回值是发生改变的keys数量，即新的就绪通道数量
    //当感兴趣的事件被触发之后,就会往selectedKeys里面添加selectedKey，在执行完将selectedKey移除之后，如果事件再次被触发再次就绪之后，相同的selectedKey对象实例会再次被添加集合里面
    public ChatClient(){
        try {
            this.selector=Selector.open();
            this.socketChannel=SocketChannel.open();
            this.socketChannel.configureBlocking(false);
            this.socketChannel.connect(new InetSocketAddress("127.0.0.1",9999));
            this.socketChannel.register(selector, SelectionKey.OP_CONNECT);
          new Thread(()->{
                while(true){
                    try{
                        int count =this.selector.select();
                        Iterator<SelectionKey> keys=this.selector.selectedKeys().iterator();
                        while (keys.hasNext()){
                            SelectionKey key=keys.next();
                            if(key.isValid() && key.isConnectable()){
                                SocketChannel socketChannel=(SocketChannel)key.channel();
                                if(socketChannel.finishConnect()){
                                    keys.remove();
                                    socketChannel.register(this.selector,SelectionKey.OP_READ);
                                }
                            }
                            if(key.isValid() && key.isReadable()){
                                this.readMsg(key);
                                keys.remove();
                            }
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }catch (ClosedChannelException e){
            System.out.println("Client： 失去主机连接");
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void readMsg(SelectionKey key){
        try {
            SocketChannel socketChannel=(SocketChannel)key.channel();
            ByteBuffer buffer=ByteBuffer.allocate(1024);
            StringBuilder stringBuilder=new StringBuilder();
            int size=socketChannel.read(buffer);
            while(size>0){
                buffer.flip();
                stringBuilder.append(new String(buffer.array(),0,size));
                size=socketChannel.read(buffer);
            }
            System.out.println("接收到的消息：\n"+stringBuilder.toString());
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        try {
            ChatClient chatClient=new ChatClient();
            System.out.println("请输入名称：");
            Scanner scanner=new Scanner(System.in);
            String name=scanner.nextLine();
            String onlineStr="open_"+name;
            sendMsg(onlineStr);
            System.out.println("开始聊天吧！");
            while (true){
                String msg=scanner.nextLine();
                if(msg.equals("close")){
                    String closeStr="exit_"+name;
                    sendMsg(closeStr);
                    break;
                }
                String sendMsg=name+"^"+msg;
                sendMsg(sendMsg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static  void sendMsg(String msg){
        try{
            byte[] req=msg.getBytes();
            ByteBuffer byteBuffer=ByteBuffer.wrap(req);
            byteBuffer.put(req);
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();

        }catch (Exception e){
            e.printStackTrace();
        }

    }





}


