package cn.com.kun.foo.javacommon.io.scanner;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 写文件的六种方法比较
 *
 * author:xuyaokun_kzx
 * date:2022/11/1
 * desc:
*/
public class WriteFileExample2 {

    public static void main(String[] args) throws IOException {
        // 构建写入内容
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 3000000; i++) {
            stringBuilder.append("ABCDEFG|1234567890|kunghsu|kkkkkkkkkkkkkkkkkkkkkkkkkkk|8888888888888888888888888888888|9999999999999999999999999999999999999999");
            stringBuilder.append("\n");
        }
        // 写入内容
        final String content = stringBuilder.toString();
        // 存放文件的目录
//        final String filepath6 = "D:/home/write6.txt";
        //
        final String filepath6 = "D:/home/ReadFileExample.txt";

        // 方法六:使用 Files 写文件
        long stime6 = System.currentTimeMillis();
        filesTest(filepath6, content);
        long etime6 = System.currentTimeMillis();
        System.out.println("Files 写入用时:" + (etime6 - stime6));

    }

    /**
     * 方法六:使用 Files 写文件
     * @param filepath 文件目录
     * @param content  待写入内容
     * @throws IOException
     */
    private static void filesTest(String filepath, String content) throws IOException {
        Files.write(Paths.get(filepath), content.getBytes());
    }

    /**
     * 方法五:使用 BufferedOutputStream 写文件
     * @param filepath 文件目录
     * @param content  待写入内容
     * @throws IOException
     */
    private static void bufferedOutputStreamTest(String filepath, String content) throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                new FileOutputStream(filepath))) {
            bufferedOutputStream.write(content.getBytes());
        }
    }

    /**
     * 方法四:使用 FileOutputStream  写文件
     * @param filepath 文件目录
     * @param content  待写入内容
     * @throws IOException
     */
    private static void fileOutputStreamTest(String filepath, String content) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filepath)) {
            byte[] bytes = content.getBytes();
            fileOutputStream.write(bytes);
        }
    }

    /**
     * 方法三:使用 PrintWriter 写文件
     * @param filepath 文件目录
     * @param content  待写入内容
     * @throws IOException
     */
    private static void printWriterTest(String filepath, String content) throws IOException {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(filepath))) {
            printWriter.print(content);
        }
    }

    /**
     * 方法二:使用 BufferedWriter 写文件
     * @param filepath 文件目录
     * @param content  待写入内容
     * @throws IOException
     */
    private static void bufferedWriterTest(String filepath, String content) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath))) {
            bufferedWriter.write(content);
        }
    }

    /**
     * 方法一:使用 FileWriter 写文件
     * @param filepath 文件目录
     * @param content  待写入内容
     * @throws IOException
     */
    private static void fileWriterTest(String filepath, String content) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filepath)) {
            fileWriter.append(content);
        }
    }
}
