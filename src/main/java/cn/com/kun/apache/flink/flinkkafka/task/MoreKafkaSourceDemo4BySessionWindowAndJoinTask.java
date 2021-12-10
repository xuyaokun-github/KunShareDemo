package cn.com.kun.apache.flink.flinkkafka.task;

import cn.com.kun.apache.flink.flinkkafka.TopicConstants;
import cn.com.kun.apache.flink.flinkkafka.config.FlinkKafkaConfig;
import cn.com.kun.apache.flink.flinkkafka.model.FlinkTopicMsg;
import cn.com.kun.apache.flink.flinkkafka.operators.join.MoreKafkaSourceByJoinAndSessionWindowJoinFunction;
import cn.com.kun.apache.flink.flinkkafka.operators.map.MoreKafkaSourceByJoinAndSessionWindowFlatMapFunction;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.utils.PropertiesUtil;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

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
public class MoreKafkaSourceDemo4BySessionWindowAndJoinTask {

    public static void main(String[] args) throws Exception {

        String value = PropertiesUtil.get("spring.application.name", "111");
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //设置time
//        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        // 其他
//         env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

//        env.setParallelism(5);
//        /*
//            Checkpoint机制
//            假如
//         */
//        env.enableCheckpointing(1000);
//        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
//        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
//        env.getCheckpointConfig().setCheckpointTimeout(70000);
//        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
//        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);


        //同时监听多个主题
        //创建消费者
        FlinkKafkaConsumer<String> flinkKafkaConsumer = FlinkKafkaConfig.getFlinkKafkaConsumer(TopicConstants.TOPIC_FLINK_DEMO_1);
        FlinkKafkaConsumer<String> flinkKafkaConsumer2 = FlinkKafkaConfig.getFlinkKafkaConsumer(TopicConstants.TOPIC_FLINK_DEMO_2);

        //添加输入源
        //设置并行度为3
        int parallelism = 3;
        DataStream<String> topic1Stream = env.addSource(flinkKafkaConsumer);
        DataStream<String> topic2Stream = env.addSource(flinkKafkaConsumer2);
//        DataStream<String> topic1Stream = env.addSource(flinkKafkaConsumer).setParallelism(parallelism);
//        DataStream<String> topic2Stream = env.addSource(flinkKafkaConsumer2).setParallelism(parallelism);
        //将流进行合并
        DataStream<String> stream = topic1Stream.join(topic2Stream)
                .where(new MyKeySelector())
                .equalTo(new MyKeySelector()) //两个函数用的key可以是不一样的，假如字段真的完全不同名的话
                .window(ProcessingTimeSessionWindows.withGap(Time.seconds(3)))
                .apply(new MoreKafkaSourceByJoinAndSessionWindowJoinFunction())
                .flatMap(new MoreKafkaSourceByJoinAndSessionWindowFlatMapFunction());

        //输出到控制台,这里输出的是最后一次调用collect设置的结果
        stream.print();

        //创建生产者
        FlinkKafkaProducer flinkKafkaProducer = FlinkKafkaConfig.getFlinkKafkaProducer(TopicConstants.TOPIC_FLINK_DEMO_3);
        //添加输出源
        stream.addSink(flinkKafkaProducer);

        System.out.println("调用execute方法");
        //输出执行计划
        System.out.println(env.getExecutionPlan());
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
