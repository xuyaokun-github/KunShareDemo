package cn.com.kun.component.redo;

import cn.com.kun.component.redo.callback.RedoTaskCallback;
import cn.com.kun.component.redo.common.RedoTaskCallbackNotFoundException;
import cn.com.kun.component.redo.common.RedoTaskNotFoundException;
import cn.com.kun.component.redo.bean.vo.RedoTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 补偿任务注册工厂
 * author:xuyaokun_kzx
 * date:2021/10/27
 * desc:
*/
public class RedoTaskRegisterFactory {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedoTaskRegisterFactory.class);

    /**
     * 重试任务集合
     */
    private static Map<String, RedoTaskCallback> redoTaskCallbacks = new HashMap();

    private static Map<String, RedoTask> redoTasks = new HashMap();

    public static void registerRedoTask(RedoTask redoTask){
        redoTasks.put(redoTask.getRedoTaskId(), redoTask);
    }

    public static RedoTask findRedoTask(String redoTaskId) throws RedoTaskNotFoundException {
        RedoTask redoTask = redoTasks.get(redoTaskId);
        if (redoTask == null){
            LOGGER.warn("redoTask:[] can not find redoTask");
            throw new RedoTaskCallbackNotFoundException();
        }
        return redoTask;
    }

    /**
     * 注册重试任务回调逻辑
     * @param redoTaskId
     * @param redoTaskCallback
     */
    public static void registerRedoTaskCallback(String redoTaskId, RedoTaskCallback redoTaskCallback){
        redoTaskCallbacks.put(redoTaskId, redoTaskCallback);
    }

    public static RedoTaskCallback findRedoTaskCallback(String redoTaskId) throws RedoTaskCallbackNotFoundException {
        RedoTaskCallback redoTaskCallback = redoTaskCallbacks.get(redoTaskId);
        if (redoTaskCallback == null){
            LOGGER.warn("redoTaskId:[] can not find RedoTaskCallback");
            throw new RedoTaskCallbackNotFoundException();
        }
        return redoTaskCallback;
    }

}
