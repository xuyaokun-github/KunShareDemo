package cn.com.kun.foo.javacommon.exception;

public class TestException {

    public static void main(String[] args) {

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
}
