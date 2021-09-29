package cn.com.kun.apache.flink.flinkkafka.test.watermark;

import cn.com.kun.apache.flink.FlinkUtils;
import cn.com.kun.apache.flink.flinkkafka.TopicConstants;
import cn.com.kun.apache.flink.flinkkafka.config.FlinkKafkaConfig;
import cn.com.kun.apache.flink.flinkkafka.model.FlinkTopicMsg;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 验证同时消费多个kafka的topic
 * 假如两个topic不是在同一个kafka服务端，需要建不同的KafkaConsumer，即需要两个流
 *
 * eventTime的滚动窗口
 * 验证例子--eventTime + BoundedOutOfOrdernessTimestampExtractor的使用
 * 好久之前的一个窗口里面有数据，但是没有等到下一个可以触发这个窗口执行的数据，
 * 所以之前的这个窗口会一直停留。这种窗口，有些场景明显不适合，数据迟迟得不到处理！！
 *
 * AssignerWithPunctuatedWatermarks使用例子
 *
 * author:xuyaokun_kzx
 * date:2021/9/28
 * desc:
 */
public class MoreKafkaSourceDemoByAssignerWithPunctuatedWatermarks {

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //同时监听多个主题
        //创建消费者
        FlinkKafkaConsumer<String> flinkKafkaConsumer = FlinkKafkaConfig.getFlinkKafkaConsumer(TopicConstants.TOPIC_FLINK_DEMO_1);
        FlinkKafkaConsumer<String> flinkKafkaConsumer2 = FlinkKafkaConfig.getFlinkKafkaConsumer(TopicConstants.TOPIC_FLINK_DEMO_2);

        //添加输入源
        DataStream<String> topic1Stream = env.addSource(flinkKafkaConsumer);
        DataStream<String> topic2Stream = env.addSource(flinkKafkaConsumer2);
        //将流进行合并
        DataStream<String> stream = topic1Stream.union(topic2Stream);

        //数据的中间处理操作
        SingleOutputStreamOperator newStream = stream
                .assignTimestampsAndWatermarks(new MyPunctuatedAssigner())
                .keyBy(new MyKeySelector()) //键控策略
                .window(TumblingEventTimeWindows.of(Time.seconds(10))) //设置窗口大小为10秒，window后接apply操作
                .apply(new WindowFunction<String, Object, Object, TimeWindow>() {

                    @Override
                    public void apply(Object o, TimeWindow window, Iterable<String> input, Collector<Object> out) throws Exception {
                        AtomicReference<String> msgId = new AtomicReference<>();
                        AtomicReference<String> statusCode = new AtomicReference<>();
                        AtomicInteger count = new AtomicInteger();
                        input.forEach(inputStr -> {
                            count.getAndIncrement();
                            FlinkTopicMsg flinkTopicMsg = JacksonUtils.toJavaObject(inputStr, FlinkTopicMsg.class);
                            if (flinkTopicMsg != null){
                                if (StringUtils.isNotEmpty(flinkTopicMsg.getMsgId())){
                                    msgId.set(flinkTopicMsg.getMsgId());
                                }
                                if (StringUtils.isEmpty(flinkTopicMsg.getMsgId()) && StringUtils.isNotEmpty(flinkTopicMsg.getStatusCode())){
                                    statusCode.set(flinkTopicMsg.getStatusCode());
                                }
                            }
                        });

                        System.out.println("触发apply方法，o:" + o + " 收到记录个数：" + count.get()
                                + "   input:" + JacksonUtils.toJSONString(input)
                                + " 窗口起始：" + FlinkUtils.showWindowInfo(window));

                        String newRes;
                        if (msgId.get() != null && statusCode.get() != null){
                            //说明数据到齐
                            Map<String, String> resultMap = new HashMap<>();
                            resultMap.put("msgId", msgId.get());
                            resultMap.put("statusCode", statusCode.get());
                            newRes = JacksonUtils.toJSONString(resultMap);
                        }else {
                            newRes = JacksonUtils.toJSONString(ResultVo.valueOfError("数据未到齐"));
                        }
                        out.collect(newRes);
                    }
                })
                .process(new ProcessFunction<Object, Object>() {

                    @Override
                    public void processElement(Object value, Context ctx, Collector<Object> out) throws Exception {
                        System.out.println("触发processElement方法,value:" + value);
                        out.collect(value);
                    }
                });

        //输出到控制台,这里输出的是最后一次调用collect设置的结果
        newStream.print();

        //创建生产者
//        FlinkKafkaProducer flinkKafkaProducer = FlinkKafkaConfig.getFlinkKafkaProducer(TopicConstants.TOPIC_FLINK_DEMO_3);
        //添加输出源
//        newStream.addSink(flinkKafkaProducer);

        System.out.println("调用execute方法");
        //注意：因为flink是懒加载的，所以必须调用execute方法，上面的代码才会执行
        env.execute();
        System.out.println("执行结束");
    }

    /**
     * 键控策略
     */
    public static class MyKeySelector implements KeySelector<String, Object> {

        @Override
        public Object getKey(String value) throws Exception {

            //先解析出这个属于哪个topic
            //两个topic肯定有一个字段可以关联起来(例子中即tradeId)
            FlinkTopicMsg flinkTopicMsg = JacksonUtils.toJavaObject(value, FlinkTopicMsg.class);
            return flinkTopicMsg.getTradeId();
        }
    }


    public static class MyPunctuatedAssigner implements AssignerWithPunctuatedWatermarks<String> {

        @Override
        public long extractTimestamp(String element, long previousElementTimestamp) {
            //从报文解析为对象，然后得到报文中的时间戳
            FlinkTopicMsg flinkTopicMsg = JacksonUtils.toJavaObject(element, FlinkTopicMsg.class);
            //获取时间戳
            return flinkTopicMsg.getCreateTime().getTime();
        }

        @Override
        public Watermark checkAndGetNextWatermark(String lastElement, long extractedTimestamp) {

            //从报文解析为对象，然后得到报文中的时间戳
            FlinkTopicMsg flinkTopicMsg = JacksonUtils.toJavaObject(lastElement, FlinkTopicMsg.class);
            if (flinkTopicMsg.getMsgId() == null){
                return new Watermark(flinkTopicMsg.getCreateTime().getTime());
            }
            return null;
        }
    }

}
