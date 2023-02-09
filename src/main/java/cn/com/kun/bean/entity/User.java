package cn.com.kun.bean.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class User /*implements Serializable*/ {

    private Long id;

    private String firstname;

    private String lastname;

    private String phone;

    private String email;

    private String username;

    private String password;

//    private Long age;
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

//    public Long getAge() {
//        return age;
//    }
//
//    public void setAge(Long age) {
//        this.age = age;
//    }


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
