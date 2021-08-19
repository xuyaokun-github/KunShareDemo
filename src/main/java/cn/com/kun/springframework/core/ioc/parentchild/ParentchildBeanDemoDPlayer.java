package cn.com.kun.springframework.core.ioc.parentchild;

import org.springframework.stereotype.Component;

@Component
public class ParentchildBeanDemoDPlayer extends ParentchildBeanDemoBasePlayer {

    public void say(){
        System.out.println("DPlayer saying...........");

        //调用基类的方法
        this.method3();

    }
}
