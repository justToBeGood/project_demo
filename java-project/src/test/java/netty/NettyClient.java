package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @ClassName NettyClient
 * @Author wengweixin
 * @Date 2020/11/11 10:14
 **/
public class NettyClient {
    public static void main(String[] args) {
        EventLoopGroup group=new NioEventLoopGroup();
        try{
            Bootstrap b=new Bootstrap();
            b.group(group);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline=socketChannel.pipeline();
                    pipeline.addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                    pipeline.addLast("decoder",new StringDecoder());
                    pipeline.addLast("encoder",new StringEncoder());
                    pipeline.addLast("handler", new SimpleChannelInboundHandler<String>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
                            System.out.println(msg);
                        }
                    });
                }
            });
            Channel ch=b.connect("127.0.0.1",7777).sync().channel();
            BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
            String line=null;
            while(true){
                line=in.readLine();
                if("close".equals(line)){
                    break;
                }
                ch.writeAndFlush(line+"\n");
            }
            ch.close().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
}
