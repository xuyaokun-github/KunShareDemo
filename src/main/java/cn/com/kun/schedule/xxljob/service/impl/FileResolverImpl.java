package cn.com.kun.schedule.xxljob.service.impl;

import cn.com.kun.schedule.xxljob.handler.ExecTemplateJobHandler;
import cn.com.kun.schedule.xxljob.service.FileResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 这是一个单例服务层，模拟一种处理逻辑，一个任务执行中可能会有多种处理逻辑
 * 例如查库，查redis，写文件等
 * 如何在ExecTemplateJobHandler注入这些处理逻辑呢？
 * 可以直接通过注解注入吗？
 *
 * author:xuyaokun_kzx
 * date:2021/6/9
 * desc:
*/
@Service
public class FileResolverImpl implements FileResolver {

    private Logger LOGGER = LoggerFactory.getLogger(ExecTemplateJobHandler.class);

    @Override
    public void resolver() {

        LOGGER.info("正在进行文件解析。。。。。。。。");
    }

}
