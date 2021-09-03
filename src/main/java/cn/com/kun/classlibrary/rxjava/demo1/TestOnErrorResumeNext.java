package cn.com.kun.classlibrary.rxjava.demo1;

import cn.com.kun.common.utils.ThreadUtils;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * 这也是rxjava一个重要的特性
 *
 * author:xuyaokun_kzx
 * date:2021/9/2
 * desc:
*/
public class TestOnErrorResumeNext {

    public static void main(String[] args) {
        testM1();
    }

    private static void testM1() {

        //模仿hystrix中的写法
        Observable observable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                Observable observable1;
                try {
                    observable1 = Observable.just(hello());
                }catch (Throwable e){
                    observable1 = Observable.error(e);
                }
                return observable1;
            }
        });

        observable = observable.doOnNext(new Action1<String>() {
            @Override
            public void call(String s) {
                ThreadUtils.logWithThreadInfo("处理观察者逻辑doOnNext，" + s);
            }
        })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends String>>() {

                    //假如正常的分支出异常了，就走降级分支。假如正常分支没有问题，这里就不会执行
                    @Override
                    public Observable<? extends String> call(Throwable throwable) {
                        ThreadUtils.logWithThreadInfo("执行onErrorResumeNext逻辑");
                        return Observable.just("降级结果");
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

        //在正常分支中，模拟一个异常
        int a = 1/0;
        return "hello";
    }

}
