package cn.com.kun.controller;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.PageParam;

import java.util.Map;

/**
 * 控制层基类
 * author:xuyaokun_kzx
 * date:2021/8/12
 * desc:
*/
public class BaseController {

    /**
     * 构造PageParam对象
     *
     * @param reqVO
     * @param clazz
     * @param <T>
     * @return
     */
    protected <T> PageParam<T> buildPageParam(Map<String, String> reqVO, Class<T> clazz) {

        return PageParam.build(reqVO, clazz);
    }

    /**
     * 将map参数转成具体java实体类对象
     *
     * @param reqVO
     * @param clazz
     * @param <T>
     * @return
     */
    protected <T> T buildRequestObj(Map<String, String> reqVO, Class<T> clazz){
        T res = (T) JacksonUtils.toJavaObject(reqVO, clazz);
        return res;
    }

}
