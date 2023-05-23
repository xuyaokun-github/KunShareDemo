package cn.com.kun.kafka.autoSwitch.decorator;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 消费者装饰者
 *
 * author:xuyaokun_kzx
 * date:2023/5/6
 * desc:
*/
public class KafkaConsumerDecorator<K, V> implements Consumer<K, V> {

    private Consumer consumer;


    public KafkaConsumerDecorator(Consumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public ConsumerRecords<K, V> poll(long timeout) {
        return this.consumer.poll(timeout);
    }

    @Override
    public ConsumerRecords<K, V> poll(Duration timeout) {
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
