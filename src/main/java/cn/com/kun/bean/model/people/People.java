package cn.com.kun.bean.model.people;

import cn.com.kun.common.annotation.DesensitizationField;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 人
 *
 * author:xuyaokun_kzx
 * date:2021/5/25
 * desc:
*/
public class People {

    @JSONField(serialize=false)
    private String firstname;

    @JsonIgnore
    private String lastname;

    private String phone;

    private String email;

    private String company;

    /**
     * 一个人只能有一个家乡地址
     */
    @DesensitizationField
    private HomeTownAddress homeTownAddress;

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public HomeTownAddress getHomeTownAddress() {
        return homeTownAddress;
    }

    public void setHomeTownAddress(HomeTownAddress homeTownAddress) {
        this.homeTownAddress = homeTownAddress;
    }
}
