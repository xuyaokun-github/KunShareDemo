package cn.com.kun.controller.monitor;

import cn.com.kun.service.monitor.MonitorDaoMockServcie;
import cn.com.kun.service.monitor.MonitorDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/monitor-demo")
@RestController
public class MonitorDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(MonitorDemoController.class);

    @Autowired
    private MonitorDemoService monitorDemoService;

    @Autowired
    private MonitorDaoMockServcie monitorDaoMockServcie;

    @GetMapping("/test")
    public String test(){

        monitorDemoService.doService("888");
        return "OK";
    }

    @GetMapping("/testMoreMonitorTask")
    public String testMoreMonitorTask(){

        monitorDemoService.doService("888");
        monitorDemoService.doService("999");
        return "OK";
    }


    @GetMapping("/addStatus4Done")
    public String testAddStatus(){

        monitorDaoMockServcie.addStatus("888", "Done");
        return "OK";
    }

    @GetMapping("/addStatus4Fail")
    public String addStatus4Fail(){

        monitorDaoMockServcie.addStatus("888", "Fail");
        return "OK";
    }

    @GetMapping("/addStatus4Stop")
    public String addStatus4Stop(){

        monitorDaoMockServcie.addStatus("888", "Stop");
        return "OK";
    }


    @GetMapping("/addStatus4Done2")
    public String addStatus4Done2(){

        monitorDaoMockServcie.addStatus("999", "Done");
        return "OK";
    }

    @GetMapping("/addStatus4Fail2")
    public String addStatus4Fail2(){

        monitorDaoMockServcie.addStatus("999", "Fail");
        return "OK";
    }

    @GetMapping("/addStatus4Stop2")
    public String addStatus4Stop2(){

        monitorDaoMockServcie.addStatus("999", "Stop");
        return "OK";
    }


}
