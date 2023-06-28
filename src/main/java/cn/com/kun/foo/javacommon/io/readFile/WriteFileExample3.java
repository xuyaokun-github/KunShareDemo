package cn.com.kun.foo.javacommon.io.readFile;

import org.apache.commons.io.FileUtils;

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
public class WriteFileExample3 {

    public static void main(String[] args) throws IOException {

        /*
            比较快的生成模拟文件的方法是 先升成一个稍微小的文件，例如100M一个，再复制多份，再将多个文件合并为一个文件。
         */
        final String filepath6 = "D:/home/readFile/ReadFileExample.txt";
        File file = new File(filepath6);
        long stime6 = System.currentTimeMillis();
        // 构建写入内容
        String str = "ABCDEFG|1234567890|kunghsu|kkkkkkkkkkkkkkkkkkkkkkkkkkk|8888888888888888888888888888888|9999999999999999999999999999999999999999";
        str = str + "|" + str + "|" + str;
        str = str + "\n";

        for (int i = 0; i < 10; i++) {
            FileUtils.writeStringToFile(file, str, "UTF-8", true);
        }

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
