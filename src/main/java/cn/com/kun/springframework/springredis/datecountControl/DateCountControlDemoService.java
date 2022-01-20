package cn.com.kun.springframework.springredis.datecountControl;

import cn.com.kun.springframework.springredis.RedisTemplateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class DateCountControlDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DateCountControlDemoService.class);

    private String storeId = "abc";
    private String bizPrefix = "coupon_send";
    private List<String> dateList = new ArrayList<>();

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    DateCountStatisticsHelper dateCountStatisticsHelper;

    @Autowired
    RedisTemplateHelper redisTemplateHelper;

    @PostConstruct
    public void init(){
        dateList.add("20220112");
        dateList.add("20220113");
        dateList.add("20220114");
    }

    public void test1() {

        /*
            放入50万个key
         */
//        for (int i = 0; i < 50 * 10000; i++) {
//
//            String key = bizPrefix + ":" + storeId + ":" + UUID.randomUUID().toString();
//            dateList.forEach(date->{
//                redisTemplate.opsForHash().put(key, date, 1);
//            });
//        }

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 5 * 10000; j++) {
                    String key = bizPrefix + ":" + storeId + ":" + UUID.randomUUID().toString();
                    dateList.forEach(date->{
                        redisTemplate.opsForHash().put(key, date, 1);
                    });
                }
            }).start();
        }



    }

    public void test2() {

        /*
            放入50万个key
         */
//        for (int i = 0; i < 50 * 10000; i++) {
//
//            String key = bizPrefix + ":" + UUID.randomUUID().toString();
//            Map map = new HashMap();
//            dateList.forEach(date->{
//                map.put(date, 1);
//            });
//            redisTemplate.opsForHash().put(key, storeId, map);
//        }

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 5 * 10000; j++) {
                    String key = bizPrefix + ":" + UUID.randomUUID().toString();
                    Map map = new HashMap();
                    dateList.forEach(date->{
                        map.put(date, 1);
                    });
                    redisTemplate.opsForHash().put(key, storeId, map);
//                    redisTemplate.opsForHash().put(key, storeId, JacksonUtils.toJSONString(map));
                }
            }).start();
        }

    }

    /**
     * 用字符串结构
     */
    public void test3() {

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 5 * 10000; j++) {
                    String key = bizPrefix + ":" + storeId + ":" + UUID.randomUUID().toString();
                    dateList.forEach(date->{
                        String newKey = key + ":" + date;
                        redisTemplate.opsForValue().set(newKey, 1);
                    });
                }
            }).start();
        }

    }

    public void testDateCountStatisticsHelper(){

        //存
        List<String> dateList = new ArrayList<>();
        dateList.add("20220112");
        dateList.add("20220113");
        dateList.add("20220114");
        String key = bizPrefix + ":" + storeId + ":" + "kunghsu";
        dateList.forEach(date->{
            dateCountStatisticsHelper.add(key, date, 2);
        });
        dateCountStatisticsHelper.add(key, "20210210", 2);

        //查
        int count = dateCountStatisticsHelper.get(key, "20220101", "20220201");
        LOGGER.info("获取到总数：{}", count);

    }


    public void testDateCountStatisticsHelperDelete(){

        //删
        dateCountStatisticsHelper.remove(bizPrefix + ":" + storeId + ":");

//        Set<String> keySet = redisTemplateHelper.scan(bizPrefix + ":" + storeId + ":");
//        LOGGER.info("扫描获取到总数：{}", keySet.size());

    }

}
