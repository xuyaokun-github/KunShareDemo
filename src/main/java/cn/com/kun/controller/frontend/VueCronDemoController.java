package cn.com.kun.controller.frontend;

import cn.com.kun.bean.model.PointCountResVO;
import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.SpringCronUtils;
import cn.com.kun.common.vo.ResultVo;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(value = "http://localhost:8081") //这是正常的
@RequestMapping("/vue-cron-demo")
@RestController
public class VueCronDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(VueCronDemoController.class);

    @RequestMapping("/cron")
    public ResultVo<List<PointCountResVO>> pointCount(@RequestBody Map<String,String> params){

        String cron = params.get("cron");
        LOGGER.info("获取到的表达式：{}", cron);

        LOGGER.info("cron表达式是否合法：{}", CronSequenceGenerator.isValidExpression(cron));

        try {
            CronExpression cronExpression = new CronExpression(cron);
            Date date = cronExpression.getNextValidTimeAfter(new Date());
            LOGGER.info("date：{}", DateUtils.toStr(date, DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss_SSS));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //获取十个下次执行时间点
        List<Date> dateList = SpringCronUtils.calNextPoint(cron, 10);
        List<String> strList = dateList.stream().map(date -> {
            return DateUtils.toStr(date, DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss_SSS);
        }).collect(Collectors.toList());

        return ResultVo.valueOfSuccess(strList);
    }






}
