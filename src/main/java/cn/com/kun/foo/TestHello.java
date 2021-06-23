package cn.com.kun.foo;

import java.math.BigDecimal;

public class TestHello {

    private static final String aaa = "aaa";

   public static void main(String[] args) {
       for (int i = 0; i < 10; i++) {
           long l = Long.parseLong("ABCDEF", 16);
           System.out.println(l);
       }
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
