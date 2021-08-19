package cn.com.kun.springframework.core.ioc.parentchild;

import cn.com.kun.common.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 父子bean demo
 *
 * author:xuyaokun_kzx
 * date:2021/8/13
 * desc:
*/
@RestController
@RequestMapping("/parentchildBeanDemo")
public class ParentchildBeanDemoController {

    public final static Logger LOGGER = LoggerFactory.getLogger(ParentchildBeanDemoController.class);

    @RequestMapping("/hello")
    public String testString(){
        return "kunghsu";
    }

    /**
     * ParentchildBeanDemoBasePlayer这个是基类，被多个bean子类继承
     * 在容器里，会生成多个ParentchildBeanDemoBasePlayer的bean,因为子类也是一个ParentchildBeanDemoBasePlayer类型
     * 在这里直接根据类型注入，会注入失败，提示存在多个同类型的bean
     */
    //    @Resource
//    @Autowired
    ParentchildBeanDemoBasePlayer parentchildBeanDemoBasePlayer;

    @Autowired
    ParentchildBeanDemoCPlayer parentchildBeanDemoCPlayer;

    @Autowired
    ParentchildBeanDemoDPlayer parentchildBeanDemoDPlayer;

    @RequestMapping(value = "/test2")
    public void test2(HttpServletRequest request, HttpServletResponse response) throws Exception {


        //会发现有2个基类的NewBasePlayer（因为基类本身它自己不会创建，A和B分别也创建了一个，所以只有2个）
        //这种情况，NewBasePlayer没有使用@Component注解，所以spring没有把它识别为bean,spring只会把它认为是一个模板
        //不管有没有使用@Component注解，NewBasePlayer加不加abstract都不会创建bean

        //
        Map<String, ParentchildBeanDemoBasePlayer> map =  SpringContextUtil.getContext().getBeansOfType(ParentchildBeanDemoBasePlayer.class);
        System.out.println(map.size());

        //C类player只有一个
        Map<String, ParentchildBeanDemoCPlayer> map2 =  SpringContextUtil.getContext().getBeansOfType(ParentchildBeanDemoCPlayer.class);
        System.out.println(map2.size());

        //调用子类bean cPlayer的方法
        parentchildBeanDemoCPlayer.say();

        parentchildBeanDemoDPlayer.say();

        //注意，虽然继承的是同一个基类，但是它们的属性能共享吗？
        //假如是普通属性，不能共享，因为spring虽然不会创建bean,但是仍会创建对象。不同的子类bean,它们的基类实例也是不同的。
        parentchildBeanDemoCPlayer.put("aa", "123");
        parentchildBeanDemoCPlayer.getMap();//输出{"aa":"123"}

        parentchildBeanDemoDPlayer.getMap();//输出{}

        //假如是静态属性呢，能不能共享呢？
        //静态属性，可以共享，这和spring没关系，这是Java的本身特性，同一个类的多个实例，它们的静态属性只存在一份，每个实例共享的
        parentchildBeanDemoCPlayer.putStatic("bb", "456");
        parentchildBeanDemoCPlayer.getMapStatic();//输出{"bb":"456"}

        parentchildBeanDemoDPlayer.getMapStatic();//输出{"bb":"456"}

        parentchildBeanDemoDPlayer.putStatic("cc", "789");
        parentchildBeanDemoDPlayer.getMapStatic();//输出{"bb":"456","cc":"789"}

        parentchildBeanDemoCPlayer.getMapStatic();//输出{"bb":"456","cc":"789"}


    }

}
