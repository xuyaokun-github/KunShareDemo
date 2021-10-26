package cn.com.kun.foo.javacommon.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AIOEchoServer {

    private final static int PORT = 8001;
    private final static String IP = "127.0.0.1";

    //
    private AsynchronousServerSocketChannel server = null;

    public AIOEchoServer(){
        try {
            //同样是利用工厂方法产生一个通道，异步通道 AsynchronousServerSocketChannel
            server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(IP,PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //使用这个通道(server)来进行客户端的接收和处理
    public void start(){
        System.out.println("Server listen on " + PORT);

        //注册事件和事件完成后的处理器，这个CompletionHandler就是事件完成后的处理器
        server.accept(null,new CompletionHandler<AsynchronousSocketChannel,Object>(){

            final ByteBuffer buffer = ByteBuffer.allocate(1024);

            @Override
            public void completed(AsynchronousSocketChannel result, Object attachment) {

                //只要接收到一个客户端连接，就会触发这个方法

                System.out.println(Thread.currentThread().getName());
                Future<Integer> writeResult = null;

                try{
                    buffer.clear();
                    //写数据到缓冲区
                    result.read(buffer).get(100,TimeUnit.SECONDS);
                    //
                    System.out.println("In server: "+ new String(buffer.array()));

                    //将数据写回客户端
                    buffer.flip();
                    writeResult = result.write(buffer);
                }catch(InterruptedException | ExecutionException | TimeoutException e){
                    e.printStackTrace();
                }finally{
                    server.accept(null,this);
                    try {
                        writeResult.get();
                        //关闭通道
                        result.close();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println("failed:"+exc);
            }

        });
    }

    public static void main(String[] args) {
        new AIOEchoServer().start();
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
