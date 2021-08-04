package cn.com.kun.springframework.core.resolvableType;

import org.springframework.core.ResolvableType;

public class ResolvableTypeDemo1 {

    public static void main(String[] args) {

        SubFooObj subFooObj = new SubFooObj();
        ResolvableType resolvableType = resolveDefaultEventType(subFooObj);
        System.out.println(resolvableType);
    }

    /**
     * 类似这样的代码，在spring中有很多
     * 例如spring的启动过程监听器的获取过程，就会将事件对象解析成ResolvableType
     * @param obj
     * @return
     */
    private static ResolvableType resolveDefaultEventType(Object obj) {
        return ResolvableType.forInstance(obj);
    }


}
