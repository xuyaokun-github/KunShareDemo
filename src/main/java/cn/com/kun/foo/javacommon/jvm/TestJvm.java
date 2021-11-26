package cn.com.kun.foo.javacommon.jvm;

public class TestJvm {

    /*
        -Xms100m -Xmx500m  -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./java_appname_pid.hprof
     */
    public static void main(String[] args) {

         //最大可用内存，对应-Xmx，单位是字节
        System.out.println(Runtime.getRuntime().maxMemory());
        long value = Runtime.getRuntime().maxMemory();//1883242496
        double d = 1024.0;
        System.out.println(value/d);//K
        System.out.println(value/d/d);//M
        System.out.println(value/d/d/d);//G 1.7G

        System.out.println("---------------------------");
        System.out.println(124008000/d);//K
        System.out.println(124008000/d/d);//M


    }

}
