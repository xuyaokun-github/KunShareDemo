package cn.com.kun.foo.javacommon.io.bio;


import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class BIOClient {

    public static void main(String[] args) {
        // TODO 创建多个线程，模拟多个客户端连接服务端
        new Thread(() -> {
            try {
                //创建一个Socket绑定3333端口，表示去访问3333端口
                Socket socket = new Socket("127.0.0.1", 3333);
                while (true) {
                    try {
                        //发送数据
                        System.out.println("开始发送数据");
                        socket.getOutputStream().write((new Date() + ": hello world").getBytes());
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

}
