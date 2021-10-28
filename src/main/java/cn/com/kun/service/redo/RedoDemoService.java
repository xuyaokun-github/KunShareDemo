package cn.com.kun.service.redo;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.component.redo.RedoManager;
import cn.com.kun.component.redo.RedoTaskRegisterFactory;
import cn.com.kun.component.redo.bean.vo.RedoReqParam;
import cn.com.kun.component.redo.bean.vo.RedoResult;
import cn.com.kun.component.redo.bean.vo.RedoTask;
import cn.com.kun.component.redo.callback.RedoTaskCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class RedoDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedoDemoService.class);

    String redoTaskIdOne = "redoBiz1";

    @Autowired
    RedoManager redoManager;

    @PostConstruct
    public void init(){
        //注册
        RedoTask redoTask = RedoTask.newBuilder(redoTaskIdOne).maxAttempts(3).build();
        RedoTaskRegisterFactory.registerRedoTask(redoTask);
        RedoTaskRegisterFactory.registerRedoTaskCallback(redoTask.getRedoTaskId(), new RedoTaskCallback() {
            @Override
            public RedoResult redo(RedoReqParam redoReqParam) {
                Map<String, Object> map = redoReqParam.getParams();
                String name = (String) map.get("name");
                String desc = (String) map.get("desc");
                doService(name, desc);
                return RedoResult.BIZ_ERROR;
//                return RedoResult.SUCCESS;
            }
        });

    }

    public void test1(){

        RedoReqParam redoReqParam = new RedoReqParam();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "xyk");
        map.put("desc", "kunghsu");
        redoReqParam.setParams(map);

        //调用组件提供出来的工具类方法
        redoManager.addRedoTask(redoTaskIdOne, JacksonUtils.toJSONString(redoReqParam));
    }

    /**
     * 这是一个业务方法
     */
    public void doService(String name, String desc){

        LOGGER.debug("执行具体的业务逻辑");
        LOGGER.info("{},{}", name, desc);
    }


}
