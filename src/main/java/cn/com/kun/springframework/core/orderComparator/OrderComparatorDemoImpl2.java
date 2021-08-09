package cn.com.kun.springframework.core.orderComparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order
@Component
public class OrderComparatorDemoImpl2 implements OrderComparatorDemo{

    public final static Logger LOGGER = LoggerFactory.getLogger(OrderComparatorDemoImpl2.class);

    @Override
    public void show() {

        LOGGER.info("我是OrderComparatorDemoImpl2");
    }
}
