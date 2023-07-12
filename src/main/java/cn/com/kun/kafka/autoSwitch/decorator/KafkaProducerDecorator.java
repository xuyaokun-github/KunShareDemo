package cn.com.kun.kafka.autoSwitch.decorator;

import cn.com.kun.kafka.autoSwitch.core.AutoSwitchInfoHolder;
import cn.com.kun.kafka.autoSwitch.factory.KafkaProducerFactory;
import cn.com.kun.kafka.autoSwitch.vo.KafkaCluster;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * KafkaProducer装饰者(不用装饰者模式，用代理模式也是可以的)
 * TODO 还是要有一个getKafkaProducer方法，要支持懒加载
 * 假如自动切换集群功能未开启，要支持用配置文件里的地址创建
 *
 * author:xuyaokun_kzx
 * date:2023/5/6
 * desc:
*/
public class KafkaProducerDecorator<K, V> implements Producer<K, V> {

    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaProducerDecorator.class);

    /**
     * 被装饰者（单个KafkaProducerDecorator只能同时写一个集群，不能同时写两个集群，则被装饰者一个就足够）
     * 假如需要支持同时写两个集群，可以定义多个KafkaProducerDecorator
     */
    private Producer kafkaProducer;

    /**
     * 目标主题
     */
    private String targetTopic;

    private KafkaProducerFactory kafkaProducerFactory;

    /**
     * 当前连接集群
     */
    private String currentCluster;

    public KafkaProducerDecorator() {
    }

    public KafkaProducerDecorator(Producer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public KafkaProducerDecorator(Producer kafkaProducer, String targetTopic) {
        this.kafkaProducer = kafkaProducer;
        this.targetTopic = targetTopic;
    }

    public KafkaProducerFactory getKafkaProducerFactory() {
        return kafkaProducerFactory;
    }

    public void setKafkaProducerFactory(KafkaProducerFactory kafkaProducerFactory) {
        this.kafkaProducerFactory = kafkaProducerFactory;
    }

    public String getTargetTopic() {
        return targetTopic;
    }

    public void setTargetTopic(String targetTopic) {
        this.targetTopic = targetTopic;
    }

    @Override
    public void initTransactions() {
        getKafkaProducer().initTransactions();
    }

    @Override
    public void beginTransaction() throws ProducerFencedException {
        getKafkaProducer().beginTransaction();
    }

    @Override
    public void sendOffsetsToTransaction(Map<TopicPartition, OffsetAndMetadata> offsets, String consumerGroupId) throws ProducerFencedException {
        getKafkaProducer().sendOffsetsToTransaction(offsets, consumerGroupId);
    }

    @Override
    public void commitTransaction() throws ProducerFencedException {
        getKafkaProducer().commitTransaction();
    }

    @Override
    public void abortTransaction() throws ProducerFencedException {
        getKafkaProducer().abortTransaction();
    }

    @Override
    public Future<RecordMetadata> send(ProducerRecord<K, V> record) {

        //主题
        String topic = record.topic();
        Assert.isTrue(topic.equals(targetTopic), String.format("当前生产主题：%s 与 目标主题：%s 不一致，请检查bean定义", topic, targetTopic));

        Future<RecordMetadata> future = getKafkaProducer().send(record);
        return future;
    }

    private void showPartitionInfo(List<PartitionInfo> partitionInfoList) {
        if (partitionInfoList != null && partitionInfoList.size() > 0){
            StringBuilder builder = new StringBuilder("服务器地址信息（个别地址，非全部）：");
            for (PartitionInfo partitionInfo : partitionInfoList){
                //partitionInfo.leader()有可能为空，当3个节点宕机了一个节点时，就会出现这种情况，partitionInfo.leader()是null
                if (partitionInfo.leader() != null){
                    builder.append("host:" + partitionInfo.leader().host() + "port:" + partitionInfo.leader().port());
                }
            }
            LOGGER.info(builder.toString());
        }
    }

    private Producer rebuildProducer(String topic, String address) {

        //工厂模式
        //构建生产者,无论哪个topic,创建生产者的实现可以一样，也可以定制化，交给工厂
        Producer producer = kafkaProducerFactory.buildProducer(topic, address);
        return producer;
    }

    private <K, V> Producer getKafkaProducer() {

        Assert.notNull(targetTopic, "目标主题不能为空，一个KafkaProducerDecorator对应一个Topic");
        //检查是否需要切换集群
        //检查是否发生了集群切换，重新建生产者
        KafkaCluster kafkaCluster = AutoSwitchInfoHolder.getBestTargetCluster(targetTopic);
        //最佳集群 和 当前链接集群 不相同意味着需要切换
        boolean needSwitch = kafkaCluster != null && !kafkaCluster.getName().equals(currentCluster);
        if (needSwitch){
            //先关闭当前生产者
            if (kafkaProducer != null){
                kafkaProducer.flush();
                kafkaProducer.close();
            }
            //重建生产者kafkaProducer = {KafkaProducer@23646}
            kafkaProducer = rebuildProducer(targetTopic, kafkaCluster.getAddress());
            //设置当前连接集群
            currentCluster = kafkaCluster.getName();
            LOGGER.info("生产者重建，新集群：{} 地址：{}", kafkaCluster.getName(), kafkaCluster.getAddress());
        }

        if (kafkaProducer instanceof KafkaProducer){
            //这里只能通过反射拿到它的生产者配置属性，可以输出它的服务器地址
            List<PartitionInfo> partitionInfoList = kafkaProducer.partitionsFor(targetTopic);
            //生产不要输出，调试用就好
            showPartitionInfo(partitionInfoList);
        }
        return kafkaProducer;
    }

    @Override
    public Future<RecordMetadata> send(ProducerRecord<K, V> record, Callback callback) {
        return getKafkaProducer().send(record, callback);
    }

    @Override
    public void flush() {
        getKafkaProducer().flush();
    }

    @Override
    public List<PartitionInfo> partitionsFor(String topic) {
        return getKafkaProducer().partitionsFor(topic);
    }

    @Override
    public Map<MetricName, ? extends Metric> metrics() {
        return getKafkaProducer().metrics();
    }

    @Override
    public void close() {
        getKafkaProducer().close();
    }

    @Override
    public void close(long timeout, TimeUnit unit) {
        getKafkaProducer().close(timeout, unit);
    }
}
