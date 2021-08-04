package cn.com.kun.foo.javacommon.hashcode;

import cn.com.kun.bean.entity.User;

public class IdentityHashCodeTest {

    public static void main(String[] args) {

        new User();

        //虽然复写了hashcode方法，但是System.identityHashCode输出的还是不同的值
        IdentityHashCodeVO vo1 = new IdentityHashCodeVO();
        IdentityHashCodeVO vo2 = new IdentityHashCodeVO();
        System.out.println(System.identityHashCode(vo1));
        System.out.println(System.identityHashCode(vo2));
        System.out.println(System.identityHashCode(vo2));
        //并且每次重启，输出的值都一样，说明算法是固定的

    }
}
