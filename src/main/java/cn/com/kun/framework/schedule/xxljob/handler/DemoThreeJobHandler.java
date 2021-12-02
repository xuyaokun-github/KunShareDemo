package cn.com.kun.framework.schedule.xxljob.handler;


import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * 样例
 *
 * @author xuyaokun
 * @date 2019/3/27 11:35
 */
@JobHandler(value="DemoThreeJobHandler")
@Component
public class DemoThreeJobHandler extends IJobHandler {

    private Logger logger = LoggerFactory.getLogger(DemoThreeJobHandler.class);

    @Override
    public ReturnT<String> execute(String param) throws Exception {

        //使用XxlJobLogger.log输出的日志内容可以控制台端查看
        //但是不能输出到普通的console和file
        XxlJobLogger.log("XXL-JOB, Hello World. 我是定时任务3。");
        logger.info("XXL-JOB, Hello World. 我是定时任务3。");

        //模拟具体的业务逻辑
        for (int i = 0; i < 5; i++) {
            XxlJobLogger.log("beat at:" + i);
            //使用System.out或者log4j等方式输出的日志在控制台看不到
//            System.out.println("beat at:" + i);
            logger.info("beat at:" + i);
            TimeUnit.SECONDS.sleep(1);
        }
        return SUCCESS;
    }

}
