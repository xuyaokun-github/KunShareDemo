package cn.com.kun.foo.javacommon.io.readFile;

import org.springframework.util.StopWatch;

import java.io.*;
import java.util.Scanner;
import java.util.function.Consumer;

public class ReadFileExample {

    static File file = new File("D:/home/readFile/ReadFileExample.txt");

    public static void main(String[] args) {

        //第一种方法
        runRead(a -> {method1();}, "Scanner");
        runRead(a -> {method2();}, "BufferedReader");

    }

    private static void runRead(Consumer consumer, String methodName) {
        StopWatch watch = new StopWatch();
        watch.start();
        consumer.accept(null);
        watch.stop();
        System.out.println(methodName + "耗时（ms）:" + watch.getTotalTimeMillis());
    }

    private static void method2() {

        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(bis, "utf-8"), 10 * 1024 * 1024);//10M缓存
        ) {
            String line = "";
            while((line = reader.readLine()) != null){
                String[] strs = line.split("\\|");
//                System.out.println(MD5EncryptUtil.md5Encode(strs[0]));
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void method1() {

        try (Scanner scanner = new Scanner(new FileInputStream(file))){
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] strs = line.split("\\|");
//                System.out.println(MD5EncryptUtil.md5Encode(strs[0]));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
