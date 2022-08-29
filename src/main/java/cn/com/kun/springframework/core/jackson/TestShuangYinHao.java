package cn.com.kun.springframework.core.jackson;

import cn.com.kun.common.utils.JacksonUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by xuyaokun On 2022/8/18 23:47
 * @desc:
 */
public class TestShuangYinHao {

    public static void main(String[] args) {

        //假如value中有双引号，会报错 需要用上 JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS
//        String source = "{\"a1\":\"v1\",\"a3\":\"v3kkkk\"\"\",\"a2\":\"v2\"}";
        String source = "{\"a1\":\"v1\",\"a3\":\"v3kkkk\"888888\"\",\"a2\":\"v2\"}";

        //假如要传双引号，这种是正确的
//        String source = "{\"a1\":\"v1\",\"a3\":\"v3kkkk\\\"\\\"\",\"a2\":\"v2\"}";

        //用阿里的json工具一样报错
//        JSONObject jsonObject = JSONObject.parseObject(source);

        //用Gson也一样报错
//        Map<String, Object> map2 = new Gson().fromJson(source, Map.class);

        //解决方法1,将双引号转为单引号（前提是业务允许！！！！）
//        source = source.replaceAll("\"\"", "''");

        //用spring的Jackson,也一样报错
//        Map<String, Object> map = JacksonUtils.toMap(source);

        //解决方法2
        Map<String, Object> map = JacksonUtils.toMapSupportSpecialChar(source);

        System.out.println(JacksonUtils.toJSONString(map));
    }

    /**
     * Jackson会默认将value里的双引号转义， " 会转义成 \"
     */
    @Test
    public void test1(){

        Map<String, Object> map = new HashMap<>();
        map.put("name", "kunghsu");
        map.put("name1", "kunghsu\"\"vvvvv");
        String sourceJson = JacksonUtils.toJSONString(map);
        System.out.println(sourceJson);
        Map<String, Object> newMap = JacksonUtils.toMap(sourceJson);
        System.out.println(JacksonUtils.toJSONString(newMap));
    }

    @Test
    public void test2(){

        Map<String, Object> map = new HashMap<>();
        map.put("name", "kunghsu");
        map.put("name1", "kunghsu\"\"vvvvv");
        //阿里的fastjson也会自动转义双引号，所以下面再次反序列化时不会出问题
        String sourceJson = JSON.toJSONString(map);
        System.out.println(sourceJson);
        Map<String, Object> newMap = JacksonUtils.toMap(sourceJson);
        System.out.println(JacksonUtils.toJSONString(newMap));
    }



}
