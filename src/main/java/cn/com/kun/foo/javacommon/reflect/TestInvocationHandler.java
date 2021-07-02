package cn.com.kun.foo.javacommon.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class TestInvocationHandler {

    public static void main(String[] args) {

        //要代理的真实对象(被代理对象)
        FooPeople people = new FooTeacher();
        //代理对象的调用处理程序，我们将要代理的真实对象传入代理对象的调用处理的构造函数中，最终代理对象的调用处理程序会调用真实对象的方法
        InvocationHandler handler = new WorkHandler(people);

        /**
         * 通过Proxy类的newProxyInstance方法创建代理对象，我们来看下方法中的参数
         * 第一个参数：people.getClass().getClassLoader()，使用handler对象的classloader对象来加载我们的代理对象
         * 第二个参数：people.getClass().getInterfaces()，这里为代理类提供的接口是真实对象实现的接口，这样代理对象就能像真实对象一样调用接口中的所有方法
         * 第三个参数：handler，我们将代理对象关联到上面的InvocationHandler对象上
         */
        FooPeople proxy = (FooPeople) Proxy.newProxyInstance(handler.getClass().getClassLoader(), people.getClass().getInterfaces(), handler);
        //用代理对象调方法（InvocationHandler并不是代理对象，proxy才是代理对象，people是被代理对象）
        System.out.println(proxy.work());

        System.out.println("Client#manin "+proxy.getClass());

        /*
        上面的handler是代理处理程序，代理对象执行的逻辑就在代理处理程序里
        如何获取代理处理程序？通过静态方法
         */
        InvocationHandler h = Proxy.getInvocationHandler(proxy);
        System.out.println(h.getClass());

    }

}
