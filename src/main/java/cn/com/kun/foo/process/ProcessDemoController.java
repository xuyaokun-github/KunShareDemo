package cn.com.kun.foo.process;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.utils.ProcessUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 执行命令检索服务器文件内容
 * Created by xuyaokun On 2020/11/22 14:15
 * @desc:
 */
@RequestMapping("/process")
@RestController
public class ProcessDemoController {

    public final static Logger logger = LoggerFactory.getLogger(ProcessDemoController.class);

    //限流器
    RateLimiter rateLimiter = RateLimiter.create(100, 20, TimeUnit.HOURS);

    /**
     *
     * @param dirName 待检索文件的目录名
     * @param filename 待检索文件文件名（可简单模糊匹配）
     * @param keyword 待检索内容（可简单模糊匹配）
     * @return 返回文件总行数、待检索内容该行内容
     */
    @RequestMapping("/test")
    public JSONObject test(@RequestParam String dirName, @RequestParam String filename, @RequestParam String keyword){

        if (!rateLimiter.tryAcquire()){
            throw new RuntimeException("被限流");
        }

        JSONObject jsonObject = new JSONObject();

        //1.获取最新生成的文件名和总行数
        Map resultMap = run(String.format("ls -lrt %s | grep %s", dirName, filename));
        String result = (String) resultMap.get("result");
        //切割所有行
        String[] array = result.split("\\n");
        if (array == null || array.length == 0){
            //命令返回数据为空不做处理
            logger.info("命令返回数据为空不做处理");
        }
        String lastLine = array[array.length-1];
        logger.info("最后一行：" + lastLine);
        String targetFileName = null;//目标文件名
        if (StringUtils.isNotEmpty(lastLine)){
            //
            logger.info("最后一行不为空，继续处理解析文件名");
            String[] array2 = lastLine.split(" ");
            //全路径名
            targetFileName = dirName + File.separator + array2[array2.length -1];
        }

        //
        logger.info("开始获取目标文件的行数");
        resultMap = run(String.format("cat %s | wc -l", targetFileName));
        result = (String) resultMap.get("result");
        jsonObject.put("lineCount", result);//总行数

        //2.获取检索内容
        logger.info("开始检索目标文件内容");
        resultMap = run(String.format("cat %s | grep %s", targetFileName, keyword));
        result = (String) resultMap.get("result");
        jsonObject.put("content", result);//兼容内容
        jsonObject.put("success", "true");//

        return jsonObject;
    }

    /**
     * 传入命令拿到命令输出
     * @param command
     * @return
     */
    private Map run(String command){
        String[] commandArray = { "/bin/sh", "-c", command};
        ResultVo resultVo = ProcessUtil.execCmd(commandArray);
        if (!resultVo.isSuccess()){
            logger.error(JSONObject.toJSONString(resultVo));
            throw new RuntimeException("命令执行失败");
        }
        Map resultMap = (Map) resultVo.getValue();
        return resultMap;
    }

}
