package cn.com.kun.web.login.context;

import cn.com.kun.common.vo.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 封装一个对象，供服务层方便获取request和request里的常用内容
 * Created by xuyaokun On 2020/10/26 22:48
 * @desc:
 */
public final class RequestContext {

    private RequestContext(){}

    public static HttpServletRequest getCurrentRequest() {
        // 获取当前request请求对象
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    }

    public static User getCurrentUser() {
        // 通过request对象获取session对象，再获取当前用户对象
        return (User)getCurrentRequest().getSession().getAttribute("user");
    }

    //还可以获取其他单点信息

}
