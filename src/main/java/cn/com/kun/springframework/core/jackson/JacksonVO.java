package cn.com.kun.springframework.core.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY, getterVisibility= JsonAutoDetect.Visibility.NONE)
public class JacksonVO {

    private String AAA;

    public String getAAA() {
        return AAA;
    }

    public void setAAA(String AAA) {
        this.AAA = AAA;
    }
}
