package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
//与Selector一起使用时，Channel必须处于非阻塞模式下。这意味着不能将FileChannel与Selector一起使用，因为FileChannel不能切换到非阻塞模式。而套接字通道都可以。
/*多人聊天室：服务端执行机制（设置通道对某个事件感兴趣，是设置某个事件已就绪，具体得看事件觉得，比如读事件就得通道有数据才能算就绪）
1、创建选择器、服务端通道、服务端socket、绑定端口
2、将服务端通道注册到选择器中，并设置服务端通道对接收连接感兴趣
3、selector.select()会阻塞，知道至少一个通道就绪，然后获取所有就绪的通道
4、接下来就是三个执行动作了，接收连接，读取消息，发送消息，虽然第一次循环肯定是接收连接
5、接收连接：selector在接收到通道连接的事件之后就会为该连接创建新的socketChannel用于和客户端的通信，将该通道注册到选择器，并且设置其感兴趣的事件是读
6、读取消息：当通道有数据发送过来的时候，读取客户端消息，通过attach()给其余各个channel设置附加对象数据，之后设置其感兴趣的事件是写
7、发送消息：由于设置通道对写事件感兴趣时就可对判断通道对该事件是否就绪，获取attach里面的对象，给客户端发送数据
如果服务端的channel在发送消息之前收到2个通知，前面的通知会被覆盖，造成消息丢失
*/
/**
 * @ClassName ChatServer
 * @Author wengweixin
 * @Date 2020/11/11 14:10
 **/

public class ChatServer {

    public static void main(String[] args) {
        ChatServerThread chatServerThread=new ChatServerThread();
        new Thread(chatServerThread).start();
    }
}

class ChatServerThread implements Runnable{
    private Selector selector;
    private SelectionKey serverKey;
    private Vector<String> usernames;
    private static final int PORT=9999;

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public  ChatServerThread(){
        usernames=new Vector<>();
        init();
    }

    public void init(){
        try {
            //获取选择器
            selector=Selector.open();
            //获取服务端通道
            ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
            //获取服务端socket
            ServerSocket socket=serverSocketChannel.socket();
            //socket绑定端口
            socket.bind(new InetSocketAddress(PORT));
            //跟selector配合时channel要设置成非阻塞状态
            serverSocketChannel.configureBlocking(false);
            //将通道注册到selector，并且监听该通道对连接时间感兴趣
            serverKey=serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
            printInfo("server starting.......");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try {
            while(true){
                //select()阻塞到至少有一个通道注册的事件上就绪了。
                int count=selector.select();
                if(count>0){
                    //获取已选择的键集合来访问就绪的通道
                    Iterator<SelectionKey> iterator=selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey key=iterator.next();
                        //如果该事件的通道等待接收新的套接字连接
                        if(key.isAcceptable()){
                            System.out.println(key.toString()+":接收");
                            //Selector不会自己从已选择键集中移除SelectionKey实例。必须在处理完通道时自己移除。不然下次该通道变成就绪时，Selector会再次将其放入已选择键集中。
                            iterator.remove();
                            //获取该事件触发的通道
                            ServerSocketChannel serverSocketChannel=(ServerSocketChannel)key.channel();
                            //监听新进来的连接，注册通道并且监听对读事件感兴趣
                            SocketChannel socketChannel=serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector,SelectionKey.OP_READ);
                        }
                        //如果该事件的通道有数据可读状态
                        if(key.isValid()&& key.isReadable()){
                            readMSg(key);
                        }
                        //如果该事件的通道是写数据状态
                        if(key.isValid() && key.isWritable()){
                            writeMsg(key);
                        }
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //读取通道数据
    private void readMSg(SelectionKey key){
        SocketChannel channel=null;
        try {
            //获取该事件的通道
            channel=(SocketChannel) key.channel();
            //设置缓存区
            ByteBuffer buffer= ByteBuffer.allocate(1024);
            //将通道数据写到缓存区
            int count=channel.read(buffer);
            StringBuffer buf=new StringBuffer();
            if(count>0){
                //切换读状态
                buffer.flip();
                buf.append(new String(buffer.array(),0,count));
            }
            String msg=buf.toString();
            //如果次数据是客户端连接时发送的数据
            if(msg.indexOf("open_")!=-1){
                String name=msg.substring(5);
                printInfo(name+"--->online");
                //将用户添加到上线用户列表中
                usernames.add(name);
                //获取已就绪的通道集合
                Iterator<SelectionKey> iter=selector.selectedKeys().iterator();
                while ((iter.hasNext())){
                    SelectionKey skey=iter.next();
                    //该事件不是道路端口通道的事件
                    if(skey!=serverKey){
                        //可以将一个对象或者更多信息附着到SelectionKey上，这样就能方便的识别某个给定的通道
                        skey.attach(usernames);
                        //更新通道感兴趣的事件
                        skey.interestOps(skey.interestOps() | SelectionKey.OP_WRITE);
                    }
                }
            }else if(msg.indexOf("exit_")!=-1){
                String username=msg.substring(5);
                usernames.remove(username);
                key.attach("close");
                //要退出当前channel加上close的标示，并把兴趣转为写，如果write中收到close，则中断通道
                key.interestOps(SelectionKey.OP_WRITE);
                Iterator<SelectionKey> iter=selector.selectedKeys().iterator();
                printInfo(username+"下线啦！");
                while(iter.hasNext()){
                    SelectionKey skey=iter.next();
                    //相当于服务器通道内部之间数据的传输，并通知该通道写就绪了
                    skey.attach(username+"下线啦！");
                    skey.interestOps(skey.interestOps()|SelectionKey.OP_WRITE);
                }
            }else{
                String uname=msg.substring(0,msg.indexOf("^"));
                msg=msg.substring(msg.indexOf("^")+1);
                printInfo("("+uname+")说："+msg);
                String dateTime=sdf.format(new Date());
                String smsg=uname+" "+dateTime+"\n"+msg+"\n";
                Iterator<SelectionKey> iter=selector.selectedKeys().iterator();
                while (iter.hasNext()){
                    SelectionKey skey=iter.next();
                    skey.attach(smsg);
                    skey.interestOps(skey.interestOps()|SelectionKey.OP_WRITE);
                }
            }
            buffer.clear();
        }catch (IOException e){
            key.cancel();
            try{
                channel.socket().close();
                channel.close();
            }catch (IOException e1){
                e1.printStackTrace();
            }
        }
    }

    //先通道写信息
    private void writeMsg(SelectionKey key){
        try {
            SocketChannel socketChannel=(SocketChannel)key.channel();
            Object attachment=key.attachment();
            key.attach("");
            socketChannel.write(ByteBuffer.wrap(attachment.toString().getBytes()));
            key.interestOps(SelectionKey.OP_READ);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void printInfo(String str) {
        System.out.println("[" + sdf.format(new Date()) + "] -> " + str);
    }
}
