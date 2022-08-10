package cn.com.kun.springframework.core.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class SpringTransactionDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringTransactionDemoService.class);

    @Transactional
    public void method1(){

        check();
    }

    public void method2(){
        check();
    }

    private void check(){
        if (TransactionSynchronizationManager.isSynchronizationActive()){
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    /*
                        这里不仅可以复写提交后方法，还能复写其他的。但复写before意义不大，因为进到这里的时候，事务已经开始了
                        可以复写org.springframework.transaction.support.TransactionSynchronizationAdapter.beforeCommit
                     */
                    LOGGER.info("执行afterCommit方法");
                }
            });
            //处在事务中
            LOGGER.info("处在事务中");
        }else {
            //不在事务中
            LOGGER.info("不在事务中");
        }
    }


}
