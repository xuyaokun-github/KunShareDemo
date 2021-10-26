package cn.com.kun.service.mybatis;

import cn.com.kun.common.enums.TblExpiredDataRemoveEnum;
import cn.com.kun.mapper.TblExpiredDataRemoveMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库表过期数据清理服务
 *
 * author:xuyaokun_kzx
 * date:2021/7/30
 * desc:
*/
@Service
public class TblExpiredDataRemoveService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TblExpiredDataRemoveService.class);

    @Autowired
    private TblExpiredDataRemoveMapper tblExpiredDataRemoveMapper;

    /**
     * 调度：一天跑一次即可
     */
//    @Scheduled(cron = "0/10 * * * * ?")
//    @Scheduled(cron = "0 0 3 1/1 * ? *") //每天凌晨三点跑(错误的cron,最多支持6位)
//    @Scheduled(cron = "0 0 3 1/1 * ?") //每天凌晨三点跑
    public void scheduled(){

        //抢锁，抢到就跑，抢不到退出等下一次调度
        removeExpiredData();
        //释放锁 TODO
    }

    private void removeExpiredData() {

        LOGGER.info("开始清理过期表数据");
        //遍历枚举类
        for(TblExpiredDataRemoveEnum tblExpiredDataRemoveEnum : TblExpiredDataRemoveEnum.values()) {
            if (tblExpiredDataRemoveEnum.days <= 0){
               continue;
            }
            //根据创建时间进行删除
            Map<String, Object> map = new HashMap();
            //倒退days天
            Date targetCreateTime = DateUtils.addDays(new Date(), (0 - tblExpiredDataRemoveEnum.days));
            map.put("createTime", targetCreateTime);
            map.put("tblName", tblExpiredDataRemoveEnum.tblName);
            int count = tblExpiredDataRemoveMapper.removeData(map);
            LOGGER.info("本次清理表[{}],清理总数：{}", tblExpiredDataRemoveEnum.tblName, count);
        }

    }

}
