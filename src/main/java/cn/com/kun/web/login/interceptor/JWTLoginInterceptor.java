package cn.com.kun.web.login.interceptor;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.common.utils.AESUtils;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.web.login.context.UserContext;
import cn.com.kun.web.login.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static cn.com.kun.web.login.util.JwtUtil.AES_KEY;

/**
 *
 * Created by xuyaokun On 2020/10/26 23:42
 * @desc:
 */
public class JWTLoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 简单的白名单，登录这个接口直接放行
        if ("/logindemo4Jwt/loginByJwt".equals(request.getRequestURI())) {
            return true;
        }

        // 从请求头中获取token字符串并解析
        String token = request.getHeader("kunghsu_token");
        //先进行AES解密，再JWT解析
        token = AESUtils.decrypt(token, AES_KEY);
        //TODO 支持登录注销，token销毁（在这里补充一个判断，判断token是否还有效）
        Claims claims = JwtUtil.parse(token);
        // 已登录就直接放行
        if (claims != null) {
            // 将我们之前放到token中的userName给存到上下文对象中
            //也可以把更具体的json放入自定义的上下文对象中，
            // 后续的service层可以通过这个UserContext拿到sso信息
            UserContext.add(claims.getSubject());
            return true;
        }

        // 走到这里就代表是其他接口，且没有登录
        // 设置响应数据类型为json（前后端分离）
        sendNoLogin(response);
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        // 请求结束后要从上下文对象删除数据，如果不删除则可能会导致内存泄露
        UserContext.remove();
        super.afterCompletion(request, response, handler, ex);
    }

    /**
     * 返回提示登录
     * @param response
     * @throws IOException
     */
    private void sendNoLogin(HttpServletResponse response) throws IOException {
        // 设置响应数据类型为json（前后端分离）
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        // 设置响应内容，结束请求
        out.write(JacksonUtils.toJSONString(ResultVo.valueOfError("未登录！")));
        out.flush();
        out.close();
    }

}
