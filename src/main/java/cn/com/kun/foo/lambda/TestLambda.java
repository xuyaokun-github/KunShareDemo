package cn.com.kun.foo.lambda;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TestLambda {

    public static void main(String[] args) {

        //使用这个函数式接口，把一个lambda表达式，也就是一块代码赋值给这个变量
        MyLambdaInterface myVariable = ()->{
            System.out.println("hello");
        };

        DunkPlayer aGorden = new DunkPlayer("阿龙戈登");
        DunkPlayer jones = new DunkPlayer("琼斯");

        List<DunkPlayer> list = new ArrayList<>();
        list.add(aGorden);
        list.add(jones);

        Predicate<DunkPlayer> predicate = p -> p.getName().equals("阿龙戈登");
        Consumer<DunkPlayer> consumer = p ->p.dunk();
        list.forEach(play -> {
            if(predicate.test(play)){
                System.out.println("下面请冠军扣将开始为武汉朋友进行表演");
                consumer.accept(play);
            }
        });


        //
        Supplier<DunkPlayer> supplier = () -> {
            DunkPlayer me = new DunkPlayer("xyk");
            return me;
        };
        DunkPlayer xyk = supplier.get();
        System.out.println(JSONObject.toJSONString(xyk));

    }


}
