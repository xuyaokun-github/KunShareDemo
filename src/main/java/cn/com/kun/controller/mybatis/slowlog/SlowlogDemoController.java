package cn.com.kun.controller.mybatis.slowlog;


import cn.com.kun.bean.entity.SceneMsgRecordDO;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.mapper.SceneMsgRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequestMapping("/mybatis-slowlog-demo")
@RestController
public class SlowlogDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SlowlogDemoController.class);

    @Autowired
    SceneMsgRecordMapper sceneMsgRecordMapper;

    @GetMapping("/test3")
    public ResultVo test3() throws InterruptedException {

        List<SceneMsgRecordDO> sceneMsgRecordDOList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            sceneMsgRecordDOList.add(buildSceneMsgRecordDO(new SceneMsgRecordDO(UUID.randomUUID().toString().replace("-", "").substring(0, 10),
                    UUID.randomUUID().toString())));
        }

        for (int i = 0; i < sceneMsgRecordDOList.size(); i++) {
            int finalI = i;
            new Thread(()->{
                SceneMsgRecordDO recordDO = sceneMsgRecordDOList.get(finalI);
                recordDO.setCreate_time(new Date());
                recordDO.setUpdate_time(new Date());
                for (int j = 0; j < 50 * 10000; j++) {
                    sceneMsgRecordMapper.insert(recordDO);
                }
                LOGGER.info("插入结束 " + Thread.currentThread().getName());
            }).start();
        }

        ResultVo res = ResultVo.valueOfSuccess();
        return res;
    }

    private SceneMsgRecordDO buildSceneMsgRecordDO(SceneMsgRecordDO sceneMsgRecordDO) {

        sceneMsgRecordDO.setStatus("0");
        sceneMsgRecordDO.setPriority(0);
        sceneMsgRecordDO.setSend_qps_threshold(0);
        sceneMsgRecordDO.setTask_id(UUID.randomUUID().toString());
        sceneMsgRecordDO.setCreate_oper("kunghsu");
        sceneMsgRecordDO.setUpdate_by("kunghsu");
        sceneMsgRecordDO.setSend_request_content(UUID.randomUUID().toString());
        sceneMsgRecordDO.setCreate_time(new Date());
        sceneMsgRecordDO.setUpdate_time(new Date());

        return sceneMsgRecordDO;
    }


}
