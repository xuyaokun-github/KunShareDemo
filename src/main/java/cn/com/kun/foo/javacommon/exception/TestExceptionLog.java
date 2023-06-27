package cn.com.kun.foo.javacommon.exception;

import java.io.IOException;

public class TestExceptionLog {

    public static void main(String[] args) {

        try {

        }catch (Exception e){

        }
    }

    private static void method1() throws IOException {

        if (true){
            String str = null;
            System.out.println(str.length());
        }

    }


}
