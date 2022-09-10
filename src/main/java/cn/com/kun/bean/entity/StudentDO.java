package cn.com.kun.bean.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 模拟学生表为大表，上亿的表(进行按证件号分表)
 *
 * Created by xuyaokun On 2020/11/5 22:29
 * @desc:
 */
public class StudentDO {

    private Long id;//主键，将用雪花算法生成

    private String idCard;//身份证号（本例为了简单，用求余数的方法分表）

    private String studentName;

    private String address;

    private String otherAddress;

//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
//    private Date createTime;

    /**
     * 假如用传统的JDBC,正确的应该用java.sql.Date，不能用java.util.Date
     * (假如非要用java.util.Date，JDBC底层要做一个特殊处理)
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.sql.Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }


    public java.sql.Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.sql.Date createTime) {
        this.createTime = createTime;
    }

    public String getOtherAddress() {
        return otherAddress;
    }

    public void setOtherAddress(String otherAddress) {
        this.otherAddress = otherAddress;
    }
}
