package cn.com.kun.schedule.xxljob.controller;


import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.schedule.xxljob.bean.XxlJobInfo;
import cn.com.kun.schedule.xxljob.service.XxlJobExtendService;
import com.xxl.job.core.biz.model.ReturnT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * 实现xxl-job的扩展demo
 * 需求：
 * 1.业务可通过操作内管管理页面动态生成job,动态生成jobhandler
 *
 * author:xuyaokun_kzx
 * date:2021/6/9
 * desc:
*/
@RequestMapping("/xxl-job-extend")
@RestController
public class XxljobDemoController {

    private Logger LOGGER = LoggerFactory.getLogger(XxljobDemoController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${xxlJobAdminUrl:http://localhost:8061/xxl-job-admin/jobinfo/add}")
    private String xxlJobAdminUrl;

    @Autowired
    private XxlJobExtendService xxlJobExtendService;
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
    @GetMapping("/create-job")
    public ResultVo testJsonFormat(){

        //这个应该用每个任务的ID作为唯一标志生成executorHandlerName
        String executorHandlerName = "executorHandler" + System.currentTimeMillis();

        //下面是必须的属性
        XxlJobInfo xxlJobInfo = new XxlJobInfo();
        xxlJobInfo.setJobGroup(2);//任务组ID,每个执行器都会有一个组ID,这个组ID,可以放系统参数，然后读取
        xxlJobInfo.setJobDesc("任务描述xxxx" + executorHandlerName);//任务描述
        xxlJobInfo.setExecutorRouteStrategy("FIRST");//默认的执行策略
        xxlJobInfo.setJobCron("0/10 * * * * ?");//时间表达式(必须是合法格式例如：)
        xxlJobInfo.setGlueType("BEAN");//运行模式，默认是BEAN模式
        xxlJobInfo.setExecutorHandler(executorHandlerName);//处理器的bean名
        xxlJobInfo.setExecutorBlockStrategy("SERIAL_EXECUTION");
        xxlJobInfo.setAuthor("xyk");//负责人
//        xxlJobInfo.setExecutorParam("");//执行参数

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
         * 如何去除登录校验，必须改xxl-job-admin代码，使用@PermissionLimit(limit = false)，将JobInfoController的接口排除校验
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, Charset.forName("UTF-8")));
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ReturnT result = restTemplate.postForObject(xxlJobAdminUrl, request, ReturnT.class);
        LOGGER.info("请求xxl-job-admin接口{} 返回：{}", xxlJobAdminUrl, JacksonUtils.toJSONString(result));
        //返回的内容就是在xxl-job-admin平台的jobID
        String content = (String) result.getContent();
        //这里拿到xxl-job-admin平台的jobID应该和自定义的定时任务做一个绑定，后续方便停止和启动
        LOGGER.info("本次创建的jobID：{}", content);

        //动态注册bean
        //用executorHandlerName作为bean的ID
        xxlJobExtendService.register(executorHandlerName);

        return ResultVo.valueOfSuccess("添加job成功");
    }


    /**
     * 启动一个任务
     * Request URL: http://localhost:8061/xxl-job-admin/jobinfo/start
     * Request Method: POST
     * id=3
     * {"code":200,"msg":null,"content":null}
     */
    @GetMapping("/start-job")
    public ResultVo startJob(String handlerName){

        /**
         * msg：job handler [executorHandler1623225468515] not found.
         */

        return ResultVo.valueOfSuccess("");
    }



}
