package cn.com.kun.springframework.core.jackson.lanyang;

import cn.com.kun.common.utils.JacksonUtils;

public class TestLanYangDemo {

    public static void main(String[] args) {

        LanYangVO lanYangVO = new LanYangVO();
        lanYangVO.setStatus("2");
        lanYangVO.setName("lanyang");
        System.out.println(JacksonUtils.toJSONString(lanYangVO));
    }
}
