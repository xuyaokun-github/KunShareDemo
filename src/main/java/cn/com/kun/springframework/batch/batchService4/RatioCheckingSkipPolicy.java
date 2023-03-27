package cn.com.kun.springframework.batch.batchService4;

import cn.com.kun.springframework.batch.exception.RatioSkippableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

/**
 * 自定义skip-policy
 * org.springframework.batch.core.step.skip.SkipPolicy 是batch框架提供的接口
 *
 * author:xuyaokun_kzx
 * date:2023/2/2
 * desc:
*/
public class RatioCheckingSkipPolicy implements SkipPolicy {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatioCheckingSkipPolicy.class);

    private double skipRatioThreshold;

    public RatioCheckingSkipPolicy(double skipRatioThreshold) {
        this.skipRatioThreshold = skipRatioThreshold;
    }

    @Override
    public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {

        //程序抛出异常是可跳过异常且 符合跳过条件，再允许跳过
        if (t instanceof RatioSkippableException){

            //如何得知总数？
            //第一种方法，通过异常携带这个数据
            RatioSkippableException ratioSkippableException = (RatioSkippableException) t;
            //第二种方法，
            if (canSkip(skipCount, ratioSkippableException.getReadTotalCount())){
                return true;
            }
        }

        return false;
    }


    private boolean canSkip(int skipCount, int readTotalCount) {

        boolean canSkip = true;
        try {
            if (readTotalCount > 0){
                Double ratio = RatioFormatUtil.getRatioForInt(skipCount, readTotalCount);
                //跳过的累计数除以已读取数，得到一个比例，小于等于 配置文件中定义的比例，则允许跳过
                //假如配置为100，表示允许跳过所有异常数据
                canSkip = ratio <= skipRatioThreshold;
                if (!canSkip){
                    LOGGER.info("异常比例跳过机制计算比例[{}/{}={}]超出阈值[{}]，无需继续跳过，即将终止任务 ", skipCount, readTotalCount, ratio, skipRatioThreshold);
                }else {
                    LOGGER.info("异常比例跳过机制允许跳过，计算比例[{}/{}={}]未超出阈值[{}]，任务继续执行", skipCount, readTotalCount, ratio, skipRatioThreshold);
                }
            }
        }catch (Exception e){
            LOGGER.error("RatioCheckingSkipPolicy异常", e);
        }
        return canSkip;

    }


}
