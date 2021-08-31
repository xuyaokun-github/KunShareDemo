package cn.com.kun.classlibrary.rxjava.demo1;

import cn.com.kun.common.utils.ThreadUtils;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * just使用例子
 *
 * author:xuyaokun_kzx
 * date:2021/8/31
 * desc:
*/
public class TestJust {

    public static void main(String[] args) {

            testJust1();
            //不用just
            testNoJust();
//        ThreadUtils.runForever();
    }

    private static void testNoJust() {
        System.out.println("---------------");
        Observable observable = Observable.unsafeCreate(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                ThreadUtils.logWithThreadInfo("处理被观察者逻辑");
//                subscriber.onNext("hello2");
            }
        });
        observable.subscribe(new Subscriber() {
            @Override
            public void onCompleted() {
                //Observable.just创建出来的被观察者，会自动触发 观察者的onCompleted方法
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
    }

    private static void testJust1() {
        //just方法的参数就是一个具体的值
        //注意，这里hello并不是一个观察者逻辑了，这里就不是函数式编程了，这里的hello方法会被优先执行
        //hello方法的结果会被发射到观察者中，由观察者继续处理。
        Observable observable = Observable.just(hello())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        ThreadUtils.logWithThreadInfo("处理观察者逻辑，" + s);
                    }
                });

        observable.subscribe(new Subscriber() {
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
    }


    private static String hello() {
        ThreadUtils.logWithThreadInfo("执行hello方法");
        return "hello";
    }

}
