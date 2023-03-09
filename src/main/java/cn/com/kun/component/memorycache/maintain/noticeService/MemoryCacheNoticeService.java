package cn.com.kun.component.memorycache.maintain.noticeService;

import cn.com.kun.component.memorycache.vo.MemoryCacheNoticeMsg;

public interface MemoryCacheNoticeService {

    /**
     * 发送缓存通知
     * @param noticeMsg
     */
    void sendBroadcastNotice(MemoryCacheNoticeMsg noticeMsg);

}
