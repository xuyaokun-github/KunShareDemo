package cn.com.kun.springframework.core.ioc.parentchild;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

//加不加abstract有区别吗？
//对于spring来说，没啥区别，只要没用@Component注解，加不加abstract都不会创建bean
//但用了@Component注解的前提下，加abstract则不会创建Bean,不加则会创建Bean
/*
    @Component 和 abstract 同时用会怎样？
    最终spring不会创建这个bean.所以其他地方要是注入NewBasePlayer就会报错。因为根本不存在这个bean.
    但是在这种情况下，在这个基类中通过@Autowired引入其他服务层，却是允许的。
    例如下面的Service1，通过@Autowired能正常注入，在调用时不会报空指针

 */
/*
    @Component 不要@Component行不行呢？其实也是可以的。最好是不要加。
    即使没有加@Component和@Service,基类中的依赖@Autowired也能正常注入。
    为什么呢？这就要看源码了。
    因为spring在初始化子类bean的时候会判断有这个属性，若属性上有@Autowired，就会去注入。
 */

//总结：只要用了abstract, Spring只会把这个类当成模板，并不会真正去创建bean.
//（难道只有这个区别吗？基本就是了）
public abstract class ParentchildBeanDemoBasePlayer {

    protected String parentName = "NewBasePlayer-name";

    private Map<String, String> map = new HashMap<>();//非static属性，也就是成员属性（多个子类不共享，每个子类都有单独的一份）

    private static Map<String, String> staticMap = new HashMap<>();//静态属性（多个子类bean能共享）

    //能正常注入
    @Autowired
    private ParentchildBeanDemoService1 parentchildBeanDemoService1;

    //中间省略98个

    @Autowired
    private ParentchildBeanDemoService100 parentchildBeanDemoService100;

    /**
     * 无论有没有abstract，这个方法都会触发。有多少个子类bean,就会被触发多少次
     * 所以要注意一些不能重复执行的逻辑，不要放在这里，假如要放在这里，就要做好判断，避免重复操作导致bug
     */
    @PostConstruct
    private void init() {

        System.out.println("进入了com.bgi.phoenix.base.foo.service.right.NewBasePlayer.init");

    }

    public void method(){
        System.out.println("NewBasePlayer showing..........." + this.parentName);
    }

    public void method2(){
        parentchildBeanDemoService1.doService();
    }

    public void method3(){
        parentchildBeanDemoService100.doService();
    }


    public void put(String key, String value){
        this.map.put(key, value);
    }

    public void getMap(){
        System.out.println(JSON.toJSONString(map));
    }

    public void putStatic(String key, String value){
        this.staticMap.put(key, value);
    }

    public void getMapStatic(){
        System.out.println(JSON.toJSONString(staticMap));
    }
}
