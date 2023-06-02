package cn.com.kun.foo.javacommon.exception;

import java.io.IOException;

public class TestException {

    public static void main(String[] args) {

        try {
            method1();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (true){
            return;
        }

        new Thread(()->{
            while (true){
                try {
                    if (true){
                        throw new RuntimeException("error");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }).start();
    }

    private static void method1() throws IOException {

        if (true){
            String str = null;
            System.out.println(str.length());
        }

    }


}
