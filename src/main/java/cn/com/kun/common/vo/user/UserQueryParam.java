package cn.com.kun.common.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 查询参数
 * （分页参数不用体现到具体的业务参数里）
 */
public class UserQueryParam {

    private String firstname;
    private String lastname;
    private String phone;
    private String email;

    /**
     * 这个是json框架提供的
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * @DateTimeFormat这个是spring内置提供的格式化
     * @DateTimeFormat注意事项：
     *     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE)
     *     前端传参"createTime2": "2021-04-17"，是正常的。
     *     假如传了"createTime2": "2021-04-17 14:11:09"就会报错,可以通过自定义Format解决
     *     假如不用JsonFormat，默认在转json串时输出的是该date对应的long型时间戳
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE)
    private Date createTime2;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime2() {
        return createTime2;
    }

    public void setCreateTime2(Date createTime2) {
        this.createTime2 = createTime2;
    }
}
