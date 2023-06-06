package cn.com.kun.service.mybatis.sensitiveDemo;

import java.lang.annotation.*;
/**
 * 该注解有两种使用方式
 * ①：配合@SensitiveData加在类中的字段上
 * ②：直接在Mapper中的方法参数上使用
 *
 * 注意：用了该注解的属性，在保存DB前会自动加密，在查询时会自动解密
 * 假如希望保存时加密，查询时不解密，要改代码，新增一个解密注解。这种需求比较少
 * 因此一个注解同时表示做加解密是能满足大部分场景的。
 *
 **/
@Documented
@Inherited
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptDecryptField {

}