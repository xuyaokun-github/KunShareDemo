package cn.com.kun.springframework.core.jackson.sensitivedemo;

public class UserSensitiveVO {
    /**     * 用户ID     */
    private Long userId;
    /**     * 用户姓名     */
    private String name;
    /**     * 手机号     */
    @SensitiveWrapped(SensitiveEnum.MOBILE_PHONE)
    private String mobile;
    /**     * 身份证号码     */
    @SensitiveWrapped(SensitiveEnum.ID_CARD)
    private String idCard;
    /**     * 年龄     */
    private String sex;
    /**     * 性别     */
    private int age;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
