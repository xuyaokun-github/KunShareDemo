package cn.com.kun.foo.javacommon.juc;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Exchanger;

/**
 * 线程之间交换数据
 * （最好不要多个线程间交换，增加了控制的复杂度）
 */
public class MoreThreadExchangeTest {

    private static final Exchanger<Set<String>> exchanger = new Exchanger<>();

    public static void main(String[] args) {

        new Thread(){
            @Override
            public void run() {
                Set<String> bSet = new HashSet<>();
                bSet.add("11");
                bSet.add("22");
                bSet.add("33");
                try {
                    Set<String> exchange = exchanger.exchange(bSet);
                    for (String s : exchange) {
                        System.out.println("cSet"+s);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                Set<String> aSet = new HashSet<>();
                aSet.add("A");
                aSet.add("B");
                aSet.add("C");
                try {
                    Set<String> exchange = exchanger.exchange(aSet);
                    for (String s : exchange) {
                        System.out.println("aSet"+s);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                Set<String> bSet = new HashSet<>();
                bSet.add("1");
                bSet.add("2");
                bSet.add("3");
                try {
                    Set<String> exchange = exchanger.exchange(bSet);
                    for (String s : exchange) {
                        System.out.println("bSet"+s);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                Set<String> bSet = new HashSet<>();
                bSet.add("X");
                bSet.add("Y");
                bSet.add("K");
                try {
                    Set<String> exchange = exchanger.exchange(bSet);
                    for (String s : exchange) {
                        System.out.println("dSet"+s);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

}
