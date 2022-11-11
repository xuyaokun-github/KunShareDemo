package cn.com.kun.foo.javacommon.socket.demo2;

import java.io.*;
import java.net.Socket;

public class ServerThread  extends Thread{
    private Socket socket = null;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            //获取客户端信息
            inputStream = socket.getInputStream();
            //回复客户端
            outputStream = socket.getOutputStream();

            while(true){
                if (socket.isClosed())
                    break;
                DataInputStream in = new DataInputStream(inputStream);
                //假如客户端一直没有命令发送过来，将会阻塞在这里（等待操作系统唤醒）
                String s = in.readUTF();
                System.out.println(s);//

                //向客户端返回命令处理结果
                //假如不返回，客户端的read方法将会阻塞
//                DataOutputStream out = new DataOutputStream(outputStream);
//                out.writeUTF(s + socket.getLocalSocketAddress());
//                Thread.sleep(1);
            }
        } catch(Exception e){
            e.printStackTrace();
        }

    }

}
