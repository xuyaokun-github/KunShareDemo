package cn.com.kun.springframework.core.resolvableType;

import cn.com.kun.common.vo.people.People;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class ParameterizedBean {
    List<String> list1;//这种就是ParameterizedType
    List list2;
    Map<String,Long> map1;//这种就是ParameterizedType
    Map map2;
    Map.Entry<Long,Short> map3;//这种就是ParameterizedType
    VaccineService<People> aService;//这种就是ParameterizedType

    public static void main(String[] args) {
//        Field[] fields = ParameterizedBean.class.getDeclaredFields();
//        for(Field f:fields){
//            //是否是ParameterizedType
//            System.out.println(f.getName()+":"+(f.getGenericType() instanceof ParameterizedType));
//        }

//        Field[] fields = ParameterizedBean.class.getDeclaredFields();
//        for(Field f:fields){
//            if(f.getGenericType() instanceof ParameterizedType){
//                ParameterizedType pType =(ParameterizedType) f.getGenericType();
//                System.out.println("变量："+pType.getTypeName()+"     ");
//                Type[] types =pType.getActualTypeArguments();
//                for(Type t:types){
//                    System.out.println("类型："+t.getTypeName());
//                }
//            }
//    }

//        Field[] fields =  ParameterizedBean.class.getDeclaredFields();
//        for(Field f:fields){
//            if(f.getGenericType() instanceof ParameterizedType){
//                ParameterizedType pType = (ParameterizedType) f.getGenericType();
//                System.out.println("变量："+f.getName());
//                System.out.println("RawType："+pType.getRawType().getTypeName());
//            }
//        }

        Field[] fields =  ParameterizedBean.class.getDeclaredFields();
        for(Field f:fields){
            if(f.getGenericType() instanceof ParameterizedType){
                ParameterizedType pType = (ParameterizedType) f.getGenericType();
                System.out.println("变量："+f.getName());
                Type t = pType.getOwnerType();
                if(t == null){
                    System.out.println("OwnerType:Null     ");
                }else{
                    System.out.println("OwnerType："+t.getTypeName());
                }
            }
        }

    }
}
