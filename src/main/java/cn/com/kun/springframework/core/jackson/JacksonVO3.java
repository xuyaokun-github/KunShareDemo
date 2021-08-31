package cn.com.kun.springframework.core.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class JacksonVO3 {

    // 不加   timezone = "GMT+8"
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

