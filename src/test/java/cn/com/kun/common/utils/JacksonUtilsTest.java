package cn.com.kun.common.utils;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.vo.ResultVo;
import com.fasterxml.jackson.core.type.TypeReference;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JacksonUtilsTest extends TestCase {

    /**
     * 错误示例1
     */
    @Test
    public void testJson() {

        User user = new User();
        user.setUsername("xyk");
        List<User> userList = new ArrayList<>();
        userList.add(user);
        ResultVo<List<User>> resultVo = ResultVo.valueOfSuccess(userList);

        String sourceStr = JacksonUtils.toJSONString(resultVo);
        //根据字符串还原回原来的对象，这时候会发生泛型丢失
        //User类型对象变成LinkedHashMap
        ResultVo<List<User>> resultVo1 = JacksonUtils.toJavaObject(sourceStr, ResultVo.class);
        List<User> newList = resultVo1.getValue();
        /*
        下面代码直接报异常了：java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to cn.com.kun.bean.entity.User
        因为丢失泛型了，resultVo1里面的对象早已不是User类型对象
         */
        System.out.println(newList.get(0).getUsername());
    }

    /**
     * 错误示例2
     */
    @Test
    public void testJson2() {

        User user = new User();
        user.setUsername("xyk");
        ResultVo<User> resultVo = ResultVo.valueOfSuccess(user);

        String sourceStr = JacksonUtils.toJSONString(resultVo);
        //根据字符串还原回原来的对象，这时候会发生泛型丢失
        //User类型对象变成LinkedHashMap
        ResultVo<User> resultVo1 = JacksonUtils.toJavaObject(sourceStr, ResultVo.class);
        User newObj = resultVo1.getValue();
        /*
            无论ResultVo里的是List或者是一个对象，都会发生泛型信息丢失。
         */
        System.out.println(newObj.getUsername());
    }


    /**
     * 解决办法：
     * 解决
     * 对于可以指定返回类型的反序列化，可以通过Jackson的API指定反序列化对象的泛型。
     * Map<Integer, List<Integer>> map3 = om.readValue(json, new TypeReference<Map<Integer, List<Integer>>>(){});
     */
    @Test
    public void testJson3() throws IOException {

        User user = new User();
        user.setUsername("xyk");
        List<User> userList = new ArrayList<>();
        userList.add(user);
        ResultVo<List<User>> resultVo = ResultVo.valueOfSuccess(userList);

        String sourceStr = JacksonUtils.toJSONString(resultVo);
        //这样是成功的，泛型信息不会丢失
        ResultVo<List<User>> resultVo1 = JacksonUtils.toJavaObject(sourceStr, new TypeReference<ResultVo<List<User>>>(){});
        List<User> newList = resultVo1.getValue();
        System.out.println(newList.get(0).getUsername());
    }

}