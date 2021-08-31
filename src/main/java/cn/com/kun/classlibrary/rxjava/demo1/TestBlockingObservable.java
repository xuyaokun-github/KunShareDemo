package cn.com.kun.classlibrary.rxjava.demo1;

import cn.com.kun.common.utils.ThreadUtils;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 异步转同步的例子
 *
 * author:xuyaokun_kzx
 * date:2021/8/31
 * desc:
*/
public class TestBlockingObservable {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //定义一个被观察者
        Observable<String> observable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                Observable<String> mObservable = Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        ThreadUtils.logWithThreadInfo("开始执行被观察者逻辑 " + Thread.currentThread().getName());

                        //这是一个被观察者
                        try {
                            Thread.sleep(6000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ThreadUtils.logWithThreadInfo("结束执行被观察者逻辑");

                        subscriber.onNext("onNext开始");
                        //必须要调用完成，否则内部的计数器不会减一，Future将永久阻塞
                        subscriber.onCompleted();
                    }
                })
                //订阅事件1，没产生一个订阅就会生成一个新的observable对象
                .subscribeOn(Schedulers.newThread())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        ThreadUtils.logWithThreadInfo("观察者2订阅事件:" + s);
                    }
                });
                return mObservable;
            }
        });

        ThreadUtils.logWithThreadInfo("准备调用toFuture()，异步转同步 当前线程："  + Thread.currentThread().getName());
        Future future = observable.toBlocking().toFuture();
        ThreadUtils.logWithThreadInfo("准备调用get()，异步转同步");
        Object res = future.get();
        ThreadUtils.logWithThreadInfo("拿到的结果：" + res);
        
        //必须先挂起
        ThreadUtils.runForever();
    }
}
