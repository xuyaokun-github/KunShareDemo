package cn.com.kun.apache.flink.flinkkafka.test;

import cn.com.kun.apache.flink.flinkkafka.model.FlinkTopicMsg;
import cn.com.kun.common.utils.JacksonUtils;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.internals.KeyedSerializationSchemaWrapper;

import java.util.Map;
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

        //数据的中间处理操作
        //得到一个新的输入流
        DataStream<String> newStream = stream.map(new MapFunction<String, String>() {
            @Override
            public String map(String value) {

                Map<String, Object> map = JacksonUtils.toMap(value);
                map.put("processFlag", true);//进过加工的数据，加一个字段属性
                return JacksonUtils.toJSONString(map);
            }
        });

        //做一个过滤操作
        newStream = newStream.filter(new FilterFunction<String>(){

            @Override
            public boolean filter(String s) throws Exception {

                //返回true,表示需要该记录，返回false表示丢弃
                FlinkTopicMsg flinkTopicMsg = JacksonUtils.toJavaObject(s, FlinkTopicMsg.class);
                if ("0".equals(flinkTopicMsg.getStatusCode())) {
                    //只要状态码为0的记录
                    System.out.println("找到StatusCode为0的记录：" + s);
                    return true;
                }
                return false;
            }
        });

        //输出到控制台
        newStream.print();

        //生产者配置
        Properties produceProperties = new Properties();
        produceProperties.setProperty("bootstrap.servers", "localhost:9092");

        FlinkKafkaProducer<String> myProducer = new FlinkKafkaProducer<String>(
                "flink-demo-topic-3",                  // 目标 topic
                new KeyedSerializationSchemaWrapper<String>(new SimpleStringSchema()), // 序列化 schema
                produceProperties,                  // producer 配置
                FlinkKafkaProducer.Semantic.EXACTLY_ONCE); //容错

        //添加输出源
        newStream.addSink(myProducer);

        try {
            System.out.println("调用execute方法");
            env.execute();
            System.out.println("执行结束");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
