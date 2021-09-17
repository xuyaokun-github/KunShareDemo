package cn.com.kun.apache.flink.flinkkafka.test;

import cn.com.kun.apache.flink.flinkkafka.FlinkKafkaConfig;
import cn.com.kun.apache.flink.flinkkafka.model.FlinkTopicMsg;
import cn.com.kun.apache.flink.flinkkafka.TopicConstants;
import cn.com.kun.common.utils.JacksonUtils;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * 验证基于流处理消费kafka
 * 收到一个topic的内容，写到另一个topic中
 * OutputTag的使用例子
 */
public class FlinkSourceKafkaDemoOutputTagTest {

    public static void main(String[] args) {

        System.out.println("测试kafka连接器FlinkSourceKafkaDemoOutputTagTest");
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

        final OutputTag<String> outputTag = new OutputTag<String>("side-output") {};

        //数据的中间处理操作
        SingleOutputStreamOperator newStream = stream.keyBy(new KeySelector<String, Object>() {
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
                        collector.collect("apply result");
                    }
                }).process(new ProcessFunction<Object, Object>() {

                    @Override
                    public void processElement(Object o, Context context, Collector<Object> collector) throws Exception {
                        //这里收到的将是上面的collect方法传入的内容：“apply result”
                        System.out.println(String.format("触发processElement方法,接收到的o:%s", o));
                        //然后这里再次调用了collect，sink那边将收到的结果是 “processElement result”
                        collector.collect("processElement result");
                        //旁路输出
                        context.output(outputTag, "outputTag result");
                    }
                });

        //最原始的输入流
        stream.print();

        //输出到控制台,这里输出的是最后一次调用collect设置的结果
        newStream.print();

        //创建生产者
        FlinkKafkaProducer flinkKafkaProducer = FlinkKafkaConfig.getFlinkKafkaProducer(TopicConstants.TOPIC_FLINK_DEMO_3);
        //添加输出源
        /*
            假如拿到输入源之后没有做任何处理（没有调用collect方法），
            假如上面的apply函数和process函数里都没有调用collect方法，那输出源将不会有任何输出，包括print也不会有输出
            必须要process函数里调用了collect方法之后，输出流才会有数据
            因为调用了process函数，所以相当于产生了一个新的流，
            下面就是针对新的流调了addSink
         */
        newStream.addSink(flinkKafkaProducer);

        /*
            outputTag 该怎么用呢?
            必须将它转成另一个流
         */
        DataStream outputTagStream = newStream.getSideOutput(outputTag);
        outputTagStream.print();

        try {
            System.out.println("调用execute方法");
            //注意：因为flink是懒加载的，所以必须调用execute方法，上面的代码才会执行
            env.execute();
            System.out.println("执行结束");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
