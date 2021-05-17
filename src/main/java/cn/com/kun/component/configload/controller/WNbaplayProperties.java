package cn.com.kun.component.configload.controller;


//@Component
//@ConfigurationProperties(prefix = "wnbaplay")
public class WNbaplayProperties {

    String number;
    String level;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


}
