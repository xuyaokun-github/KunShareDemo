package cn.com.kun.springframework.core.orderComparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(300)
@Component
public class OrderComparatorDemoImpl1 implements OrderComparatorDemo{

    public final static Logger LOGGER = LoggerFactory.getLogger(OrderComparatorDemoImpl1.class);

    @Override
    public void show() {

        LOGGER.info("我是OrderComparatorDemoImpl1");
    }
}
