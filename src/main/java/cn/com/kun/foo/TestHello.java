package cn.com.kun.foo;

import cn.com.kun.common.utils.JacksonUtils;

import java.math.BigDecimal;

public class TestHello {

    private static final String aaa = "aaa";

   public static void main(String[] args) {

       String sourceStr = "kunghsu";
       String str = JacksonUtils.toJSONString(sourceStr);
       System.out.println(str);
   }

    private static long getPercent(long count, long total) {
        if(total==0){
            return 0;
        }
        BigDecimal currentCount = new BigDecimal(count);
        BigDecimal totalCount = new BigDecimal(total);
        BigDecimal divide = currentCount.divide(totalCount,2, BigDecimal.ROUND_HALF_UP);
        return divide.multiply(new BigDecimal(100)).longValue();
    }


}
