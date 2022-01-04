package cn.com.kun.springframework.core.jackson.lanyang;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * 自定义序列化
 *
 * author:xuyaokun_kzx
 * date:2021/12/16
 * desc:
*/
public class CustomStatusSerialize extends JsonSerializer<String> implements ContextualSerializer {

    public CustomStatusSerialize() {}

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        switch (value) {
            case "0":
            {
                jsonGenerator.writeString("完成");
                break;
            }
            case "1":
            {
                jsonGenerator.writeString("成功");
                break;
            }
            case "2":
            {
                jsonGenerator.writeString("停止");
                break;
            }
            case "3":
            {
                jsonGenerator.writeString("失败");
                break;
            }
        }
    }

    @Override
    public JsonSerializer< ? > createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        // 为空直接跳过
        if (beanProperty != null) {
            // 非 String 类直接跳过
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                StatusWrapped statusWrapped = beanProperty.getAnnotation(StatusWrapped.class);
                if (statusWrapped == null) {
                    statusWrapped = beanProperty.getContextAnnotation(StatusWrapped.class);
                }
                if (statusWrapped != null) {
                    return new CustomStatusSerialize();
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(beanProperty);
    }


}