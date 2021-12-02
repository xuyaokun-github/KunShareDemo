package cn.com.kun.framework.rxjava.demo1;

import rx.Observable;
import rx.Subscriber;

/**
 * 测试使用自定义的线程池做异步
 *
 * author:xuyaokun_kzx
 * date:2021/8/30
 * desc:
*/
public class TestSchedulers2 {

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
        }).subscribeOn(customRxJavaScheduler) //线程调度(假如没有用observeOn方法，默认使用与被观察者相同的线程)
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
