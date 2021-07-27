package cn.com.kun.springframework.springredis.vo;

/**
 * 优先级队列demo对应的VO类
 *
 * author:xuyaokun_kzx
 * date:2021/7/27
 * desc:
*/
public class JobVO {

    private String name;

    private int priority;

    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
