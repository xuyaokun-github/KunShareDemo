package cn.com.kun.springframework.core.jackson;

import cn.com.kun.bean.model.people.People;
import cn.com.kun.common.utils.JacksonUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 *
 *
 *
 *
 *
 *
 */
public class TestBackslash {

    /*




D:\home\json\1.txt在文件里放入：
{"a1":"12345678",
"a2":"12345678",
"a3":"12345678",
"aaa":"12345678","company":"aaa\sss123456"}

下面这个无法模拟：
    { \"a1\":\"12345678\",
\"a2\":\"12345678\",
\"a3\":\"12345678\",
\"aaa\":\"12345678\",\"company\":\"aaa\\sss123456\"}

     */
    public static void main(String[] args) throws IOException {



//        testMethod3();
        testMethod4();
//        testMethod5();

    }

    /**
     * 假如是正常的字符串，有两个反斜杠
     *
     * @throws IOException
     */
    private static void testMethod5() throws IOException {

        String source = FileUtils.readFileToString(new File("D:\\home\\json\\2.txt"), Charset.forName("UTF-8"));
        System.out.println(source);
//        People people2 = JacksonUtils.toJavaObject(source, People.class);
//        System.out.println(people2);
        Map<String, Object> map = JacksonUtils.toMapSupportSpecialChar(source);
        System.out.println(map);

    }

    /**
     *  从文件中读取String
     */
    private static void testMethod4() throws IOException {

        String source = FileUtils.readFileToString(new File("D:\\home\\json\\1.txt"), Charset.forName("UTF-8"));
        System.out.println(source);
//        People people2 = JacksonUtils.toJavaObject(source, People.class);
//        System.out.println(people2);
        Map<String, Object> map = JacksonUtils.toMapSupportSpecialChar(source);
        System.out.println(map);
    }

    private static void testMethod3() {

        //这是正常的字符串，反序列化是正常的（两个反斜杠会被认为是一个反斜杠）
        String source1 = "{\"company\":\"aaa\\\\sss\"}";
//        People people = JacksonUtils.toJavaObject(source1, People.class);
        //这是有问题的字符串，反序列化会出问题（假如只有一个反斜杠，它就无法处理了）
        String source2 = "{ \"aaa\":\"12345678\",\"company\":\"aaa\\sss123456\"}";
//        String source2 = "{ \"aaa\":\"dddd\t\rdddddddd\\\\\n\",\"company\":\"aaa\\sss\"}";

        /*
            反序列化异常，因为字符串里含有右斜杠
            所以这里得到的对象是空
         */
//        source2 = source2.replace("\\", "");
        People people2 = JacksonUtils.toJavaObject(source2, People.class);
        System.out.println(people2);
    }

}
