package cn.com.kun.apache.flink.flinkkafka.operators.join;

import cn.com.kun.apache.flink.flinkkafka.model.FlinkTopicDealResultMsg;
import cn.com.kun.apache.flink.flinkkafka.model.FlinkTopicMsg;
import cn.com.kun.common.utils.JacksonUtils;
import org.apache.flink.api.common.functions.JoinFunction;

public class MoreKafkaSourceByJoinAndSessionWindowJoinFunction implements JoinFunction<String, String, FlinkTopicDealResultMsg> {

    /**
     * 这种情况假如数据没到齐，不会触发任何处理！！
     * 因为where和equal没匹配成功，所以不会有后续
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
}
