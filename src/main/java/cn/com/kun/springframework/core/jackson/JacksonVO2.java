package cn.com.kun.springframework.core.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class JacksonVO2 {

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

