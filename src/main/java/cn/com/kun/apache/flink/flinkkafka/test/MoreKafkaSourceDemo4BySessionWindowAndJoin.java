package cn.com.kun.apache.flink.flinkkafka.test;

import cn.com.kun.apache.flink.flinkkafka.FlinkKafkaConfig;
import cn.com.kun.apache.flink.flinkkafka.TopicConstants;
import cn.com.kun.apache.flink.flinkkafka.model.FlinkTopicDealResultMsg;
import cn.com.kun.apache.flink.flinkkafka.model.FlinkTopicMsg;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;

/**
 * 验证同时消费多个kafka的topic
 * 假如两个topic不是在同一个kafka服务端，需要建不同的KafkaConsumer，即需要两个流
 *
 * 使用会话窗口，用join连接流
 *
 * author:xuyaokun_kzx
 * date:2021/9/15
 * desc:
 */
public class MoreKafkaSourceDemo4BySessionWindowAndJoin {

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //设置time
//        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        // 其他
//         env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //同时监听多个主题
        //创建消费者
        FlinkKafkaConsumer<String> flinkKafkaConsumer = FlinkKafkaConfig.getFlinkKafkaConsumer(TopicConstants.TOPIC_FLINK_DEMO_1);
        FlinkKafkaConsumer<String> flinkKafkaConsumer2 = FlinkKafkaConfig.getFlinkKafkaConsumer(TopicConstants.TOPIC_FLINK_DEMO_2);

        //添加输入源
        DataStream<String> topic1Stream = env.addSource(flinkKafkaConsumer);
        DataStream<String> topic2Stream = env.addSource(flinkKafkaConsumer2);
        //将流进行合并
        DataStream<String> stream = topic1Stream.join(topic2Stream)
                .where(new MyKeySelector())
                .equalTo(new MyKeySelector()) //两个函数用的key可以是不一样的，假如字段真的完全不同名的话
                .window(ProcessingTimeSessionWindows.withGap(Time.seconds(10)))
                .apply(new JoinFunction<String, String, FlinkTopicDealResultMsg>() {

                    /**
                     * 这种情况假如数据没到齐，不会触发任何处理！！因为where和equal没匹配成功，所以不会有后续
                     *
                     * @param first
                     * @param second
                     * @return
                     * @throws Exception
                     */
                    @Override
                    public FlinkTopicDealResultMsg join(String first, String second) throws Exception {

                        //整个之后，可以自由决定返回什么
                        //
                        FlinkTopicMsg flinkTopic1Msg = JacksonUtils.toJavaObject(first, FlinkTopicMsg.class);
                        FlinkTopicMsg flinkTopic2Msg = JacksonUtils.toJavaObject(first, FlinkTopicMsg.class);
                        FlinkTopicDealResultMsg flinkTopicResMsg = new FlinkTopicDealResultMsg();
                        if (flinkTopic1Msg.getTradeId().equals(flinkTopic2Msg.getTradeId())){
                            flinkTopicResMsg.setMsgId(flinkTopic1Msg.getMsgId());
                            flinkTopicResMsg.setStatusCode(flinkTopic2Msg.getStatusCode());
                        }
                        return flinkTopicResMsg;
                    }

                })
                .flatMap(new FlatMapFunction<FlinkTopicDealResultMsg, String>() {

                    @Override
                    public void flatMap(FlinkTopicDealResultMsg value, Collector<String> out) throws Exception {

                        if (StringUtils.isNotEmpty(value.getMsgId())){
                            //说明得到正确的处理
                            String newRes = JacksonUtils.toJSONString(value);
                            //放到输出流
                            out.collect(newRes);
                        }else {
                            out.collect(JacksonUtils.toJSONString(ResultVo.valueOfError("处理异常")));
                        }

                    }
                });

        //输出到控制台,这里输出的是最后一次调用collect设置的结果
        stream.print();

        //创建生产者
        FlinkKafkaProducer flinkKafkaProducer = FlinkKafkaConfig.getFlinkKafkaProducer(TopicConstants.TOPIC_FLINK_DEMO_3);
        //添加输出源
        stream.addSink(flinkKafkaProducer);

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


}
