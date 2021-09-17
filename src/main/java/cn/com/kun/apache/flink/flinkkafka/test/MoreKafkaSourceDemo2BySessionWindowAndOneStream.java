package cn.com.kun.apache.flink.flinkkafka.test;

import cn.com.kun.apache.flink.flinkkafka.FlinkKafkaConfig;
import cn.com.kun.apache.flink.flinkkafka.model.FlinkTopicMsg;
import cn.com.kun.apache.flink.flinkkafka.TopicConstants;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证同时消费多个kafka的topic
 * 假如两个topic是在同一个kafka服务端里，用一个流就可以实现需求
 *
 * 会话窗口,能保证数据得到正确的处理。
 *
 * author:xuyaokun_kzx
 * date:2021/9/15
 * desc:
 */
public class MoreKafkaSourceDemo2BySessionWindowAndOneStream {

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //设置time
//        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        // 其他
//         env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //同时监听多个主题
        List<String> topics = new ArrayList<>();
        topics.add(TopicConstants.TOPIC_FLINK_DEMO_1);
        topics.add(TopicConstants.TOPIC_FLINK_DEMO_2);

        //创建消费者
        FlinkKafkaConsumer<String> flinkKafkaConsumer = FlinkKafkaConfig.getFlinkKafkaConsumer(topics);
        //添加输入源
        DataStream<String> stream = env.addSource(flinkKafkaConsumer);

        //数据的中间处理操作
        SingleOutputStreamOperator newStream = stream.keyBy(new KeySelector<String, Object>() {
            @Override
            public Object getKey(String value) throws Exception {

                //先解析出这个属于哪个topic
                //两个topic肯定有一个字段可以关联起来(例子中即tradeId)
                FlinkTopicMsg flinkTopicMsg = JacksonUtils.toJavaObject(value, FlinkTopicMsg.class);
                return flinkTopicMsg.getTradeId();
            }
        })
                .window(ProcessingTimeSessionWindows.withGap(Time.seconds(10))) //会话窗口
                .apply(new WindowFunction<String, Object, Object, TimeWindow>() {

                    // 因为上面用了keyBy，每一个key就会有一个时间窗口，即每一个msgId就对应一个窗口
                    @Override
                    public void apply(Object o, TimeWindow timeWindow, Iterable<String> iterable, Collector<Object> collector) throws Exception {
                        System.out.println(String.format("触发apply方法,o:%s iterable:%s", o, JacksonUtils.toJSONString(iterable)));
                        //这里还可以拿到该时间窗口的起始和终止时间
                        timeWindow.getEnd();
                        timeWindow.getStart();
                        List<String> sourceStrList = new ArrayList<>();
                        iterable.forEach(str -> {
                            sourceStrList.add(str);
                        });
                        //通过iterable的个数判断,是否数据都已回齐,假如没齐,就返回具体错误描述
                        String res = "";
                        if (sourceStrList.size() < 2) {
                            Map<String, Object> map1 = JacksonUtils.toMap(sourceStrList.get(0));
                            if (map1.get("msgId") == null) {
                                //假如只收到第二个topic的数据,直接丢弃,不做处理
                                return;
                            }
                            //说明数据没回齐,返回错误
                            res = JacksonUtils.toJSONString(ResultVo.valueOfError("数据存在延迟"));
                        } else {
                            //通过报文结构区分开是哪个topic(这样做很挫)
                            Map<String, Object> map1 = JacksonUtils.toMap(sourceStrList.get(0));
                            Map<String, Object> map2 = JacksonUtils.toMap(sourceStrList.get(1));
                            Map<String, Object> topic1Map;
                            Map<String, Object> topic2Map;
                            if (map1.get("msgId") != null) {
                                //说明map1是 topic1的数据,
                                topic1Map = map1;
                                topic2Map = map2;
                            } else {
                                //说明map1是 topic2的数据
                                topic1Map = map2;
                                topic2Map = map1;
                            }
                            Map<String, String> resultMap = new HashMap<>();
                            resultMap.put("msgId", (String) topic1Map.get("msgId"));
                            resultMap.put("statusCode", (String) topic2Map.get("statusCode"));
                            res = JacksonUtils.toJSONString(resultMap);
                        }
                        collector.collect(res);
                    }
                });

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

        System.out.println("调用execute方法");
        //注意：因为flink是懒加载的，所以必须调用execute方法，上面的代码才会执行
        env.execute();
        System.out.println("执行结束");
    }

}
