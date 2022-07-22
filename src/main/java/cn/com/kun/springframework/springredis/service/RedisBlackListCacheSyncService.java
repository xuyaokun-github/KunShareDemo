package cn.com.kun.springframework.springredis.service;

import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.component.redis.RedisTemplateHelper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 黑名单缓存同步服务
 * 针对双活多redis架构，一边机房做了黑名单新增之后，另一边机房需要扫描数据库表进行数据同步。
 * 判断逻辑：比较数据的最新更新时间，假如时间比它大则做同步
 * 假如查出来的黑名单记录主键比之前缓存的要大，说明要做同步，否则不需要
 * 查询的时候，用更新时间做流式查询
 *
 * 假如黑名单数据支持删除，下面的逻辑要稍微调整一下。
 * mysql的日期类型只支持到秒级。
 *
 * author:xuyaokun_kzx
 * date:2021/7/19
 * desc:
 */
@Component
public class RedisBlackListCacheSyncService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisBlackListCacheSyncService.class);

    private String blacklistSyncPrefix = "BLACKLISTSYNC:";

    @Autowired
    private RedisTemplateHelper redisTemplateHelper;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    //30秒一次(假如redis是双活双用架构再开启调度，假如是单用即只同时使用一套redis，无需同步)
//    @Scheduled(fixedRate = 30000L)
//    @Scheduled(fixedRate = 3000L)
    public void sync() {

        LOGGER.info("开始同步黑名单数据至redis, {}", DateUtils.now());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        //1.获取redis中的最近更新时间和最近一次同步的ID主键
//        String maxId = (String) redisTemplateHelper.get(blacklistSyncPrefix + "maxId");
//        String lastUpdateTime = (String) redisTemplateHelper.get(blacklistSyncPrefix + "lastUpdateTime");
//        if (StringUtils.isEmpty(maxId)){
//            maxId = "0";
//        }
//        if (StringUtils.isEmpty(lastUpdateTime)){
//            lastUpdateTime = "2020-07-19 00:00:00";
//        }
//        AtomicReference<String> id = new AtomicReference<>(maxId);
//        AtomicReference<String> updateTime = new AtomicReference<>(lastUpdateTime);
//        Map<String, Object> map = new HashMap<>();
//        map.put("createTime", DateUtils.toDate(lastUpdateTime, PATTERN_ONE));
//        map.put("maxId", Long.parseLong(maxId));
//        try (
//                // 使用 sqlSession 手动获取 Mapper 对象，否则会出现 "A Cursor is already closed" 异常
//                SqlSession sqlSession = sqlSessionFactory.openSession();
//                //这里我暂时用 学生dao层模拟黑名单dao层，需要用流式查询
//                Cursor<Student> cursor = sqlSession.getMapper(StudentMapper.class).findStudentStreamByCondition(map)
//        ) {
//            String finalMaxId = maxId;
//            cursor.forEach(student -> {
////                LOGGER.info("student info:{}", JacksonUtils.toJSONString(student));
//                if (Long.parseLong(finalMaxId) < student.getId()){
//                    //需要做同步
//                    //调同步逻辑
//                    LOGGER.info("sync student info:{}", JacksonUtils.toJSONString(student));
//                    //假如同步失败，等一下一次继续同步
//                    id.set(student.getId().toString());
//                    updateTime.set(DateUtils.toStr(student.getCreateTime(), PATTERN_ONE));
//                }
//            });
//            //索引是从0开始！
//            LOGGER.info("流式查询全部结束，处理总数：{}", cursor.getCurrentIndex() + 1);
//        } catch (IOException e) {
//            LOGGER.error("查询异常", e);
//            return;//返回，等下一次同步，无需更新redis
//        }
//
//        //存新的更新时间和ID主键到redis
//        redisTemplateHelper.set(blacklistSyncPrefix + "maxId", id.get());
//        redisTemplateHelper.set(blacklistSyncPrefix + "lastUpdateTime", updateTime.get());
    }

}
