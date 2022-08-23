package cn.com.kun.framework.apache.commonpool2;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * author:xuyaokun_kzx
 * date:2022/8/23
 * desc:
*/
public class StringPoolTest {

    private static final Logger LOG = LoggerFactory.getLogger(StringPoolTest.class);

    public static void main(String[] args) {

        StringPoolObjectFactory fac = new StringPoolObjectFactory();
        //定义池配置
        GenericObjectPoolConfig<String> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(2);
        config.setMinIdle(1);
        config.setMaxWaitMillis(3000);
        //创建池，构造函数的入参是对象工厂，和 池配置
        StringPool pool = new StringPool(fac, config);
        for (int i = 0; i < 3; i++) {
            String s = "";
            try {
                //获取资源
                s = pool.borrowObject();
                System.out.println("str:{}" + s);
//                LOG.info("str:{}", s);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //释放资源
                if (!s.equals("")) {
                    pool.returnObject(s);
                }
            }
        }
    }

}
