package cn.com.kun.apache.flink.flinkkafka.test;

import cn.com.kun.apache.flink.flinkkafka.FlinkKafkaConfig;
import cn.com.kun.apache.flink.flinkkafka.model.FlinkTopicMsg;
import cn.com.kun.apache.flink.flinkkafka.TopicConstants;
import cn.com.kun.common.utils.JacksonUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;

import java.util.Date;

/**
 * 验证基于流处理消费kafka
 * 收到一个topic的内容，写到另一个topic中
 * flatMap和collect的使用
 */
public class FlinkSourceKafkaDemoFlatMapTest {

    public static void main(String[] args) {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //创建消费者
        FlinkKafkaConsumer flinkKafkaConsumer = FlinkKafkaConfig.getFlinkKafkaConsumer(TopicConstants.TOPIC_FLINK_DEMO_1);
        //添加输入源
        DataStream<String> stream = env.addSource(flinkKafkaConsumer);

        //数据的中间处理操作
        /*
            这里调用了flatMap这类的方法之后，当前流就不再是最新的流了
            假如继续用stream操作，拿到的将是旧结果。
            所以为了拿到最新的结果，必须用最新的引用来操作
         */
        stream = stream.flatMap(new MyFlatMapFunction());

        //输出到控制台
        stream.print();

        //创建生产者
        FlinkKafkaProducer flinkKafkaProducer = FlinkKafkaConfig.getFlinkKafkaProducer(TopicConstants.TOPIC_FLINK_DEMO_3);
        //添加输出源
        stream.addSink(flinkKafkaProducer);

        try {
            System.out.println("调用execute方法");
            env.execute();
            System.out.println("执行结束");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * flatMap使用例子
     */
    public static class MyFlatMapFunction implements FlatMapFunction<String, String> {

        @Override
        public void flatMap(String sentence, Collector<String> out) throws Exception {

            System.out.println("进入flatMap函数，参数：" + sentence);
            FlinkTopicMsg flinkTopicMsg = JacksonUtils.toJavaObject(sentence, FlinkTopicMsg.class);
            //假如返回码小于5，就做二次处理
            if (5 > Integer.parseInt(flinkTopicMsg.getStatusCode())) {
                //只要状态码为0的记录
                System.out.println("找到StatusCode小于5的记录,进行二次处理");
                flinkTopicMsg.setUpdateTime(new Date());
                String newRes = JacksonUtils.toJSONString(flinkTopicMsg);
                //调用collect有什么效果？
                // 调了collect才会将结果放到输出流，sink收到的数据就是collect的数据。
                //假如不调，输出流就没有数据
                out.collect(newRes);
            }
        }

    }

}
