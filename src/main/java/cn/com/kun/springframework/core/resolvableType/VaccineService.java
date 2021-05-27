package cn.com.kun.springframework.core.resolvableType;

import cn.com.kun.common.vo.people.Dog;
import cn.com.kun.common.vo.people.People;

/**
 * 这是一个疫苗服务层
 * 作用就是为不同的人打疫苗
 *
 * 什么时候才需要用到泛型？
 * 一个逻辑要应对多种不同对象的情形，就可以用泛型来抽象不同的对象
 * 例如List的泛型，就是如此，它是一个集合，集合可以存各种各样的对象
 * 可以不用泛型吗？当然可以Object,从编程上当然也可以
 * author:xuyaokun_kzx
 * date:2021/5/27
 * desc:
*/
public class VaccineService<T> {

    /**
     * 这个就是泛型参数
     * 疫苗可以为很多生物进行注射，例如人，阿猫阿狗
     */
    private T data;

    public VaccineService(){
    }

    public VaccineService(T data){
        this.data = data;
    }

    /**
     * 注射疫苗
     */
    public void injectVaccine(T data){
        //
        System.out.println(data.getClass().getTypeName());
        if (data instanceof People){
            //假如是人，要打两针
            System.out.println("正在为人类注射，需要打两针");
        }
        if (data instanceof Dog){
            System.out.println("正在为狗注射，一针足矣");
        }
    }

}
