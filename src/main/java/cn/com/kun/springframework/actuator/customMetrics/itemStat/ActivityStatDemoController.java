package cn.com.kun.springframework.actuator.customMetrics.itemStat;

import cn.com.kun.common.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/customMetricsDemo-itemStat")
@RestController
public class ActivityStatDemoController {

    @Autowired
    ActivityDemoService activityDemoService;

    @Autowired
    ItemStatHelper itemStatHelper;

    @GetMapping("/add")
    public ResultVo add() throws Exception {

        activityDemoService.markRunning("00001");
        for (int i = 0; i < 10; i++) {
            itemStatHelper.increment("00001", 1);
        }
        return ResultVo.valueOfSuccess("OK");
    }

    /**
     * 模拟一个活动，已经完成，已经结束意味着不需要再监控，就不需要再绘制图表
     * @return
     * @throws Exception
     */
    @GetMapping("/markFinished")
    public ResultVo markFinished() throws Exception {

        activityDemoService.markFinished("00001");
        return ResultVo.valueOfSuccess("OK");
    }
}
