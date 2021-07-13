package cn.com.kun.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressUtils {

    public static String getHostName(){
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
//        String ip=addr.getHostAddress().toString(); //获取本机ip
        String hostName = addr.getHostName().toString(); //获取本机计算机名称
        return hostName;
    }


}
