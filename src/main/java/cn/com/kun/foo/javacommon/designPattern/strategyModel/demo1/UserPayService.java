package cn.com.kun.foo.javacommon.designPattern.strategyModel.demo1;

import java.math.BigDecimal;

public interface UserPayService {

    BigDecimal quote(BigDecimal orderPrice);
}
