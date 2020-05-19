package cn.com.kun.foo.lambda;

public class DunkPlayer {

    private String name;

    public DunkPlayer(String name) {
        this.name = name;
    }

    public void dunk(){
        System.out.println(this.name + "dunking.......");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
