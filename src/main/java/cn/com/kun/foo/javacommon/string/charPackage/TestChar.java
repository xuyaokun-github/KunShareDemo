package cn.com.kun.foo.javacommon.string.charPackage;

public class TestChar {

    public static void main(String[] args) {

        //
        char c='1';
        System.out.println(c);
        System.out.println(c-'0');
        System.out.println("char类型会自动转换为int,c+0="+(c+0));

        int a=49;
        System.out.println("因为char类型会自动转换为int,所以a+'0'="+(a+'0'));
        System.out.println("对其进行强制转换，使其成为char类型， 所以(char)(a+'0')="+(char)(a+'0'));


        //code 0 这个字符，输出到控制台就是一个空
        int source = 0;
        System.out.println("xyk" + (char)(source) + "kunghsu");

    }
}
