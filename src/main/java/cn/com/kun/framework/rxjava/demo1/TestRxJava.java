package cn.com.kun.framework.rxjava.demo1;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * 简单的观察者模式例子
 *
 * author:xuyaokun_kzx
 * date:2021/8/30
 * desc:
*/
public class TestRxJava {


    public static void main(String[] args) {

        //创建被观察者
        Observable mObservable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {

                System.out.println("执行被观察者的call方法");
                System.out.println("当前执行call的线程：" + Thread.currentThread().getName());
                //被观察者的方法执行完之后，需要执行观察者的方法，就可以调用onNext方法，假如不需要执行观察者逻辑，可以不调用

                //调用观察者的方法
                subscriber.onNext("hello world!!!");

                if(true){
                    throw new RuntimeException("fffff");
                }

                //假如上面抛异常了，下面的onCompleted将不会被执行，被执行的是error方法
//                subscriber.onCompleted();

                //onCompleted方法需要被观察者来进行调用

            }
        });

        //创建观察者
        Subscriber subscriber = new Subscriber<String>() {

            @Override
            public void onStart() {
                super.onStart();
                System.out.println("执行观察者的onStart方法");
            }

            /**
             * rx.Subscriber#onCompleted和
             * rx.Subscriber#onError 二者只有其中一个会被执行
             */
            @Override
            public void onCompleted() {
                System.out.println("执行onCompleted方法");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("执行onError方法");
                throwable.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                System.out.println("执行onNext方法，" + s);
            }

        };

        System.out.println("当前线程：" + Thread.currentThread().getName());

        //注册观察者，订阅事件
        //假如不调用subscribe方法，相当于啥事情都没有发生
        //在调用subscribe方法之后，会先触发观察者的onStart方法方法，然后就会触发被观察者的call方法，并且是由同一个线程完成的。
        //
        Subscription subscription = mObservable.subscribe(subscriber);
        //订阅成功后，会返回一个subscription对象。
        System.out.println(subscription);
        //拿到这个subscription对象，可以做什么？
        //只有两个方法可以调
        // 一个是取消订阅么？取消订阅会起到什么效果？
        subscription.unsubscribe();
        //一个是判断是否已取消
        System.out.println(subscription.isUnsubscribed());

    }


}
