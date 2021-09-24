package cn.com.kun.apache.flink.flinkkafka.test;

import cn.com.kun.apache.flink.flinkkafka.config.FlinkKafkaConfig;
import cn.com.kun.apache.flink.flinkkafka.model.FlinkTopicMsg;
import cn.com.kun.apache.flink.flinkkafka.TopicConstants;
import cn.com.kun.common.utils.JacksonUtils;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.util.Date;

/**
 * 验证基于流处理消费kafka
 * 收到一个topic的内容
 * Mapstate的使用
 */
public class MapstateTest {

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
        stream = stream
                .keyBy(new KeySelector<String, Object>() {
                    @Override
                    public Object getKey(String value) throws Exception {

                        //先解析出这个属于哪个topic
                        //两个topic肯定有一个字段可以关联起来(例子中即tradeId)
                        FlinkTopicMsg flinkTopicMsg = JacksonUtils.toJavaObject(value, FlinkTopicMsg.class);
                        return flinkTopicMsg.getMsgId();
                    }
                })
                .flatMap(new MyFlatMapFunction());

        //输出到控制台
        stream.print();

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
    public static class MyFlatMapFunction extends RichFlatMapFunction<String, String> {

        private MapState<String, String> mapState;

        //FlatMapFunction接口没有open方法


        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            MapStateDescriptor descriptor = new MapStateDescriptor("MapDescriptor",String.class,String.class);
            mapState = getRuntimeContext().getMapState(descriptor);
        }

        @Override
        public void flatMap(String sentence, Collector<String> out) throws Exception {

            System.out.println("进入flatMap函数，参数：" + sentence);

            FlinkTopicMsg flinkTopicMsg = JacksonUtils.toJavaObject(sentence, FlinkTopicMsg.class);
            mapState.put(flinkTopicMsg.getMsgId() + flinkTopicMsg.getStatusCode(), flinkTopicMsg.getMsgId());
            System.out.println("当前mapState： values:" + JacksonUtils.toJSONString(mapState.values())
                + " keys:" + JacksonUtils.toJSONString(mapState.keys()));
//            System.out.println("当前mapState：" + mapState.toString());

            //每个key,都会有一个独立的mapState


                flinkTopicMsg.setUpdateTime(new Date());
                String newRes = JacksonUtils.toJSONString(flinkTopicMsg);
                //调用collect有什么效果？
                // 调了collect才会将结果放到输出流，sink收到的数据就是collect的数据。
                //假如不调，输出流就没有数据
                out.collect(newRes);
        }

    }

}
