package cn.com.kun.springframework.core.resolvableType;

import cn.com.kun.common.vo.people.Dog;
import cn.com.kun.common.vo.people.People;
import org.springframework.core.ResolvableType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TestResolvableType {

    public static void main(String[] args) {

        //为人打疫苗
        People people = new People();
        VaccineService<People> aService = new VaccineService<>(people);
        aService.injectVaccine(people);
        //为狗打疫苗
        Dog dog = new Dog();
        //在构建aService对象时，已经指定了泛型的具体类型，假如传错类型，编译器会直接提示
//        aService.injectVaccine(dog);
        VaccineService<Dog> bService = new VaccineService<>();
        bService.injectVaccine(dog);

        /*
           这样的编程，有一个缺点，每为一类生物打疫苗都需要new一个专门的服务层
           但有一个好处就是代码可以复用
         */

        /**
         * 示例1-获取PeopleVaccineService类所使用的泛型参数
         */
        PeopleVaccineService peopleVaccineService = new PeopleVaccineService();
        //首先获取peopleVaccineService的父类对应的class，然后解析成ResolvableType
        ResolvableType resolvableType =
        //因为可能会实现多个接口，所以这里必须要指定序号
        resolvableType = ResolvableType.forType(peopleVaccineService.getClass().getGenericInterfaces()[0]);
        /*
            getGeneric是获取泛型参数对应的ResolvableType，序号是泛型参数的序号，从0开始
            调用resolve()方法是拿到其对应的class对象
         */
        Class class1 = resolvableType.getGeneric(0).resolve();
        System.out.println("class1:" + class1.getTypeName());//cn.com.kun.common.vo.people.People

        /**
         * 示例2
         */
        Type type = peopleVaccineService.getClass().getGenericInterfaces()[0];
        if (type instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] types = parameterizedType.getActualTypeArguments();
            for (Type type1 : types){
                //注意，这里遍历的Type已经是每个泛型参数对应的Type了，它的class对象是java.lang.Class(这个对象是没意义的)
                Class class11 = type1.getClass();
                //要想拿到具体的泛型参数对应的class对象，要强制转换
                Class class12 = (Class) type1;
                System.out.println("class11:" + class11.getName());//cn.com.kun.common.vo.people.People
                System.out.println("class12:" + class12.getName());//cn.com.kun.common.vo.people.People
                System.out.println("getTypeName:" + type1.getTypeName());
            }
        }
    }
}
