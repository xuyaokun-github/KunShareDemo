package cn.com.kun.component.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogInterceptor extends HandlerInterceptorAdapter {

    public final static Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        long startTime = System.currentTimeMillis();
        logger.info("\n-------- LogInterceptor.preHandle --- Request URL: {} Start Time: {}",
                request.getRequestURL(), System.currentTimeMillis());
        request.setAttribute("startTime", startTime);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, //
                           Object handler, ModelAndView modelAndView) throws Exception {

        logger.info("\n-------- LogInterception.postHandle --- Request URL: {}", request.getRequestURL());
        // You can add attributes in the modelAndView
        // and use that in the view page
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, //
                                Object handler, Exception ex) throws Exception {
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        logger.info("\n-------- LogInterception.afterCompletion --- Request URL: {} End Time: {} Time cost: {}ms",
                request.getRequestURL(), endTime, (endTime - startTime));
    }

}
