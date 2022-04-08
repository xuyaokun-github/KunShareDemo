package cn.com.kun.springframework.actuator.endpoint;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;

/**
 *
 * 加了@Endpoint注解的类，会被springmvc转成一个web接口
 * 为什么不用@WebEndpoint？
 * org.springframework.boot.actuate.endpoint.annotation.Endpoint
 *
 * author:xuyaokun_kzx
 * date:2021/11/25
 * desc:
*/
//主要需要@Endpoint、@Component、@ReadOperation、@Selector四个注解即可搞定，代码示例如下：
//@Endpoint(id = "customPoint")
//@WebEndpoint(id = "customPoint")
//@Component
public class CustomEndPoint {

    /**
     * @Selector有什么作用？
     * 通过参数定义更多的路径
     * http://localhost:10001/kunsharedemo/actuator/customPoint/name?name=kunghsu
     * http://localhost:10001/kunsharedemo/actuator/customPoint/rrrrrrrr?name=kunghsu
     * http://localhost:10001/kunsharedemo/actuator/customPoint/kunghsu
     * 上面三种路径，方法里拿到的name都是kunghsu
     *
     * 很奇怪这个注解
     * 错误的访问方式：
     * http://localhost:10001/kunsharedemo/actuator/customPoint?name=kunghsu
     * 这样进不去本方法，匹配的却是 @ReadOperation无参的方法
     * @param name
     * @return
     */
    @ReadOperation
    public String getCustom(@Selector String name) {
        return "MyName is ." + name;
    }

    /**
     * 同理
     * http://localhost:10001/kunsharedemo/actuator/customPoint/55555555/444
     * 是可以匹配的
     *
     * @param name
     * @param name2
     * @return
     */
    @ReadOperation
    public String getCustom3(@Selector String name, @Selector String name2) {
        return "My CustomEndPoint getCustom3" + " " + name + " " + name2;
    }

    /**
     * @Selector注解不是必须的
     * 加了注解@ReadOperation的方法不能同名
     *
     * 访问方式：http://localhost:10001/kunsharedemo/actuator/customPoint
     * @return
     */
    //可以指定返回的类型等
//    @ReadOperation(produces = TextFormat.CONTENT_TYPE_004)
    @ReadOperation
    public String getCustom2() {
        return "My CustomEndPoint getCustom2";
    }

    /**
     * 不能同时定义两个不带参数的方法
     * 否则报错：Caused by: java.lang.IllegalStateException: Unable to map duplicate endpoint operations: [web request predicate GET to path 'customPoint' produces: application/vnd.spring-boot.actuator.v2+json,application/json] to customEndPoint
     * @return
     */
//    @ReadOperation
//    public String getCustom3() {
//        return "My CustomEndPoint getCustom3";
//    }


}
