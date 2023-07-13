package cn.com.kun.kafka.dynamicConsume;

import cn.com.kun.kafka.dynamicConsume.extend.ConsumeSwitchQuerier;
import cn.com.kun.kafka.dynamicConsume.extend.KafkaConsumerBuilder;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Kafka动态消费组件
 *
 * author:xuyaokun_kzx
 * date:2023/7/12
 * desc:
*/
public class DynamicKafkaConsumer<K, V> implements Consumer<K, V> {

    /**
     * 被代理对象
     */
    private Consumer consumer;

    /**
     * 消费者构造器
     */
    private KafkaConsumerBuilder consumerBuilder;

    /**
     * 开关查询器
     */
    private ConsumeSwitchQuerier switchQuerier;

    /**
     * 待订阅的主题列表
     */
    private List<String> subscribeTopicList;

    public DynamicKafkaConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public DynamicKafkaConsumer(Consumer consumer, KafkaConsumerBuilder consumerBuilder, ConsumeSwitchQuerier switchQuerier) {
        this.consumer = consumer;
        this.consumerBuilder = consumerBuilder;
        this.switchQuerier = switchQuerier;
    }

    public KafkaConsumerBuilder getConsumerBuilder() {
        return consumerBuilder;
    }

    public void setConsumerBuilder(KafkaConsumerBuilder consumerBuilder) {
        this.consumerBuilder = consumerBuilder;
    }

    public ConsumeSwitchQuerier getSwitchQuerier() {
        return switchQuerier;
    }

    public void setSwitchQuerier(ConsumeSwitchQuerier switchQuerier) {
        this.switchQuerier = switchQuerier;
    }

    public List<String> getSubscribeTopicList() {
        return subscribeTopicList;
    }

    public void setSubscribeTopicList(List<String> subscribeTopicList) {
        this.subscribeTopicList = subscribeTopicList;
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
    public ConsumerRecords<K, V> poll(long timeout) {
        return poll(Duration.ofMillis(timeout));
    }

    @Override
    public ConsumerRecords<K, V> poll(Duration timeout) {

        if(queryConsumeSwitch(subscribeTopicList)){
            if (this.consumer == null){
                //创建消费者
                this.consumer = buildKafkaConsumer();
                this.consumer.subscribe(subscribeTopicList);
            }

            if (consumer == null) {
                throw new IllegalArgumentException("消费者不能为空");
            }
            //拉取消息
            ConsumerRecords<K, V> records = consumer.poll(timeout);
            return records;
        }else {
            //关闭当前消费者
            if (consumer != null){
                consumer.close();
                consumer = null;
            }
        }

        return null;
    }

    private Consumer buildKafkaConsumer() {

        return consumerBuilder.buildKafkaConsumer();
    }

    private boolean queryConsumeSwitch(List<String> subscribeTopicList) {

        return switchQuerier.querySwitch(subscribeTopicList);
    }

    @Override
    public void commitSync() {
        this.consumer.commitSync();
    }

    @Override
    public void commitSync(Duration timeout) {
        this.consumer.commitSync(timeout);
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
        this.consumer.seek(partition, offset);
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
        return this.consumer.offsetsForTimes(timestampsToSearch, timeout);
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
