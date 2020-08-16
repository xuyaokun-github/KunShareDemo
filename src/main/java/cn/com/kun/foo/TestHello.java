package cn.com.kun.foo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TestHello {

    private static final String aaa = "aaa";

   public static void main(String[] args) {

       try{
//           if (true)
//           throw new RuntimeException("");
           File file = new File("");
           if (file.exists()){
                new FileInputStream(file);
           }
           int aa = 1/0;
           System.out.println("end");
       } catch(IOException e){
            e.printStackTrace();
            int a = 1/0;
//           throw new RuntimeException("");
       } finally {
           System.out.println("finally");
       }

    }

}
