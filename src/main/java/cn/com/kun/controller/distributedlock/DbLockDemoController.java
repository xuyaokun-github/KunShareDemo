package cn.com.kun.controller.distributedlock;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.service.distributedlock.DBLockDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/dblock-demo")
@RestController
public class DbLockDemoController {

    @Autowired
    private DBLockDemoService dbLockDemoService;

    @GetMapping("/testRunFastJob")
    public ResultVo<String> testRunFastJob(){

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    dbLockDemoService.test();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        return ResultVo.valueOfSuccess("");
    }

    @GetMapping("/testRunLongTimeJob")
    public ResultVo<String> testRunLongTimeJob(){

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    dbLockDemoService.testRunLongTimeJob();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        return ResultVo.valueOfSuccess("");
    }

    @GetMapping("/testRunFastJobByLockMethod")
    public ResultVo<String> testRunFastJobByLockMethod(){

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                    dbLockDemoService.testByLockMethod();
            }).start();
        }

        return ResultVo.valueOfSuccess("");
    }

    @GetMapping("/testRunLongTimeJobByLockMethod")
    public ResultVo<String> testRunLongTimeJobByLockMethod(){

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    dbLockDemoService.testRunLongTimeJobByLockMethod();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        return ResultVo.valueOfSuccess("");
    }

    @GetMapping("/testRunFastJobByAnnotation")
    public ResultVo<String> testRunFastJobByAnnotation(){

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                dbLockDemoService.testRunFastJobByAnnotation();
            }).start();
        }

        return ResultVo.valueOfSuccess("");
    }

    @GetMapping("/testRunLongTimeJobByAnnotation")
    public ResultVo<String> testRunLongTimeJobByAnnotation(){

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    dbLockDemoService.testRunLongTimeJobByAnnotation();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        return ResultVo.valueOfSuccess("");
    }
}
