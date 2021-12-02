package cn.com.kun.framework.rxjava.demo1;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func0;

public class TestDefer {

    public static void main(String[] args) {

        //定义一个被观察者
        Observable<String> observable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                Observable<String> mObservable = Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("事件订阅开始");
                    }
                });
                return mObservable;
            }
        });
        //订阅事件1，没产生一个订阅就会生成一个新的observable对象
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("观察者2订阅事件    "+s);
            }
        });
        //订阅事件2，没产生一个订阅就会生成一个新的observable对象
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("观察者1订阅事件    "+s);
            }
        });
    }
}
