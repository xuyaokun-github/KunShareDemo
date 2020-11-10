package cn.com.kun.apache.shardingsphere.algorithm;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * 自定义分表策略：根据身份证进行分表
 * Created by xuyaokun On 2020/11/6 23:44
 * @desc:
 */
public class MyPreciseShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    private final static String TBL_PREFIX = "tbl_student_";//物理表名前缀

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {

        //shardingValue这个就是我们配置指定的分表列的值
        System.out.println("shardingValue:" + shardingValue);
        //简单地实现一个分表策略：因为身份证存在X特殊字符，所以先进行替换，全是数字之后再做数学运算得到余数
        String idCard = shardingValue.getValue().replace("X", "");
        //得到余数，再拼接表名前缀，返回的就是真实的物理表名
        Long result = Long.parseLong(idCard);
        return TBL_PREFIX + (result % 4);
    }

}