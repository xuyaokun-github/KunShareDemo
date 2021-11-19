package cn.com.kun.apache.flink.flinkkafka.task;

import cn.com.kun.apache.flink.flinkkafka.config.FlinkKafkaConfig;
import cn.com.kun.apache.flink.flinkkafka.model.FlinkTopicMsg;
import cn.com.kun.apache.flink.flinkkafka.TopicConstants;
import cn.com.kun.common.utils.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证同时消费多个kafka的topic
 * 假如两个topic不是在同一个kafka服务端，需要建不同的KafkaConsumer，即需要两个流
 *
 * 不使用窗口，用mapState
 * 方案也是可行的。
 *
 * author:xuyaokun_kzx
 * date:2021/9/15
 * desc:
 */
public class MoreKafkaSourceDemo4ByMapStateTask {

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
        DataStream<String> stream = topic1Stream.union(topic2Stream);

        //数据的中间处理操作
        SingleOutputStreamOperator newStream = stream
                .keyBy(new MyKeySelector())
                .flatMap(new MyRichFlatMapFunction());

        //输出到控制台,这里输出的是最后一次调用collect设置的结果
        newStream.print();

        //创建生产者
        FlinkKafkaProducer flinkKafkaProducer = FlinkKafkaConfig.getFlinkKafkaProducer(TopicConstants.TOPIC_FLINK_DEMO_3);
        //添加输出源
        newStream.addSink(flinkKafkaProducer);

        System.out.println("调用execute方法");
        //注意：因为flink是懒加载的，所以必须调用execute方法，上面的代码才会执行
        env.execute();
        //因为是无界流，所以正常情况下不会执行到这里
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

    public static class MyRichFlatMapFunction extends RichFlatMapFunction<String, String> {

        private MapState<String, String> mapState;

        //FlatMapFunction接口没有open方法
        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);

            // 设置状态过期配置
            StateTtlConfig ttlConfig = StateTtlConfig
                    /*
                        状态缓存的时间很重要
                        假如时间太小，topic1的数据放入状态中了，但是topic2来得迟了，状态就过期了，所以两个topic就关联不起来
                        假如时间太大，系统需要暂存的内容就较多，增加内存磁盘占用

                        亲测时间到了之后，状态值将获取不到。
                     */
                    .newBuilder(Time.seconds(10))
                    // 设置状态的更新类型
                    .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                    // 已过期还未被清理掉的状态数据不返回给用户
                    .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
                    // 过期对象的清理策略 全量清理
//                    .cleanupFullSnapshot()
                    //增量清理
//                    .cleanupIncrementally(1, true)
//                    .cleanupInBackground()
                    .build();

            MapStateDescriptor descriptor = new MapStateDescriptor("MapDescriptor", String.class, String.class);
            // 状态过期配置与状态绑定
            descriptor.enableTimeToLive(ttlConfig);
            mapState = getRuntimeContext().getMapState(descriptor);
        }

        /**
         * 这种方案，不管延迟多久都能拿到数据
         * @param sentence
         * @param out
         * @throws Exception
         */
        @Override
        public void flatMap(String sentence, Collector<String> out) throws Exception {

//            System.out.println("进入flatMap函数，参数：" + sentence);

            FlinkTopicMsg flinkTopicMsg = JacksonUtils.toJavaObject(sentence, FlinkTopicMsg.class);
            //首先判断，是否已经有msgId了，假如有了，说明第一个topic的数据已经回来了
//            System.out.println("当前mapState： values:" + JacksonUtils.toJSONString(mapState.values())
//                    + " keys:" + JacksonUtils.toJSONString(mapState.keys()));
            //每个key,都会有一个独立的mapState
            if (StringUtils.isNotEmpty(flinkTopicMsg.getMsgId())){
                //假如msgId不为空，说明是第一个topic
                mapState.put("msgId", flinkTopicMsg.getMsgId());
            }else {
                //第二个topic的数据
                //从状态中拿出之前存好的msgId
                String msgId = mapState.get("msgId");
                if (StringUtils.isEmpty(msgId)){
                    //假如为空，说明状态已过期被自动清除了
                    //说明topic2的数据回得太晚了
                    return;
                }
                Map<String, String> resultMap = new HashMap<>();
                resultMap.put("msgId", msgId);
                resultMap.put("statusCode", flinkTopicMsg.getStatusCode());
                String newRes = JacksonUtils.toJSONString(resultMap);
                out.collect(newRes);
            }
        }

    }


}
