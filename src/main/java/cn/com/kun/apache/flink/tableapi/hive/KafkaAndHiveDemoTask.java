package cn.com.kun.apache.flink.tableapi.hive;

import cn.com.kun.apache.flink.flinkkafka.TopicConstants;
import cn.com.kun.apache.flink.flinkkafka.config.FlinkKafkaConfig;
import cn.com.kun.apache.flink.tableapi.hive.dao.HiveInfoDao;
import cn.com.kun.apache.flink.tableapi.hive.entity.PeopleHiveDO;
import cn.com.kun.apache.flink.tableapi.hive.kafkamsg.KafkaAndHiveDemoMsg;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读kafka、查hive、写kafka
 * 已成功
 *
 * author:xuyaokun_kzx
 * date:2021/12/21
 * desc:
*/
public class KafkaAndHiveDemoTask {

    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaAndHiveDemoTask.class);

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //hive相关属性
        //定义一个唯一的名称，这个值是可以随意定义的
        String catalogName = "myhive";
        //hive-site.xml的正确位置
        String hiveConfDir = "D:\\hive\\apache-hive-2.3.6-bin\\conf";
        String version = "2.3.6";
        String database = "test";

        FlinkKafkaConsumer<String> flinkKafkaConsumer = FlinkKafkaConfig.getFlinkKafkaConsumer(TopicConstants.TOPIC_FLINK_DEMO_1);
        //添加输入源
        DataStream<String> stream = env.addSource(flinkKafkaConsumer);
        SingleOutputStreamOperator<KafkaAndHiveDemoMsg> stream2 = stream.map(new MapFunction<String, KafkaAndHiveDemoMsg>() {
            @Override
            public KafkaAndHiveDemoMsg map(String value) throws Exception {

                LOGGER.info("输入端入参：{}", value);
                System.out.println("输入端入参：" + value);
                KafkaAndHiveDemoMsg flinkTopicMsg = new KafkaAndHiveDemoMsg();
                flinkTopicMsg = JacksonUtils.toJavaObject(value, KafkaAndHiveDemoMsg.class);
                return flinkTopicMsg;
            }
        });

        DataStream<PeopleHiveDO> stream3 = stream2.process(new ProcessFunction<KafkaAndHiveDemoMsg, PeopleHiveDO>() {

            @Override
            public void processElement(KafkaAndHiveDemoMsg value, Context ctx, Collector<PeopleHiveDO> out) throws Exception {

                try {
                    //很关键，在这里就要调table api
                    String sql = "SELECT id, name, destination FROM people where id = 1";
                    //初始化table对象
                    HiveInfoDao.init(catalogName, hiveConfDir, version, database);
                    Table table = HiveInfoDao.query(sql);
                    //拿到结果
                    TableResult tableResult = table.execute();
                    CloseableIterator<Row> rowCloseableIterator = tableResult.collect();
                    while (rowCloseableIterator.hasNext()){
                        Row row = rowCloseableIterator.next();
                        PeopleHiveDO peopleHiveDO = new PeopleHiveDO();
                        //拿到第一列
                        peopleHiveDO.setId((Integer) row.getField(0));
                        peopleHiveDO.setName((String) row.getField(1));
                        peopleHiveDO.setDestination((String) row.getField(2));
                        out.collect(peopleHiveDO);
                    }
                }catch (Throwable e){
                    System.out.println("查询hive异常");
                    e.printStackTrace();
                }
            }
        });

        //创建生产者
        FlinkKafkaProducer flinkKafkaProducer = FlinkKafkaConfig.getFlinkKafkaProducer(TopicConstants.TOPIC_FLINK_DEMO_3);
        //添加输出源,输出到kafka
//        stream3.addSink(flinkKafkaProducer);

        //输出
        stream3.print();

        //开始执行
        System.out.println("KafkaAndHiveDemoTask start");
        env.execute("KafkaAndHiveDemoTask");
    }

}
