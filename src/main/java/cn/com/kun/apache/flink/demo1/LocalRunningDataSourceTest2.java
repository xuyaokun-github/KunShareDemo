package cn.com.kun.apache.flink.demo1;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;
import java.util.List;

/**
 * 采用服务端运行模式
 *
 * author:xuyaokun_kzx
 * date:2021/9/10
 * desc:
*/
public class LocalRunningDataSourceTest2 {

    public static void main(String[] args) {

        // 采用本地模式
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        // 采用服务端运行模式
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();


        // 从本地的webUI方式提供createLocalEnvironmentWithWebUI
        // Configuration conf=new Configuration();
        /* conf.set(option, value)*/
        // StreamExecutionEnvironment env =
        // StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);// 采用默认配置

        // 设定数据来源为集合数据
//        //方式1:fromElements
//        DataStream<Person> flintstones = env.fromElements(new Person("Fred", 35),
//                        new Person("Wilma", 35),
//                        new Person("Pebbles", 2));

        //方式2:fromCollection
        List<Person> personList = Arrays.asList(new Person("Fred", 35),
                new Person("Wilma", 35),
                new Person("Pebbles", 2));
        DataStream<Person> flintstones = env.fromCollection(personList);

        //执行过滤操作
        DataStream<Person> adults = flintstones.filter(new FilterFunction<Person>() {

            /**
             * 筛选18岁以上的
             * @param person
             * @return
             * @throws Exception
             */
            @Override
            public boolean filter(Person person) throws Exception {
                return person.age >= 18;
            }
        });

        adults.print();

        System.out.println("开始对数据流设置二次过滤函数式实例");
        DataStream<Person> adults2 = adults.filter(new FilterFunction<Person>() {

            /**
             * 筛选以F开头的
             * @param person
             * @return
             * @throws Exception
             */
            @Override
            public boolean filter(Person person) throws Exception {
                return person.name.startsWith("F");
            }
        });

        adults2.print();
        try {
            /*
                最后开始执行
                参数名表示这次执行实例，标识这次执行过程
             */
            JobExecutionResult result = env.execute("LocalRunningDataSourceTest2" + System.currentTimeMillis());
            //在服务端运行模式下，根本执行不到这一行，因为它被当成一个无界流
            System.out.println("execute执行完毕");
            if(result.isJobExecutionResult()) {
                System.out.println("执行完毕......");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    static class Person {

        private String name;
        private Integer age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
