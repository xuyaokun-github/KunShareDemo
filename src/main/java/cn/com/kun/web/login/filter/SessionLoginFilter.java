package cn.com.kun.web.login.filter;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.common.vo.User;
import cn.com.kun.utils.JacksonUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by xuyaokun On 2020/10/26 22:19
 * @desc: 
 */
public class SessionLoginFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 简单的白名单，登录这个接口直接放行
        if ("/logindemo/login".equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 已登录就放行
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 走到这里就代表是其他接口，且没有登录
        sendNoLogin(response);
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
