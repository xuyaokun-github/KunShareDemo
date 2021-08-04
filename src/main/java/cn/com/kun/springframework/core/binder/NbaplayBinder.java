package cn.com.kun.springframework.core.binder;

import org.springframework.stereotype.Component;

@Component
public class NbaplayBinder {

    String number = "1000";

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
