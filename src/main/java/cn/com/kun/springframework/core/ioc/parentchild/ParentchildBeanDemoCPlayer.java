package cn.com.kun.springframework.core.ioc.parentchild;

import org.springframework.stereotype.Component;

@Component
public class ParentchildBeanDemoCPlayer extends ParentchildBeanDemoBasePlayer {

    public void say(){
        System.out.println("CPlayer saying...........");

        //调用继承自基类的方法
        this.method2();
    }
}
