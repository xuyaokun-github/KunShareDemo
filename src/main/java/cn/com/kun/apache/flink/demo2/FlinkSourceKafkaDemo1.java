package cn.com.kun.apache.flink.demo2;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.internals.KeyedSerializationSchemaWrapper;

import java.util.Properties;

/**
 * 验证基于流处理消费kafka
 * 收到一个topic的内容，写到另一个topic中
 */
public class FlinkSourceKafkaDemo1 {

    public static void main(String[] args) {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        System.out.println("测试kafka连接器");
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "kunsharedemo");

        FlinkKafkaConsumer<String> myConsumer = new FlinkKafkaConsumer<String>("flink-demo-topic-1", new SimpleStringSchema(), properties);
        myConsumer.setStartFromEarliest();     // 尽可能从最早的记录开始
//        myConsumer.setStartFromLatest();       // 从最新的记录开始
//        myConsumer.setStartFromTimestamp(...); // 从指定的时间开始（毫秒）
//        myConsumer.setStartFromGroupOffsets(); // 默认的方法

        //添加输入源
        DataStream<String> stream = env.addSource(myConsumer);

        //输出到控制台
        stream.print();

        //生产者配置
        Properties produceProperties = new Properties();
        produceProperties.setProperty("bootstrap.servers", "localhost:9092");

        FlinkKafkaProducer<String> myProducer = new FlinkKafkaProducer<String>(
                "flink-demo-topic-3",                  // 目标 topic
                new KeyedSerializationSchemaWrapper<String>(new SimpleStringSchema()), // 序列化 schema
                produceProperties,                  // producer 配置
                FlinkKafkaProducer.Semantic.EXACTLY_ONCE); //容错

        //添加输出源
        stream.addSink(myProducer);

        try {
            System.out.println("调用execute方法");
            env.execute();
            System.out.println("执行结束");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
