package cn.com.kun.apache.flink.tableapi.hive;

import cn.com.kun.apache.flink.flinkkafka.TopicConstants;
import cn.com.kun.apache.flink.flinkkafka.config.FlinkKafkaConfig;
import cn.com.kun.common.utils.JacksonUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.types.Row;
import org.apache.flink.util.CloseableIterator;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;

/**
 * 读kafka、查hive、写kafka
 *
 * author:xuyaokun_kzx
 * date:2021/12/21
 * desc:
*/
public class TestKafkaAndHiveDemo {

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //初始化table对象
        String catalogName     = "myhive";
        String hiveConfDir     = "/etc/hive/conf.cloudera.hive/";
        String version         = "1.2.1";
        String database = "test_myq";
        HiveInfoDao.init(catalogName, hiveConfDir, version, database);

        FlinkKafkaConsumer<String> flinkKafkaConsumer = FlinkKafkaConfig.getFlinkKafkaConsumer(TopicConstants.TOPIC_FLINK_DEMO_1);
        //添加输入源
        DataStream<String> stream = env.addSource(flinkKafkaConsumer);
        SingleOutputStreamOperator<KafkaAndHiveDemoMsg> stream2 = stream.map(new MapFunction<String, KafkaAndHiveDemoMsg>() {
            @Override
            public KafkaAndHiveDemoMsg map(String value) throws Exception {

                KafkaAndHiveDemoMsg flinkTopicMsg = new KafkaAndHiveDemoMsg();
                flinkTopicMsg = JacksonUtils.toJavaObject(value, KafkaAndHiveDemoMsg.class);
                return flinkTopicMsg;
            }
        });

        DataStream<KafkaAndHiveDemoDealResultMsg> stream3 = stream2.process(new ProcessFunction<KafkaAndHiveDemoMsg, KafkaAndHiveDemoDealResultMsg>() {

            @Override
            public void processElement(KafkaAndHiveDemoMsg value, Context ctx, Collector<KafkaAndHiveDemoDealResultMsg> out) throws Exception {

                //很关键，在这里就要调table api
                String sql = "select name from itcs_message where status = '0'";
                Table table = HiveInfoDao.query("");
                //拿到结果
                TableResult tableResult = table.execute();
                CloseableIterator<Row> rowCloseableIterator = tableResult.collect();
                List<Row> rowList = new ArrayList<>();
                while (rowCloseableIterator.hasNext()){
                    Row row = rowCloseableIterator.next();
                    //拿到第一列
                    Object str = row.getField(0);
                    KafkaAndHiveDemoDealResultMsg flinkTopicDealResultMsg = new KafkaAndHiveDemoDealResultMsg();
                    flinkTopicDealResultMsg.setMsgId((String) str);
                    out.collect(flinkTopicDealResultMsg);
                }
            }
        });

        //创建生产者
        FlinkKafkaProducer flinkKafkaProducer = FlinkKafkaConfig.getFlinkKafkaProducer(TopicConstants.TOPIC_FLINK_DEMO_3);
        //添加输出源
//        stream3.addSink(flinkKafkaProducer);
        //输出
        stream3.print();

        //开始执行
        env.execute("TestKafkaAndHive");
    }



    /*
        <!-- Flink Dependency -->
        <dependency>
            <groupId>org.apache.flink</groupId>
            <artifactId>flink-connector-hive_2.11</artifactId>
            <version>1.13.0</version>
            <scope>provided</scope>
        </dependency>

        <dependency>
            <groupId>org.apache.flink</groupId>
            <artifactId>flink-table-api-java-bridge_2.11</artifactId>
            <version>1.13.0</version>
            <scope>provided</scope>
        </dependency>

        <!-- Hive Dependency -->
        <dependency>
            <groupId>org.apache.hive</groupId>
            <artifactId>hive-exec</artifactId>
            <version>2.3.9</version>
            <scope>provided</scope>
        </dependency>
     */
}
