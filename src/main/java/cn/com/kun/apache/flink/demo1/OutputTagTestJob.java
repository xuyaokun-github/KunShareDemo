package cn.com.kun.apache.flink.demo1;

import cn.com.kun.apache.flink.FlinkDemoJob;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.Arrays;
import java.util.List;

/**
 * 采用服务端运行模式
 *
 * author:xuyaokun_kzx
 * date:2021/9/10
 * desc:
*/
public class OutputTagTestJob implements FlinkDemoJob {

    public static void main(String[] args) {
        new OutputTagTestJob().run(args);
    }

    @Override
    public void run(String[] args) {

        // 采用本地模式
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

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

        //这个实例，可以放到常量类 id就表示某一类业务的旁路输出，不同业务不要用相同的ID,会出错
        final OutputTag<Person> outputTag = new OutputTag<Person>("side-output") {};

        //方式2:fromCollection
        List<Person> personList = Arrays.asList(new Person("Fred", 35),
                new Person("Wilma", 35),
                new Person("Pebbles", 2));
        DataStream<Person> flintstones = env.fromCollection(personList);

        //执行中间操作
        DataStream<Person> flintstones2 = flintstones.flatMap(new RichFlatMapFunction<Person, Person>() {


            @Override
            public void flatMap(Person value, Collector<Person> out) throws Exception {
                out.collect(value);
            }
        });

        SingleOutputStreamOperator<Person> flintstones3 = flintstones2.process(new ProcessFunction<Person, Person>() {

            @Override
            public void processElement(Person person, Context context, Collector<Person> collector) throws Exception {

                if (person.getName().startsWith("W")){
                    //执行成功的，放正常输出流
                    collector.collect(person);
                }else {
                    //执行失败的，放旁路输出流
                    //旁路输出的类型不需要和源类型一致，可自由定义类型，例子中还是输出person类型
                    context.output(outputTag, person);
                }
            }
        });

//        flintstones3.print();

        //SingleOutputStreamOperator才能调用getSideOutput方法
        //getSideOutput方法不是DataStream接口的方法
        DataStream outputTagStream = flintstones3.getSideOutput(outputTag);
        outputTagStream.print();

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
