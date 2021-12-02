package cn.com.kun.framework.rxjava.demo1;

import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func0;
import rx.internal.schedulers.ScheduledAction;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 参考com.netflix.hystrix.strategy.concurrency.HystrixContextScheduler.ThreadPoolScheduler类实现即可
 *
 * author:xuyaokun_kzx
 * date:2021/8/30
 * desc:
*/
public class CustomRxJavaScheduler extends Scheduler {

    //线程池容器类（里面含有ThreadPoolExecutor）
    CustomThreadPool threadPool = new CustomThreadPool();


    /**
     * 自定义实现Scheduler，只需要实现一个方法
     * @return
     */
    @Override
    public Worker createWorker() {

        Func0<Boolean> shouldInterruptThread = new Func0<Boolean>() {
            @Override
            public Boolean call() {
                //是否打断一个线程
                return true;
            }
        };
        return new CustomRxJavaSchedulerWorker(shouldInterruptThread, threadPool);
    }

    public class CustomRxJavaSchedulerWorker extends Scheduler.Worker {

        private final CompositeSubscription subscription = new CompositeSubscription();
        private final Func0<Boolean> shouldInterruptThread;
        private final CustomThreadPool threadPool;//

        public CustomRxJavaSchedulerWorker(Func0<Boolean> shouldInterruptThread, CustomThreadPool threadPool) {
            this.shouldInterruptThread = shouldInterruptThread;
            this.threadPool = threadPool;
        }

        @Override
        public Subscription schedule(Action0 action) {
            if (subscription.isUnsubscribed()) {
                // don't schedule, we are unsubscribed
                return Subscriptions.unsubscribed();
            }

            // This is internal RxJava API but it is too useful.
            ScheduledAction sa = new ScheduledAction(action);

            subscription.add(sa);
            sa.addParent(subscription);

            ThreadPoolExecutor executor = (ThreadPoolExecutor) threadPool.getExecutor();
            FutureTask<?> f = (FutureTask<?>) executor.submit(sa);
            sa.add(new FutureCompleterWithConfigurableInterrupt(f, shouldInterruptThread, executor));

            return sa;
        }

        @Override
        public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {
            throw new IllegalStateException("CustomRxJavaSchedulerWorker does not support delayed scheduling");
        }

        @Override
        public void unsubscribe() {

        }

        @Override
        public boolean isUnsubscribed() {
            return false;
        }

        private class FutureCompleterWithConfigurableInterrupt implements Subscription {
            private final FutureTask<?> f;
            private final Func0<Boolean> shouldInterruptThread;
            private final ThreadPoolExecutor executor;

            private FutureCompleterWithConfigurableInterrupt(FutureTask<?> f, Func0<Boolean> shouldInterruptThread, ThreadPoolExecutor executor) {
                this.f = f;
                this.shouldInterruptThread = shouldInterruptThread;
                this.executor = executor;
            }

            @Override
            public void unsubscribe() {
                executor.remove(f);
                if (shouldInterruptThread.call()) {
                    f.cancel(true);
                } else {
                    f.cancel(false);
                }
            }

            @Override
            public boolean isUnsubscribed() {
                return f.isCancelled();
            }
        }
    }
}
