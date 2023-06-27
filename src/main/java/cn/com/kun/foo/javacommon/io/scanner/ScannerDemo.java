package cn.com.kun.foo.javacommon.io.scanner;

import cn.com.kun.foo.javacommon.string.md5.MD5EncryptUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

public class ScannerDemo {

    public static void main(String[] args) {

        File file = new File("D:/home/write6.txt");
        try (Scanner scanner = new Scanner(new FileInputStream(file))){

            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] strs = line.split("\\|");
                System.out.println(MD5EncryptUtil.md5Encode(strs[0]));

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
