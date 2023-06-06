package cn.com.kun.bean.entity;

import cn.com.kun.service.mybatis.sensitiveDemo.EncryptDecryptField;
import cn.com.kun.service.mybatis.sensitiveDemo.SensitiveData;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 加解密demo 实体类
 * 为了方便，和User用同一张表
 *
 * author:xuyaokun_kzx
 * date:2023/6/6
 * desc:
*/
@SensitiveData
public class UserSensitiveDO implements Serializable {

    private Long id;

    private String firstname;

    private String lastname;

    private String phone;

    @EncryptDecryptField
    private String email;

    private String username;

    private String password;

    private long age;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
