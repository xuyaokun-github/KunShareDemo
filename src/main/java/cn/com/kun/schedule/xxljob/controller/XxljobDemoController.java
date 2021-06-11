package cn.com.kun.schedule.xxljob.controller;


import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.schedule.xxljob.bean.XxlJobInfo;
import cn.com.kun.schedule.xxljob.service.XxlJobExtendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 实现xxl-job的扩展demo
 * 需求：
 * 1.业务可通过操作内管管理页面动态生成job,动态生成jobhandler
 * 内管页面指的是业务系统页面，并不是xxl-job-admin页面
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
    private XxlJobExtendService xxlJobExtendService;

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

        return xxlJobExtendService.createJob(xxlJobInfo);
    }


    /**
     * http://localhost:8080/kunsharedemo/xxl-job-extend/start-job?id=21
     * @param id
     * @return
     */
    @GetMapping("/start-job")
    public ResultVo startJob(String id){

        return xxlJobExtendService.startJob(id);
    }

}
