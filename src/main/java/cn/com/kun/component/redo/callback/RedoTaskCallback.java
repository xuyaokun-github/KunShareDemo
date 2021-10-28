package cn.com.kun.component.redo.callback;

import cn.com.kun.component.redo.bean.vo.RedoReqParam;
import cn.com.kun.component.redo.bean.vo.RedoResult;

/**
 * 重试逻辑接口
 * author:xuyaokun_kzx
 * date:2021/10/27
 * desc:
*/
@FunctionalInterface
public interface RedoTaskCallback {

    /**
     * 执行重试
     * @param redoReqParam
     * @return
     */
    RedoResult redo(RedoReqParam redoReqParam);

}
