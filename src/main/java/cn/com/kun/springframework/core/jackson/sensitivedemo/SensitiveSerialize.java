package cn.com.kun.springframework.core.jackson.sensitivedemo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

public class SensitiveSerialize extends JsonSerializer<String> implements ContextualSerializer {

    /**
     * 脱敏类型
     */
    private SensitiveEnum type;

    public SensitiveSerialize() {}

    public SensitiveSerialize(final SensitiveEnum type) {
        this.type = type;
    }


    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        switch (this.type) {
            case CHINESE_NAME:
            {
                jsonGenerator.writeString(SensitiveInfoUtils.chineseName(s));
                break;
            }
            case ID_CARD:
            {
                jsonGenerator.writeString(SensitiveInfoUtils.idCardNum(s));
                break;
            }
            case FIXED_PHONE:
            {
                jsonGenerator.writeString(SensitiveInfoUtils.fixedPhone(s));
                break;
            }
            case MOBILE_PHONE:
            {
                jsonGenerator.writeString(SensitiveInfoUtils.mobilePhone(s));
                break;
            }
            case ADDRESS:
            {
                jsonGenerator.writeString(SensitiveInfoUtils.address(s, 4));
                break;
            }
            case EMAIL:
            {
                jsonGenerator.writeString(SensitiveInfoUtils.email(s));
                break;
            }
            case BANK_CARD:
            {
                jsonGenerator.writeString(SensitiveInfoUtils.bankCard(s));
                break;
            }
            case CNAPS_CODE:
            {
                jsonGenerator.writeString(SensitiveInfoUtils.cnapsCode(s));
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
                SensitiveWrapped sensitiveWrapped = beanProperty.getAnnotation(SensitiveWrapped.class);
                if (sensitiveWrapped == null) {
                    sensitiveWrapped = beanProperty.getContextAnnotation(SensitiveWrapped.class);
                }
                if (sensitiveWrapped != null) {
                    // 如果能得到注解，就将注解的 value 传入 SensitiveSerialize
                    return new SensitiveSerialize(sensitiveWrapped.value());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(beanProperty);
    }


}