package cn.com.kun.foo.javacommon.innerclass;

public class OuterDemoClass {

    private int[] data;

    public OuterDemoClass(int size) {
        this.data = new int[size];
    }

    static class Innner{
    }

    Innner createInner() {
        return new Innner();
    }
}


