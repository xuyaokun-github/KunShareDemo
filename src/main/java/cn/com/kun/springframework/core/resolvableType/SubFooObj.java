package cn.com.kun.springframework.core.resolvableType;

import cn.com.kun.bean.model.people.People;

/**
 * 展示如何获取类头上的具体泛型参数
 * author:xuyaokun_kzx
 * date:2021/5/27
 * desc:
*/
public class SubFooObj extends FooObj<People> {

    public static void main(String[] args) {
        SubFooObj subFooObj = new SubFooObj();
        System.out.println(subFooObj.getType());
    }
}
