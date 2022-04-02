package cn.com.kun.component.produceConsume;

public interface Model {

    Runnable newRunnableConsumer();
    Runnable newRunnableProducer();
}
