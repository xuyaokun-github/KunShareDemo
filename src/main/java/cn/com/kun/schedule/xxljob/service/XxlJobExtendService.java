package cn.com.kun.schedule.xxljob.service;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.schedule.xxljob.bean.XxlJobInfo;
import cn.com.kun.schedule.xxljob.bean.XxlJobUser;
import cn.com.kun.schedule.xxljob.handler.ExecTemplateJobHandler;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 针对xxl-job-admin做的一些扩展（支持版本2.1.2）：
 * 1.动态添加job,启动job
 *
 * author:xuyaokun_kzx
 * date:2021/6/11
 * desc:
*/
@Service
public class XxlJobExtendService implements BeanFactoryAware {

    private Logger LOGGER = LoggerFactory.getLogger(ExecTemplateJobHandler.class);

    private DefaultListableBeanFactory myListableBeanFactory;

    private String LOGIN_TOKEN = "";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${xxlJobAdminUrl:http://localhost:8061/xxl-job-admin/jobinfo/add}")
    private String xxlJobAdminUrl;

    @Value("${xxlJobAdminUrl:http://localhost:8061/xxl-job-admin/jobinfo/start}")
    private String xxlJobAdminStartJobUrl;
    /**
     * 注册bean
     * @param executorHandlerName
     */
    public void register(String executorHandlerName){
        /*
            添加进spring容器之后，bean就已经可用，但要考虑一个问题：重启之后，必须重新添加bean
            每次重启都要把所有定时任务对应的bean定义好放进容器，每个pod都需要执行该步骤
         */
        RootBeanDefinition beanDefinition = new RootBeanDefinition(ExecTemplateJobHandler.class);
        //设置成单例
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        //注意这里放入的bean在容器中是单例的
        //反射构建实例或者直接new
        //注册bean定义
        myListableBeanFactory.registerBeanDefinition(executorHandlerName, beanDefinition);
        //主动调用registerSingleton，有一个缺点，无法自动解析注解
//        ExecTemplateJobHandler templateJobHandler = new ExecTemplateJobHandler(executorHandlerName);
//        myListableBeanFactory.registerSingleton(executorHandlerName, templateJobHandler);
        LOGGER.info("向spring容器注册bean:{} 成功", executorHandlerName);
        //是否可以不放入spring容器？其实是可以的，但是假如不放入容器，就无法在类上用注入特性

        //如何做到放入容器，同时也能支持类上的注解解析，正确的姿势是让spring来创建这个bean
        ExecTemplateJobHandler templateJobHandler = (ExecTemplateJobHandler) myListableBeanFactory.getBean(executorHandlerName);

        XxlJobSpringExecutor.registJobHandler(executorHandlerName, templateJobHandler);
        LOGGER.info("向XxlJobSpringExecutor容器注册bean:{} 成功", executorHandlerName);

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory)beanFactory;
        this.myListableBeanFactory = listableBeanFactory;
    }

    /**
     * 生成token,用于绕过xxl-job-admin的cookie校验
     * @return
     */
    public String makeToken(){

        //这个值只需要生成一次，后续直接复用就行
        if (StringUtils.isEmpty(LOGIN_TOKEN)){
            //设置一个固定用户，用于绕过xxl-job-admin的登录拦截(这个用户必须真实有效)
            XxlJobUser xxlJobUser = new XxlJobUser();
            xxlJobUser.setUsername("admin");
            xxlJobUser.setPassword("123456");
            //塞到cookie里的密码信息是MD5值，用spring工具类加密的
            xxlJobUser.setPassword(DigestUtils.md5DigestAsHex(xxlJobUser.getPassword().getBytes()));
            //格式化成json即可，因为xxl-job-admin服务端会将这个json反序列化
            String tokenJson = JacksonUtils.toJSONString(xxlJobUser);
            //将字符串转成十六进制的字符串
            LOGIN_TOKEN = new BigInteger(tokenJson.getBytes()).toString(16);
        }
        return LOGIN_TOKEN;
    }

