package cn.com.kun.kafka.autoSwitch.decorator;

import cn.com.kun.kafka.autoSwitch.core.AutoSwitchInfoHolder;
import cn.com.kun.kafka.autoSwitch.factory.KafkaConsumerFactory;
import cn.com.kun.kafka.autoSwitch.vo.KafkaCluster;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 消费者装饰者
 * 与主题的关系是一一对应。一个KafkaConsumerDecorator对应一个主题
 *
 * author:xuyaokun_kzx
 * date:2023/5/6
 * desc:
*/
public class KafkaConsumerDecorator<K, V> implements Consumer<K, V> {

    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerDecorator.class);

    /**
     * 被代理对象
     */
    private Consumer consumer;

    /**
     * 目标主题
     */
    private String targetTopic;

    private KafkaConsumerFactory kafkaConsumerFactory;

    /**
     * 当前连接集群
     */
    private String currentCluster;

    public KafkaConsumerDecorator(Consumer consumer, String targetTopic) {
        this.consumer = consumer;
        this.targetTopic = targetTopic;
    }


    public String getTargetTopic() {
        return targetTopic;
    }

    public void setTargetTopic(String targetTopic) {
        this.targetTopic = targetTopic;
    }

    public KafkaConsumerFactory getKafkaConsumerFactory() {
        return kafkaConsumerFactory;
    }

    public void setKafkaConsumerFactory(KafkaConsumerFactory kafkaConsumerFactory) {
        this.kafkaConsumerFactory = kafkaConsumerFactory;
    }

    @Override
    public ConsumerRecords<K, V> poll(long timeout) {

        //判断是否需要进行切换
        checkAndSwitch(targetTopic);
        return this.consumer.poll(timeout);
    }

    /**
     * 判断是否需要切换
     *
     * @return
     * @param targetTopic
     */
    private void checkAndSwitch(String targetTopic) {

        KafkaCluster kafkaCluster = AutoSwitchInfoHolder.getBestTargetCluster(targetTopic);
        //切换前，必须要确保当前lag值为0，即当前集群的剩余消息全部消费完
        long currentLag = getLag();
        //最佳集群 和 当前链接集群 不相同意味着需要切换
        boolean needSwitch = kafkaCluster != null && !kafkaCluster.getName().equals(currentCluster);
        if (needSwitch && currentLag <= 0){
            LOGGER.info("开始执行消费者切换,新集群地址：{}", kafkaCluster.getAddress());
            //设置当前连接集群
            currentCluster = kafkaCluster.getName();
            //必须等原集群的消息消费完才可以进行切换
            //关闭旧的消费者
            this.consumer.close();
            //重新创建消费者
            this.consumer = kafkaConsumerFactory.buildConsumer(targetTopic, kafkaCluster.getAddress());
            //重新订阅
            this.consumer.subscribe(Arrays.asList(targetTopic));
        }

    }

    private long getLag() {

        HashMap<TopicPartition, Long> topicConsumerOffsets = new HashMap<>();
        Set<TopicPartition> topicPartitions = this.consumer.assignment();
        Iterator it = topicPartitions.iterator();
        while (it.hasNext()){
            TopicPartition tp = (TopicPartition) it.next();
            topicConsumerOffsets.put(tp, this.consumer.position(tp));
        }

        long lag = 0L;
        Map<TopicPartition, Long> offsetEndMap = this.consumer.endOffsets(topicConsumerOffsets.keySet());

        Map.Entry entry;
        Iterator entrySetIterator = topicConsumerOffsets.entrySet().iterator();
        while (entrySetIterator.hasNext()){
            entry = (Map.Entry) entrySetIterator.next();
            lag += (Long) offsetEndMap.get(entry.getKey()) - (Long)entry.getValue();
        }

        if (lag > 0){
            LOGGER.info("当前堆积量：{}", lag);
        }

        return lag;
    }

    @Override
    public ConsumerRecords<K, V> poll(Duration timeout) {

        //判断是否需要进行切换
        checkAndSwitch(targetTopic);
        return this.consumer.poll(timeout);
    }

    @Override
    public Set<TopicPartition> assignment() {
        return this.consumer.assignment();
    }

    @Override
    public Set<String> subscription() {
        return this.consumer.subscription();
    }

    @Override
    public void subscribe(Collection<String> topics) {
        this.consumer.subscribe(topics);
    }

    @Override
    public void subscribe(Collection<String> topics, ConsumerRebalanceListener callback) {
        this.consumer.subscribe(topics, callback);
    }

    @Override
    public void assign(Collection<TopicPartition> partitions) {
        this.consumer.assign(partitions);
    }

    @Override
    public void subscribe(Pattern pattern, ConsumerRebalanceListener callback) {
        this.consumer.subscribe(pattern, callback);
    }

    @Override
    public void subscribe(Pattern pattern) {
        this.consumer.subscribe(pattern);
    }

    @Override
    public void unsubscribe() {
        this.consumer.unsubscribe();
    }


    @Override
    public void commitSync() {
        this.consumer.commitSync();
    }

    @Override
    public void commitSync(Duration timeout) {
        this.consumer.commitSync();
    }

    @Override
    public void commitSync(Map<TopicPartition, OffsetAndMetadata> offsets) {
        this.consumer.commitSync(offsets);
    }

    @Override
    public void commitSync(Map<TopicPartition, OffsetAndMetadata> offsets, Duration timeout) {
        this.consumer.commitSync(offsets, timeout);
    }

    @Override
    public void commitAsync() {
        this.consumer.commitAsync();
    }

    @Override
    public void commitAsync(OffsetCommitCallback callback) {
        this.consumer.commitAsync(callback);
    }

    @Override
    public void commitAsync(Map<TopicPartition, OffsetAndMetadata> offsets, OffsetCommitCallback callback) {
        this.consumer.commitAsync(offsets, callback);
    }

    @Override
    public void seek(TopicPartition partition, long offset) {
        this.consumer.seek(partition,offset);
    }

    @Override
    public void seekToBeginning(Collection<TopicPartition> partitions) {
        this.consumer.seekToBeginning(partitions);
    }

    @Override
    public void seekToEnd(Collection<TopicPartition> partitions) {
        this.consumer.seekToEnd(partitions);
    }

    @Override
    public long position(TopicPartition partition) {
        return this.consumer.position(partition);
    }

    @Override
    public long position(TopicPartition partition, Duration timeout) {
        return this.consumer.position(partition, timeout);
    }

    @Override
    public OffsetAndMetadata committed(TopicPartition partition) {
        return this.consumer.committed(partition);
    }

    @Override
    public OffsetAndMetadata committed(TopicPartition partition, Duration timeout) {
        return this.consumer.committed(partition, timeout);
    }

    @Override
    public Map<MetricName, ? extends Metric> metrics() {
        return this.consumer.metrics();
    }

    @Override
    public List<PartitionInfo> partitionsFor(String topic) {
        return this.consumer.partitionsFor(topic);
    }

    @Override
    public List<PartitionInfo> partitionsFor(String topic, Duration timeout) {
        return this.consumer.partitionsFor(topic, timeout);
    }

    @Override
    public Map<String, List<PartitionInfo>> listTopics() {
        return this.consumer.listTopics();
    }

    @Override
    public Map<String, List<PartitionInfo>> listTopics(Duration timeout) {
        return this.consumer.listTopics(timeout);
    }

    @Override
    public Set<TopicPartition> paused() {
        return this.consumer.paused();
    }

    @Override
    public void pause(Collection<TopicPartition> partitions) {
        this.consumer.pause(partitions);
    }

    @Override
    public void resume(Collection<TopicPartition> partitions) {
        this.consumer.resume(partitions);
    }

    @Override
    public Map<TopicPartition, OffsetAndTimestamp> offsetsForTimes(Map<TopicPartition, Long> timestampsToSearch) {
        return this.consumer.offsetsForTimes(timestampsToSearch);
    }

    @Override
    public Map<TopicPartition, OffsetAndTimestamp> offsetsForTimes(Map<TopicPartition, Long> timestampsToSearch, Duration timeout) {
        return this.consumer.offsetsForTimes(timestampsToSearch,timeout);
    }

    @Override
    public Map<TopicPartition, Long> beginningOffsets(Collection<TopicPartition> partitions) {
        return this.consumer.beginningOffsets(partitions);
    }

    @Override
    public Map<TopicPartition, Long> beginningOffsets(Collection<TopicPartition> partitions, Duration timeout) {
        return this.consumer.beginningOffsets(partitions, timeout);
    }

    @Override
    public Map<TopicPartition, Long> endOffsets(Collection<TopicPartition> partitions) {
        return this.consumer.endOffsets(partitions);
    }

    @Override
    public Map<TopicPartition, Long> endOffsets(Collection<TopicPartition> partitions, Duration timeout) {
        return this.consumer.endOffsets(partitions, timeout);
    }

    @Override
    public void close() {
        this.consumer.close();
    }

    @Override
    public void close(long timeout, TimeUnit unit) {
        this.consumer.close(timeout, unit);
    }

    @Override
    public void close(Duration timeout) {
        this.consumer.close(timeout);
    }

    @Override
    public void wakeup() {
        this.consumer.wakeup();
    }
}
