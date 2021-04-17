package cn.com.kun.common.entity;

/**
 * 模拟学生表为大表，上亿的表(进行按证件号分表)
 *
 * Created by xuyaokun On 2020/11/5 22:29
 * @desc:
 */
public class Student {

    private Long id;//主键，将用雪花算法生成

    private String idCard;//身份证号（本例为了简单，用求余数的方法分表）

    private String studentName;

    private String address;

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
}
