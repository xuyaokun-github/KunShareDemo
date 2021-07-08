package cn.com.kun.controller.clusterlock;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.service.clusterlock.DBClusterLockDemoService;
import cn.com.kun.service.clusterlock.DBClusterLockDemoService2;
import cn.com.kun.service.clusterlock.DBClusterLockDemoService3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/clusterlock-demo")
@RestController
public class ClusterlockDemoController {

    @Autowired
    DBClusterLockDemoService dbClusterLockDemoService;

    @Autowired
    DBClusterLockDemoService2 dbClusterLockDemoService2;

    @Autowired
    DBClusterLockDemoService3 dbClusterLockDemoService3;

    @GetMapping("/test")
    public ResultVo<String> test(){

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                dbClusterLockDemoService.test();
            }).start();
        }

        return ResultVo.valueOfSuccess("");
    }


    @GetMapping("/test2")
    public ResultVo<String> test2(){

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                dbClusterLockDemoService.test();
            }).start();
            new Thread(()->{
                dbClusterLockDemoService2.test2();
            }).start();
        }
        return ResultVo.valueOfSuccess("");
    }

    /**
     * 验证上锁注解是否可用
     * @return
     */
    @GetMapping("/testWithAnno")
    public ResultVo<String> test3(){

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                dbClusterLockDemoService2.testWithAnno();
            }).start();
        }
        return ResultVo.valueOfSuccess("");
    }

    /**
     * 验证锁事务和业务逻辑的事务是否有冲突
     * @return
     */
    @GetMapping("/testWithAnno2")
    public ResultVo<String> testWithAnno2(){

        for (int i = 0; i < 1; i++) {
            new Thread(()->{
                dbClusterLockDemoService2.testWithAnno2();
            }).start();
        }
        return ResultVo.valueOfSuccess("");
    }

    /**
     * 验证同时使用多把锁场景
     * @return
     */
    @GetMapping("/testWithMutilLock")
    public ResultVo<String> testWithMutilLock(){

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                dbClusterLockDemoService2.testWithMutilLock();
            }).start();
        }
        return ResultVo.valueOfSuccess("");
    }


    /**
     * 验证锁是否可重入
     * @return
     */
    @GetMapping("/testReentrantLock")
    public ResultVo<String> testReentrantLock(){

        dbClusterLockDemoService3.testReentrantLock();
        return ResultVo.valueOfSuccess("");
    }

    /**
     * 验证锁是否可重入
     * @return
     */
    @GetMapping("/testReentrantLock2")
    public ResultVo<String> testReentrantLock2(){

        dbClusterLockDemoService3.testReentrantLock2();
        return ResultVo.valueOfSuccess("");
    }

    /**
     * 验证锁是否可重入--多线程
     * @return
     */
    @GetMapping("/testReentrantLock3")
    public ResultVo<String> testReentrantLock3(){

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                dbClusterLockDemoService3.testReentrantLock2();
            }).start();
        }
        return ResultVo.valueOfSuccess("");
    }

}
