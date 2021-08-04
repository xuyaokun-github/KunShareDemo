package cn.com.kun.foo.javacommon.hashcode;

public class IdentityHashCodeVO {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return 1000;
    }
}
