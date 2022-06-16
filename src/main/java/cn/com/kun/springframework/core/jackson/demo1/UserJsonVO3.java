package cn.com.kun.springframework.core.jackson.demo1;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserJsonVO3 {

    @JsonProperty("name-name1")
    private String name;

    private String name2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }
}
