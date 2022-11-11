package cn.com.kun.foo.javacommon.socket;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * author:xuyaokun_kzx
 * date:2022/11/10
 * desc:
 */
public class ServerSocketDemo {

    public static void main(String[] args) {
        try {
            //1、创建服务器端ServerSocket对象，绑定监听的端口号6666
            ServerSocket serverSocket = new ServerSocket(6666);
            //2、调用accept()方法监听开始，等待客户端连接
            System.out.println("服务器端等待客户端连接。。。");
            Socket socket = serverSocket.accept();

            //3、通过输入流读取客户端传来的信息
            InputStream inputStream = socket.getInputStream();
            //获取字节输入流
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            //包装为缓冲字符流
            String info;
            while ((info = bufferedReader.readLine()) != null)
                System.out.println("收到客户端信息：" + info);
            // 获取客户端的InetAddress信息
            InetAddress address = socket.getInetAddress();
            System.out.println("当前客户端的IP：" + address.getHostAddress());
            //关闭socket输入流
            socket.shutdownInput();

            //4、通过输出流向客户端返回相应信息
            //获取输出流
            OutputStream outputStream = socket.getOutputStream();
            //将输出流包装为打印流
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.write("欢迎登录！");
            printWriter.flush();
            //关闭socket输出流
            socket.shutdownOutput();

            //5、关闭资源流
            printWriter.close();
            outputStream.close();
            bufferedReader.close();
            inputStream.close();
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
