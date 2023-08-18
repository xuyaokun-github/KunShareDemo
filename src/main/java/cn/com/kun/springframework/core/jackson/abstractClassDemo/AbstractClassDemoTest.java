package cn.com.kun.springframework.core.jackson.abstractClassDemo;

import cn.com.kun.common.utils.JacksonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 抽象类demo
 *
 * author:xuyaokun_kzx
 * date:2023/8/16
 * desc:
*/
public class AbstractClassDemoTest {

    public static void main(String[] args) {

        Map<String, String> map = new HashMap<>();
        map.put("rtnCode", "000");
        map.put("msg", "xxx");
        String source = JacksonUtils.toJSONString(map);

        AbstractClassVO abstractClassVO = JacksonUtils.toJavaObject(source, AbstractClassVO.class);
        NotAbstractClassVO notAbstractClassVO = JacksonUtils.toJavaObject(source, NotAbstractClassVO.class);
        System.out.println(abstractClassVO);
        System.out.println(notAbstractClassVO);

    }


}
