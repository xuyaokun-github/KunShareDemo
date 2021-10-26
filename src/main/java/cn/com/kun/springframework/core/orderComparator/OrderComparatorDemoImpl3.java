package cn.com.kun.springframework.core.orderComparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(100)
@Component
public class OrderComparatorDemoImpl3 implements OrderComparatorDemo{

    private final static Logger LOGGER = LoggerFactory.getLogger(OrderComparatorDemoImpl3.class);

    @Override
    public void show() {

        LOGGER.info("我是OrderComparatorDemoImpl3");
    }
}
