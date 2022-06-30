package cn.com.kun.apache.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class ScannerTest {

    public static void main(String[] args) throws IOException {

        String bigFilePath = "D:\\home\\kunghsu\\big-file-test\\big-file.txt";
        FileInputStream inputStream = null;

        Scanner sc = null;

        try {
            inputStream = new FileInputStream(bigFilePath);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                 System.out.println(line);
            }

        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }

        }
    }
}
