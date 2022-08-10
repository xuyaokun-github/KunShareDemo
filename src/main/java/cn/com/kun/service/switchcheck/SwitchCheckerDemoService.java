package cn.com.kun.service.switchcheck;

import cn.com.kun.component.switchcheck.SwitchChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SwitchCheckerDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SwitchCheckerDemoService.class);

    @Autowired
    SwitchChecker switchChecker;


    public void test() {

        LOGGER.info("获取到的开关结果：{}", switchChecker.isOpen("NBA", true));
        LOGGER.info("获取到的开关结果：{}", switchChecker.isOpen("WNBA", true));
    }
}
