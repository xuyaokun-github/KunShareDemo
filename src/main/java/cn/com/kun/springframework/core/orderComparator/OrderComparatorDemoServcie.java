package cn.com.kun.springframework.core.orderComparator;

import cn.com.kun.common.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderComparatorDemoServcie {

    public final static Logger LOGGER = LoggerFactory.getLogger(OrderComparatorDemoServcie.class);

    List<OrderComparatorDemo> demoList = new ArrayList<>();

    @Autowired
    SpringContextUtil springContextUtil;

    @PostConstruct
    public void init(){
        demoList.add(SpringContextUtil.getBean("orderComparatorDemoImpl1"));
        demoList.add(SpringContextUtil.getBean("orderComparatorDemoImpl2"));
        demoList.add(SpringContextUtil.getBean("orderComparatorDemoImpl3"));
    }

    public void method(){

        List<OrderComparatorDemo> newList = new ArrayList<>();
        newList.addAll(demoList);
        /*
            排序前：输出顺序是1、2、3
            排序后：3,1,2 因为3的优先级数字最小，所以优先级最高
         */
//        AnnotationAwareOrderComparator.sort(newList);
        for (OrderComparatorDemo orderComparatorDemo : newList) {
            orderComparatorDemo.show();
        }
    }


}
