package cn.com.kun.common.config.mysql;

import cn.com.kun.common.utils.AESUtils;
import com.alibaba.druid.util.DruidPasswordCallback;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

/**
 * 数据库密码解密回调
 * author:xuyaokun_kzx
 * date:2021/5/17
 * desc:
*/
public class DBPasswordCallback extends DruidPasswordCallback {

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        String pwd = properties.getProperty("password");
        if (StringUtils.isNoneBlank(pwd)) {
            try {
                //这里的password是将jdbc.properties配置得到的密码进行解密之后的值
                //所以这里的代码是将密码进行解密
                //TODO 将pwd进行解密;
                String password = decrypt(pwd);
                setPassword(password.toCharArray());
            } catch (Exception e) {
                setPassword(pwd.toCharArray());
            }
        }
    }

    private String decrypt(String src) throws Exception {
        //解密
        return AESUtils.decrypt(src, AESUtils.DEFAULT_KEY);
    }
}
