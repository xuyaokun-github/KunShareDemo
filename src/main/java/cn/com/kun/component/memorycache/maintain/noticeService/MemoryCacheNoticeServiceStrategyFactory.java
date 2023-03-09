package cn.com.kun.component.memorycache.maintain.noticeService;

import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryCacheNoticeServiceStrategyFactory {

    private static Map<String, MemoryCacheNoticeService> services = new ConcurrentHashMap();

    public static MemoryCacheNoticeService getByNoticeType(String type){
        return services.get(type);
    }

    public static void register(String noticeType, MemoryCacheNoticeService memoryCacheNoticeService){
        Assert.notNull(noticeType, "noticeType can't be null");
        services.put(noticeType, memoryCacheNoticeService);
    }

}
