package cn.com.kun.foo.javacommon.datatype;

public class TestMath {

    public static void main(String[] args) {

//        System.out.println(0x1.0p-53);

        for (int i = 0; i < 100 * 10000; i++) {
            String str = "" + Math.random();
            if (str.startsWith("0.000")){
                System.out.println(str);
            }
//            if (str.startsWith("0.00")){
//                System.out.println(str);
//            }
        }
    }

}
