package cn.com.kun.framework.rxjava.demo1;

import cn.com.kun.common.utils.ThreadUtils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func0;

public class TestSingle {

    public static void main(String[] args) {
        testSingle1();
//        testSingle2();
    }

    /**
     * just 和 single() 配合使用
     */
    private static void testSingle2() {
        System.out.println("-----------------------");
        //定义一个被观察者
        Observable<String> observable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                Observable<String> mObservable = Observable.just(hello());
                //假如用just发送了多个数据，single()就会报错，如下
//                Observable<String> mObservable = Observable.just(hello(), hello());
                return mObservable;
            }
        });

        Subscription subscription = observable.single().subscribe(new Subscriber() {
            @Override
            public void onCompleted() {
                //Observable.just创建出来的被观察者，会自动触发 观察者的onCompleted方法
                //这是和普通create方式不同之处，普通的unsafeCreate创建出来的被观察者，需要主动执行观察者的方法
                ThreadUtils.logWithThreadInfo("处理观察者逻辑onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                ThreadUtils.logWithThreadInfo("处理观察者逻辑onError");
            }

            @Override
            public void onNext(Object o) {
                ThreadUtils.logWithThreadInfo("处理观察者逻辑onNext:" + o);
            }
        });

        ThreadUtils.runForever();

    }

    private static String hello() {
        return "hello";
    }

    private static void testSingle1() {

        //定义一个被观察者
        Observable<String> observable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {

                Observable<String> mObservable = Observable.unsafeCreate(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        ThreadUtils.logWithThreadInfo("处理被观察者逻辑");
                        subscriber.onNext("事件订阅开始");
                    }
                });
                return mObservable;
            }
        });

//        Observable<String> observable = Observable.unsafeCreate(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                ThreadUtils.logWithThreadInfo("处理被观察者逻辑");
//                subscriber.onNext("hello");
//            }
//        });

        //假如没加single()，这个观察者的逻辑能正常被调用到，反之，加了single()，就调不到了
        //为什么呢？
        Subscription subscription = observable/*.single()*/.subscribe(new Subscriber() {
            @Override
            public void onCompleted() {
                //Observable.just创建出来的被观察者，会自动触发 观察者的onCompleted方法
                //这是和普通create方式不同之处，普通的unsafeCreate创建出来的被观察者，需要主动执行观察者的方法
                ThreadUtils.logWithThreadInfo("处理观察者逻辑onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                ThreadUtils.logWithThreadInfo("处理观察者逻辑onError");
            }

            @Override
            public void onNext(Object o) {
                ThreadUtils.logWithThreadInfo("处理观察者逻辑onNext:" + o);
            }
        });

//        //订阅事件1，没产生一个订阅就会生成一个新的observable对象
//        observable.subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                System.out.println("观察者2订阅事件    "+s);
//            }
//        });
//        //订阅事件2，没产生一个订阅就会生成一个新的observable对象
//        observable.subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                System.out.println("观察者1订阅事件    "+s);
//            }
//        });

    }
}
