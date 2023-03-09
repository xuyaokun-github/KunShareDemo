package cn.com.kun.springframework.batch.batchService4;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.springframework.batch.common.BatchExecCounter;
import cn.com.kun.springframework.batch.exception.RatioSkippableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemProcessor;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 提供一个demo,模拟异常情况
 * Created by xuyaokun On 2020/5/19 22:41
 * @desc: 
 */
public class MyBatchProcessor4 implements ItemProcessor<User, User> {

    private static final Logger logger = LoggerFactory.getLogger(MyBatchProcessor4.class);

    private StepExecution stepExecution;

    public MyBatchProcessor4(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public User process(User item) throws Exception {

        //中间处理器
        /*
        接收的是来自读操作提供的UserMap，然后返回一个User给写操作去做具体的后续处理
        读操作主要负责读，数据来自哪里
        中间处理器重在一些信息的封装
        写操作主要负责保存数据，数据该落地保存在哪里
         */
        String sourceName = item.getFirstname();
        sourceName = sourceName + "-batchJob4";
        logger.debug("进入处理器");
        int i = ThreadLocalRandom.current().nextInt(100);
        User user  = new User();
        user.setFirstname(sourceName + i);
        user.setLastname("kunghsu" + i);
        user.setEmail(i + "-" + item.getEmail());
        user.setPhone(DateUtils.now());
        user.setCreateTime(new Date());
        String phone = item.getPhone();
        String email = item.getEmail();
        if (phone.length() > 7){
//            throw new MyBatchBussinessException("出现不合法异常");
            //抛出父类异常，并不会命中
//            throw new Exception("出现不合法异常");
            //抛出配置中定义的异常
//            throw new SkippableException("出现可跳过的异常");

            //抛出自定义异常-支持按比例判断
            throw new RatioSkippableException("出现可跳过的异常", getReadTotalCount(stepExecution));
        }

        //记录成功次数(随机)
        BatchExecCounter.countSuccess();
//        if (System.currentTimeMillis() % 2 == 0){
//            BatchExecCounter.countSuccess();
//        }else {
//            BatchExecCounter.countFail();
//        }

        //后面的写操作，可以使用一些spring自带提供的，注意这里假如返回的是null，spring不会对它做处理
        /*
        源码org.springframework.batch.core.step.item.SimpleChunkProcessor.transform，这里会对返回的对象进行判断，假如不空才会放入outputs中
         */
//        return null;

        return user;
    }

    private int getReadTotalCount(StepExecution stepExecution) {
        return stepExecution.getReadCount();
    }

}
