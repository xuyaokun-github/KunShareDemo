package cn.com.kun.common.utils;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

public class AESUtils {

    /**
     * AES加密
     * @param sourceStr
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String sourceStr, String key) throws Exception{

        byte[] sourceByte = sourceStr.getBytes("UTF-8");
        try {
            byte[] encodeKey = getAESKeyByte(key);//得到密钥的字节数组
            SecretKeySpec secretKeySpec = new SecretKeySpec(encodeKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");//创建密码器
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);//初始化
            //得到加密的字节数组
            byte[] resultByte = cipher.doFinal(sourceByte);
            String resultStr = "";
            //将字节数组转为十六进制字符串
            //方法1:用Java原生自带的实现
//			resultStr = bytesToHexString(resultByte);
            //方法2:用apache的jar包
            byte[] hexByte = encode2Hex(resultByte);
            resultStr = new String(hexByte, "UTF-8");
            return resultStr;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * AES解密
     * @param secretStr
     * @param key
     * @return
     * @throws Exception
     * @throws UnsupportedEncodingException
     */
    public static String decrypt(String secretStr, String key) throws UnsupportedEncodingException, Exception{

        byte[] secretByte;
        //方法1：用原生自带实现十六进制解码
//		secretByte = hexStringToBytes(secretStr);
        //方法2：用apache的jar包
        secretByte = decodeHex(secretStr.getBytes("UTF-8"));
        //AES运算，解密
        try {
            byte[] encodeKey = getAESKeyByte(key);
            SecretKeySpec secretKeySpec = new SecretKeySpec(encodeKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器\
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);// 初始化
            byte[] resultByte = cipher.doFinal(secretByte);
            String resultStr = new String(resultByte, "UTF-8");
            return resultStr;
        }  catch (Exception e) {
            return null;
        }
    }


    /**
     * 十六进制转码
     * @param inData
     * @return
     * @throws Exception
     */
    public static byte[] encode2Hex(byte[] inData) throws Exception{
        byte[] outData;
        /**
         * 限制编码解码传入的字节大小，不能超过100M，即100*1024*1024 字节
         */
        if(inData.length > 100*1024*1024){
            throw new Exception();
        }
        outData = new Hex().encode(inData);
        return outData;
    }

    /**
     * 十六进制解码
     * @param inData
     * @return
     * @throws Exception
     */
    public static byte[] decodeHex(byte[] inData) throws Exception{

        byte[] outData;
        if(inData.length > 100*1024*1024){
            throw new Exception();
        }
        outData = new Hex().decode(inData);
        return outData;
    }

    /**
     * 将密钥转为byte[](无论加解密，都要经过这一步)
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] getAESKeyByte(String key) throws Exception{

        byte[] aesKey;
        if(key == null || "".equals(key)){
            key = "KungKungKungKung";
        }
        aesKey = key.getBytes("UTF-8");

        byte[] enCodeFormat = null;
        if (aesKey.length == 16) {
            enCodeFormat = aesKey;
        } else {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(aesKey));
            SecretKey secretKey = kgen.generateKey();
            enCodeFormat = secretKey.getEncoded();
        }
        return enCodeFormat;
    }

    /**
     * 字节数组转十六进制字符串（不依赖任何非java的jar包）
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 十六进制字符串转字节数组（不依赖任何非java的jar包）
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     *
     * @param c
     * @return
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
