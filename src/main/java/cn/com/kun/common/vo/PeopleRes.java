package cn.com.kun.common.vo;

import cn.com.kun.common.annotation.DesensitizationField;
import cn.com.kun.common.annotation.SecretField;

import static cn.com.kun.common.constants.DesensitizationConstants.ID_EXPRESSION;
import static cn.com.kun.common.constants.DesensitizationConstants.ID_REPLACE;

/**
 * 按规范，给回前端的实体要和接收参数的实体区分开
 *
 * author:xuyaokun_kzx
 * date:2021/5/25
 * desc:
*/
public class PeopleRes {


    private String firstname;

    private String lastname;

    private String phone;

    private String email;

    private String company;

    /**
     * 加密的证件号
     * 一般给回前端的敏感信息要加密，这个密文后续在请求其他接口时会用到
     */
    @SecretField(encode = true)
    private String idCard;

    /**
     * 例如本属性，脱敏的证件号
     * 只给密文有时还不满足要求，因为有些字段要展示，所以密文对应的属性要有一个脱敏的字段，供展示
     * 脱敏需要用的是另一个注解
     */
    @DesensitizationField(expression = ID_EXPRESSION, replace = ID_REPLACE)
    private String idCardRisk;

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

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIdCardRisk() {
        return idCardRisk;
    }

    public void setIdCardRisk(String idCardRisk) {
        this.idCardRisk = idCardRisk;
    }
}
