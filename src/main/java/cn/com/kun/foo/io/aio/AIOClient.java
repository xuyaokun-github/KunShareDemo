package cn.com.kun.foo.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AIOClient {

    public static void main(String[] args) throws IOException {

        final AsynchronousSocketChannel client = AsynchronousSocketChannel.open();

        InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.1",8001);

        CompletionHandler<Void, ? super Object> handler = new CompletionHandler<Void,Object>(){

            @Override
            public void completed(Void result, Object attachment) {
                client.write(ByteBuffer.wrap("Hello".getBytes()),null,
                        new CompletionHandler<Integer,Object>(){

                            @Override
                            public void completed(Integer result,
                                                  Object attachment) {
                                final ByteBuffer buffer = ByteBuffer.allocate(1024);
                                client.read(buffer,buffer,new CompletionHandler<Integer,ByteBuffer>(){

                                    @Override
                                    public void completed(Integer result,
                                                          ByteBuffer attachment) {
                                        buffer.flip();
                                        System.out.println(new String(buffer.array()));
                                        try {
                                            client.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void failed(Throwable exc,
                                                       ByteBuffer attachment) {
                                    }

                                });
                            }

                            @Override
                            public void failed(Throwable exc, Object attachment) {
                            }

                        });
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
            }

        };

        //连接服务端
        client.connect(serverAddress, null, handler);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
