package cn.com.kun.springframework.core.jackson.toStringDemo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonIncludeDemoVo {

    private String name;

    private String address;

    private List<JsonIncludeDemoInner> innerList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<JsonIncludeDemoInner> getInnerList() {
        return innerList;
    }

    public void setInnerList(List<JsonIncludeDemoInner> innerList) {
        this.innerList = innerList;
    }
}
