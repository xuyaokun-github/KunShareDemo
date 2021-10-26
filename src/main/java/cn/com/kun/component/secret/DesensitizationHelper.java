package cn.com.kun.component.secret;

import cn.com.kun.common.annotation.DesensitizationField;
import cn.com.kun.common.utils.CustomClassUtils;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 脱敏帮助类
 * 脱敏实现了支持嵌套对象的脱敏，对于加解密实现也是类似的
 *
 * author:xuyaokun_kzx
 * date:2021/5/24
 * desc:
*/
@Component
public class DesensitizationHelper {

    private final static Logger logger = LoggerFactory.getLogger(DesensitizationHelper.class);

    /**
     * 脱敏
     * @param sourceBody
     * @return
     */
    public Object desensitization(Object sourceBody) throws Exception {

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
            DesensitizationField desensitizationField = field.getAnnotation(DesensitizationField.class);
            if (desensitizationField != null){
                //假如存在注解，则进行解密处理
                //先获取属性旧值
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cls);
                Method getMethod = pd.getReadMethod();
                //拿到属性旧值
                Object oldValue = getMethod.invoke(targetBody);
                if (oldValue != null && oldValue instanceof String){
                    //只做字符串的处理
                    String expression = desensitizationField.expression();
                    String replace = desensitizationField.replace();
                    Method setMethod = pd.getWriteMethod();
                    String newValue = ((String)oldValue).replaceAll(expression, replace);
                    setMethod.invoke(targetBody, newValue);
                }else {
                    /*
                        假如是自定义对象，可以继续处理！支持嵌套对象
                        假如属性定义成Map，则不做处理，提示修改成自定义java类型
                     */
                    //TODO 支持嵌套对象，例如list
                    Class propertyClass = oldValue.getClass();
                    if (!CustomClassUtils.isPrimitiveOrWrapper(propertyClass) && !CustomClassUtils.isMap(oldValue)
                        && !CustomClassUtils.isArray(propertyClass)){
                        if (CustomClassUtils.isCollection(propertyClass)){
                            //假如是集合类型，例如List
                            desensitizationForList(oldValue);
                        }else {
                            //递归（递归不需要返回值，只有第一次进这个方法时需要返回值）
                            desensitization(oldValue);
                        }
                    }
                }
            }
        }
        String newResultBodyStr = JacksonUtils.toJSONString(sourceBody);
        logger.info("脱敏后重新返回的结果，{}", newResultBodyStr);
        return sourceBody;
    }

    /**
     * 针对集合类型脱敏
     * @param sourceBody
     * @return
     * @throws Exception
     */
    public Object desensitizationForList(Object sourceBody) throws Exception {
        if (sourceBody instanceof Collection){
            //确定是集合类型
            for (Object obj : (Collection)sourceBody){
                //遍历每一个元素
                desensitization(obj);
            }
        }
        return sourceBody;
    }
}
