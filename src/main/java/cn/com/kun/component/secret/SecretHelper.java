package cn.com.kun.component.secret;

import cn.com.kun.common.annotation.SecretField;
import cn.com.kun.common.utils.AESUtils;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 加解密帮助类
 *
 * author:xuyaokun_kzx
 * date:2021/5/24
 * desc:
*/
@Component
public class SecretHelper {

    private final static Logger logger = LoggerFactory.getLogger(SecretHelper.class);

    /**
     * 解密
     * @param targetObj
     * @param appId
     * @return
     */
    public String decode(Object targetObj, String appId) throws Exception {

        /**
         * 这里拿到的targetObj是一个普通的java对象
         * 只有字符串需要做加解密，并且支持嵌套字段
         * 思路：通过反射来为这个对象重新赋值
         */

        /**
         * 怎么知道哪些属性要解密？
         * 还是通过注解，给需要解密的属性上注解
         */
        Class cls = targetObj.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields){
            //遍历所有属性
            SecretField secretField = field.getAnnotation(SecretField.class);
            if (secretField != null && secretField.decode()){
                //假如存在注解，则进行解密处理
                //先获取属性旧值
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cls);
                Method getMethod = pd.getReadMethod();
                //拿到旧值
                Object oldValue = getMethod.invoke(targetObj);
                if (oldValue != null && oldValue instanceof String){
                    //只做字符串的处理
                    Method setMethod = pd.getWriteMethod();
                    String newValue = AESUtils.decrypt((String)oldValue, appId);
                    setMethod.invoke(targetObj, newValue);
                }else {
                    //假如是自定义对象，可以继续处理！支持嵌套对象
                    //TODO 支持嵌套对象，例如list
                }
            }
        }
        String newResultBodyStr = JacksonUtils.toJSONString(targetObj);
        logger.info("解密后重新返回的bodyStr，{}", newResultBodyStr);
        return newResultBodyStr;
    }

    /**
     * 加密
     * @param sourceBody
     * @param appId
     * @return
     * @throws Exception
     */
    public Object encode(Object sourceBody, String appId) throws Exception {

        Object targetBody = null;
        if (sourceBody instanceof ResultVo){
            targetBody = ((ResultVo)sourceBody).getValue();
        }else {
            targetBody = sourceBody;
        }

        Class cls = targetBody.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields){
            //遍历所有属性
            SecretField secretField = field.getAnnotation(SecretField.class);
            if (secretField != null && secretField.encode()){
                //假如存在注解，则进行解密处理
                //先获取属性旧值
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cls);
                Method getMethod = pd.getReadMethod();
                //拿到旧值
                Object oldValue = getMethod.invoke(targetBody);
                if (oldValue != null && oldValue instanceof String){
                    //只做字符串的处理
                    Method setMethod = pd.getWriteMethod();
                    String newValue = AESUtils.encrypt((String)oldValue, appId);
                    setMethod.invoke(targetBody, newValue);
                }else {
                    //假如是自定义对象，可以继续处理！支持嵌套对象

                }
            }
        }
        return sourceBody;
    }

}
