package cn.com.kun.classlibrary.rxjava.demo1;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * 测试使用自定义的线程池做异步
 *
 * author:xuyaokun_kzx
 * date:2021/8/30
 * desc:
*/
public class TestSchedulers {

    //可以使用自己定义的线程池，来执行观察者或者被观察者的逻辑
    static CustomRxJavaScheduler customRxJavaScheduler = new CustomRxJavaScheduler();

    public static void main(String[] args) throws InterruptedException {

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                System.out.println("当前线程：" + Thread.currentThread().getName());
                subscriber.onNext("aaaa");
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()) //线程调度
                .observeOn(customRxJavaScheduler) //然后被观察者用另一个线程执行
                .subscribe(new Subscriber<String>() {

                    @Override
                    public void onCompleted() {
                        System.out.println("执行onCompleted方法");
                        System.out.println("当前线程：" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("执行onNext方法，" + s);
                    }
                });

        //必须先挂起
        while (true){
            Thread.sleep(1000);
        }
    }
}
