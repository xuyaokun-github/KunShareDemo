package cn.com.kun.apache.flink.demo1;

import cn.com.kun.apache.flink.FlinkDemoJob;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Arrays;
import java.util.List;

/**
 * 全局属性使用例子
 *
 * author:xuyaokun_kzx
 * date:2021/9/10
 * desc:
*/
public class GlobalJobParametersTest implements FlinkDemoJob {

    public static void main(String[] args) {
        new GlobalJobParametersTest().run(args);
    }

    @Override
    public void run(String[] args) {

        // 采用本地模式
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        Configuration conf = new Configuration();
        conf.setString("mykey", "myvalue");
//        ExecutionConfig.GlobalJobParameters globalJobParameters = new ExecutionConfig.GlobalJobParameters();
        env.getConfig().setGlobalJobParameters(conf);


        // 采用服务端运行模式
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();


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
        DataStream<Person> flintstones = env.fromCollection(personList).name("firstSource").uid("firstSource");

        //执行中间操作
        DataStream<Person> flintstones2 = flintstones.flatMap(new RichFlatMapFunction<Person, Person>() {

            //需要用到的全局属性，可以用一个属性来承接
            private String mykey;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                ExecutionConfig.GlobalJobParameters globalParams = getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
                Configuration globConf = (Configuration) globalParams;
                //获取全局属性
                mykey = globConf.getString("mykey", null);
            }

            @Override
            public void flatMap(Person value, Collector<Person> out) throws Exception {

                //修改,在名字前面拼上一个全局属性
                value.setName(value.getName() + "-" + mykey);
                out.collect(value);
            }
        });

        flintstones2.print();

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
