package cn.com.kun.apache.flink.flinkkafka.test;

import cn.com.kun.apache.flink.flinkkafka.FlinkKafkaConfig;
import cn.com.kun.apache.flink.flinkkafka.model.FlinkTopicMsg;
import cn.com.kun.apache.flink.flinkkafka.TopicConstants;
import cn.com.kun.common.utils.JacksonUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;

import java.util.Date;

/**
 * 验证基于流处理消费kafka
 * 收到一个topic的内容，写到另一个topic中
 * TimeWindow的使用例子
 *
 */
public class FlinkSourceKafkaDemoTimeTest {

    public static void main(String[] args) {

        System.out.println("测试kafka连接器FlinkSourceKafkaDemoTimeTest");
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置time
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

        // 其他
        // env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);
        // env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //创建消费者
        FlinkKafkaConsumer<String> flinkKafkaConsumer = FlinkKafkaConfig.getFlinkKafkaConsumer(TopicConstants.TOPIC_FLINK_DEMO_1);
        //添加输入源
        DataStream<String> stream = env.addSource(flinkKafkaConsumer);

        stream.keyBy(new KeySelector<String, Object>() {
            @Override
            public Object getKey(String value) throws Exception {
                FlinkTopicMsg flinkTopicMsg = JacksonUtils.toJavaObject(value, FlinkTopicMsg.class);
                return flinkTopicMsg.getMsgId();
            }
        })
                .timeWindow(Time.seconds(60)) //设置一个60秒的窗口
                .apply(new WindowFunction<String, Object, Object, TimeWindow>() {

                    //每到60秒，就会执行一次apply方法
                    //入参iterable即这60秒内某个msgId相关的所有数据，
                    // 因为上面用了keyBy，每一个key就会有一个时间窗口，即每一个msgId就对应一个窗口
                    @Override
                    public void apply(Object o, TimeWindow timeWindow, Iterable<String> iterable, Collector<Object> collector) throws Exception {
                        System.out.println(String.format("触发apply方法,o:%s iterable:%s", o, JacksonUtils.toJSONString(iterable)));
                        //这里还可以拿到该时间窗口的起始和终止时间
                        timeWindow.getEnd();
                        timeWindow.getStart();
                    }
                });

        //数据的中间处理操作

        //输出到控制台
        stream.print();

        //创建生产者
        FlinkKafkaProducer flinkKafkaProducer = FlinkKafkaConfig.getFlinkKafkaProducer(TopicConstants.TOPIC_FLINK_DEMO_3);
        //添加输出源
        /*
            假如拿到输入源之后没有做任何处理，直接就通过sink发走，那就相当于做了一次转发而已
            输出的内容将会输入的内容一样
         */
        stream.addSink(flinkKafkaProducer);

        try {
            System.out.println("调用execute方法");
            //注意：因为flink是懒加载的，所以必须调用execute方法，上面的代码才会执行
            env.execute();
            //因为是无界流，所以这一行执行不到，上面会一直运行
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
                System.out.println("找到StatusCode为0的记录,进行二次处理");
                flinkTopicMsg.setUpdateTime(new Date());
                String newRes = JacksonUtils.toJSONString(flinkTopicMsg);
                //调用collect有什么效果？
                out.collect(newRes);
            }
        }

    }

}