    /**
     * 创建job
     * (这个接口可对外提供给内管服务调用，内管写完数据库，就可以调这个接口，把这个任务的唯一ID和定时任务的运行信息传到这个方法里
     * 这个方法主要负责的就是在xxl-job-admin平台端生成一个定时任务job)
     *
     * Request URL: http://localhost:8061/xxl-job-admin/jobinfo/add
     * Request Method: POST
     * Content-Type: application/x-www-form-urlencoded; charset=UTF-8
     *
     * 具体传参：
     * jobGroup=2&
     * jobDesc=%E5%8A%A8%E6%80%81%E7%94%9F%E6%88%90%E7%9A%84%E5%AE%9A%E6%97%B6%E4%BB%BB%E5%8A%A1&
     * executorRouteStrategy=FIRST&
     * cronGen_display=0%2F10+*+*+*+*+%3F&（这个也不需要传给后端，前端展示用的）
     * second=3& (这个没用的)
     * jobCron=0%2F10+*+*+*+*+%3F&
     * glueType=BEAN&
     * executorHandler=myjob1&
     * executorBlockStrategy=SERIAL_EXECUTION&
     * childJobId=&
     * executorTimeout=0&
     * executorFailRetryCount=0&
     * author=xyk&
     * alarmEmail=&
     * executorParam=&
     * glueRemark=GLUE%E4%BB%A3%E7%A0%81%E5%88%9D%E5%A7%8B%E5%8C%96&
     * glueSource=
     *
     * 返回内容：
     * {"code":200,"msg":null,"content":"3"}
     *
     * @return
     */
    public ResultVo createJob(XxlJobInfo xxlJobInfo) {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("jobGroup", String.valueOf(xxlJobInfo.getJobGroup()));
        map.add("jobDesc", xxlJobInfo.getJobDesc());
        map.add("executorRouteStrategy", xxlJobInfo.getExecutorRouteStrategy());
        map.add("jobCron", xxlJobInfo.getJobCron());
        map.add("glueType", xxlJobInfo.getGlueType());
        map.add("executorHandler", xxlJobInfo.getExecutorHandler());
        map.add("executorBlockStrategy", xxlJobInfo.getExecutorBlockStrategy());
        map.add("author", xxlJobInfo.getAuthor());

        //调admin的接口：com.xxl.job.admin.controller.JobInfoController#add
        /**
         * 这个接口有登录权限校验，想直接调通没这么简单，必须要对xxl-job-admin做一点定制化修改，去除登录校验
         * 如何去除登录校验？
         * 方法1：改xxl-job-admin代码，使用@PermissionLimit(limit = false)，将JobInfoController的接口排除校验
         * 方法2：手动塞cookie，模拟已经登录了
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, Charset.forName("UTF-8")));

        List<String> cookies = new ArrayList<String>();
        //cookies字符串可以有多个
        cookies.add("XXL_JOB_LOGIN_IDENTITY=" + makeToken() + "; Path=/; HttpOnly");
        //在 header 中存入cookies
        //将cookie存入请求头
        headers.put(HttpHeaders.COOKIE, cookies);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ReturnT result = restTemplate.postForObject(xxlJobAdminUrl, request, ReturnT.class);
        LOGGER.info("请求xxl-job-admin接口{} 返回：{}", xxlJobAdminUrl, JacksonUtils.toJSONString(result));
        //返回的内容就是在xxl-job-admin平台的jobID
        String content = (String) result.getContent();
        //这里拿到xxl-job-admin平台的jobID应该和自定义的定时任务做一个绑定，后续方便停止和启动
        LOGGER.info("本次创建的jobID：{}", content);

        //动态注册bean
        //用executorHandlerName作为bean的ID
        register(xxlJobInfo.getExecutorHandler());

        return ResultVo.valueOfSuccess("添加job成功");
    }


    /**
     * 启动一个任务

     */
    /**
     * 启动任务
     * Request URL: http://localhost:8061/xxl-job-admin/jobinfo/start
     * Request Method: POST
     * id=3
     *
     * {"code":200,"msg":null,"content":null}
     * @param id
     * @return
     */
    public ResultVo startJob(String id) {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("id", id);//任务的ID
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, Charset.forName("UTF-8")));

        List<String> cookies = new ArrayList<String>();
        //cookies字符串可以有多个
        cookies.add("XXL_JOB_LOGIN_IDENTITY=" + makeToken() + "; Path=/; HttpOnly");
        //在 header 中存入cookies
        //将cookie存入请求头
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ReturnT result = restTemplate.postForObject(xxlJobAdminStartJobUrl, request, ReturnT.class);
        LOGGER.info("请求xxl-job-admin接口{} 返回：{}", xxlJobAdminStartJobUrl, JacksonUtils.toJSONString(result));
        return ResultVo.valueOfSuccess(result);
    }

}
