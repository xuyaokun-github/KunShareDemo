package cn.com.kun.springframework.core.resolvableType;

import org.springframework.core.ResolvableType;
import org.springframework.util.ReflectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 展示ResolvableType的用法
 * author:xuyaokun_kzx
 * date:2021/5/27
 * desc:
*/
public class SpringResolvableTypeGenericClassDemo {

    private List<String> listString;
    private List<List<String>> listLists;
    private Map<String, Long> maps;
    private VaccineService<String> parent;

    public Map<String, Long> getMaps() {
        return maps;
    }

    /**
     *  private VaccineService<String> parent;
     *  泛型参数为：class java.lang.String
     */
    public static void  doTestFindParent(){
        ResolvableType parentResolvableType = ResolvableType.forField(ReflectionUtils.findField(
                SpringResolvableTypeGenericClassDemo.class,"parent"));
        System.out.println("parent type:"+parentResolvableType.getType());

        //获取第0个位置的参数泛型
        Class<?> resolve = parentResolvableType.getGeneric(0).resolve();
        System.out.println("泛型参数为："+resolve);
    }

    /**
     * private List<String> listString;
     * listString type:java.util.List<java.lang.String>
     * 泛型参数为：class java.lang.String
     */
    public static void  doTestFindListStr(){
        ResolvableType listStringResolvableType = ResolvableType.forField(ReflectionUtils.findField(
                SpringResolvableTypeGenericClassDemo.class,"listString"));
        System.out.println("listString type:"+listStringResolvableType.getType());

        //获取第0个位置的参数泛型
        Class<?> resolve = listStringResolvableType.getGeneric(0).resolve();
        System.out.println("泛型参数为："+resolve);

    }

    /**
     * private List<List<String>> listLists;
     * listLists type:java.util.List<java.util.List<java.lang.String>>
     * 泛型参数为：interface java.util.List
     * 泛型参数为：class java.lang.String
     * 泛型参数为：class java.lang.String
     * begin 遍历
     * 泛型参数为：java.util.List<java.lang.String>
     * end 遍历
     */
    public static void  doTestFindlistLists(){
        ResolvableType listListsResolvableType = ResolvableType.forField(ReflectionUtils.findField(
                SpringResolvableTypeGenericClassDemo.class,"listLists"));
        System.out.println("listLists type:"+listListsResolvableType.getType());

        //获取第0个位置的参数泛型
        Class<?> resolve = listListsResolvableType.getGeneric(0).resolve();
        System.out.println("泛型参数为："+resolve);

        //region 这两种实现方式一样的 泛型参数为：class java.lang.String
        resolve = listListsResolvableType.getGeneric(0).getGeneric(0).resolve();
        System.out.println("泛型参数为："+resolve);

        resolve = listListsResolvableType.getGeneric(0,0).resolve();
        System.out.println("泛型参数为："+resolve);
        //endregion

        ResolvableType[] resolvableTypes = listListsResolvableType.getGenerics();
        System.out.println("begin 遍历");
        for(ResolvableType resolvableType: resolvableTypes){
            resolve = resolvableType.resolve();
            System.out.println("泛型参数为："+resolve);
        }
        System.out.println("end 遍历");

    }

    /**
     *  private Map<String, Long> maps;
     */
    public static void  doTestFindMaps(){
        ResolvableType mapsResolvableType = ResolvableType.forField(ReflectionUtils.findField(
                SpringResolvableTypeGenericClassDemo.class,"maps"));
        System.out.println("maps type:"+mapsResolvableType.getType());

        System.out.println("begin 遍历");
        ResolvableType[] resolvableTypes = mapsResolvableType.getGenerics();
        Class<?> resolve =null;
        for(ResolvableType resolvableType: resolvableTypes){
            resolve = resolvableType.resolve();
            System.out.println("泛型参数为："+resolve);
        }
        System.out.println("end 遍历");

    }

    /**
     * Map<String, Long>
     */
    public static void doTestFindReturn(){
        // Spring的提供工具类,用于方法的返回值的泛型信息
        ResolvableType resolvableType = ResolvableType.forMethodReturnType(ReflectionUtils.findMethod(SpringResolvableTypeGenericClassDemo.class, "getMaps"));
        System.out.println("maps type:"+resolvableType.getType());
        System.out.println("begin 遍历");
        ResolvableType[] resolvableTypes = resolvableType.getGenerics();
        Class<?> resolve =null;
        for(ResolvableType resolvableTypeItem: resolvableTypes){
            resolve = resolvableTypeItem.resolve();
            System.out.println("泛型参数为："+resolve);
        }
        System.out.println("end 遍历");

    }

    /**
     * 总结一句话就是使用起来非常的简单方便，更多超级复杂的可以参考spring 源码中的测试用例：ResolvableTypeTests
     * 其实这些的使用都是在Java的基础上进行使用的哦！
     Type是Java 编程语言中所有类型的公共高级接口（官方解释），也就是Java中所有类型的“爹”；其中，“所有类型”的描述尤为值得关注。它并不是我们平常工作中经常使用的 int、String、List、Map等数据类型，
     而是从Java语言角度来说，对基本类型、引用类型向上的抽象；
     Type体系中类型的包括：原始类型(Class)、参数化类型(ParameterizedType)、数组类型(GenericArrayType)、类型变量(TypeVariable)、基本类型(Class);
     原始类型，不仅仅包含我们平常所指的类，还包括枚举、数组、注解等；
     参数化类型，就是我们平常所用到的泛型List、Map；
     数组类型，并不是我们工作中所使用的数组String[] 、byte[]，而是带有泛型的数组，即T[] ；
     基本类型，也就是我们所说的java的基本类型，即int,float,double等
     * @param args
     */
    public static void main(String[] args) {
        doTestFindParent();
//        doTestFindListStr();
//        doTestFindlistLists();
//        doTestFindMaps();
//        doTestFindReturn();
    }
    /**
     言归正传，下面讲解ResolvableType。ResolvableType为所有的java类型提供了统一的数据结构以及API，
     换句话说，一个ResolvableType对象就对应着一种java类型。我们可以通过ResolvableType对象获取类型携带的信息（举例如下）：
     1.getSuperType()：获取直接父类型
     2.getInterfaces()：获取接口类型
     3.getGeneric(int...)：获取类型携带的泛型类型
     4.resolve()：Type对象到Class对象的转换

     另外，ResolvableType的构造方法全部为私有的，我们不能直接new，只能使用其提供的静态方法进行类型获取：
     1.forField(Field)：获取指定字段的类型
     2.forMethodParameter(Method, int)：获取指定方法的指定形参的类型
     3.forMethodReturnType(Method)：获取指定方法的返回值的类型
     4.forClass(Class)：直接封装指定的类型
     5.ResolvableType.forInstance 获取指定的实例的泛型信息
     */
}
