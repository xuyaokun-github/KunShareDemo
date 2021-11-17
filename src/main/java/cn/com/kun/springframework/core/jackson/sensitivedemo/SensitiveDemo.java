package cn.com.kun.springframework.core.jackson.sensitivedemo;

import cn.com.kun.common.utils.JacksonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

public class SensitiveDemo {

    public static void main(String[] args) throws JsonProcessingException {
        UserSensitiveVO userEntity = new UserSensitiveVO();
        userEntity.setUserId(1l);
        userEntity.setName("张三");
        userEntity.setMobile("18000000001");
        userEntity.setIdCard("420117200001011000008888");
        userEntity.setAge(20);
        userEntity.setSex("男");
        //通过jackson方式，将对象序列化成json字符串
        System.out.println(JacksonUtils.toJSONString(userEntity));
    }

}
