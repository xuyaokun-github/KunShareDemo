package cn.com.kun.framework.schedule.xxljob.handler;

import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.framework.schedule.xxljob.service.FileResolver;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 这是一个模板类（基于它创建bean放入spring容器）
 *
 * author:xuyaokun_kzx
 * date:2021/6/9
 * desc:
*/
@JobHandler
public class ExecTemplateJobHandler extends IJobHandler {

    private Logger LOGGER = LoggerFactory.getLogger(ExecTemplateJobHandler.class);

    private String beanId;

    //这里无法正常进行注入，因为ExecTemplateJobHandler是手动new放入容器的，不是spring来new的，所以它不会处理@Autowired
    //有什么方法注入呢？第一种还是通过构造函数，将其他服务层当成参数传入
    @Autowired
    private FileResolver fileResolver;

//    public ExecTemplateJobHandler(String executorHandlerName){
//        this.beanId = executorHandlerName;
//    }

    @Override
    public ReturnT<String> execute(String param) throws Exception {

        /**
         * 可能会有多个job都共用这个handler
         * 问题来了，怎么知道这次执行，是哪一个job触发的呢？
         * 所以需要用到构造函数，把构建bean时的beanID留下来
         */
        LOGGER.info("beanId:{}, 执行开始时间：{}", this.beanId, DateUtils.now());
        LOGGER.info("根据该bean的名字，查询数据库获取定时任务记录");
        LOGGER.info("判断文件是否存在，获取文件");
        if (fileResolver != null){
            fileResolver.resolver();
        }
        LOGGER.info("文件解析入库，操作");
        LOGGER.info("文件逐行处理");
        return ReturnT.SUCCESS;
    }


}
