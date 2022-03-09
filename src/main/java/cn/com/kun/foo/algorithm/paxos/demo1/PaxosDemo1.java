package cn.com.kun.foo.algorithm.paxos.demo1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 五个球员，都有机会成为联盟主席，机会均等
 *
 * author:xuyaokun_kzx
 * date:2022/3/9
 * desc:
*/
public class PaxosDemo1 {

    public static AtomicBoolean isFinished = new AtomicBoolean(false);

    public static List<NbaPlayer> nbaPlayerList = new ArrayList<>();

    public static AtomicInteger COMMON_NUMBER = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        //五个球员，同时是提议者、接受者、学习者
        nbaPlayerList.add(new NbaPlayer("LBJ"));
        nbaPlayerList.add(new NbaPlayer("Curry"));
        nbaPlayerList.add(new NbaPlayer("MJ"));
        nbaPlayerList.add(new NbaPlayer("AD"));
        nbaPlayerList.add(new NbaPlayer("KD"));

        NbaPlayerListHolder.init(nbaPlayerList);

        //开启五个线程，五个球员开始试图达成共识
        nbaPlayerList.forEach(nbaPlayer -> {
            new Thread(()->{

                while (true){

                    //第一阶段
                    //开始向接受者发出perpare请求
                    if (!nbaPlayer.isWaitReponse()){
                        sendPerpareReq(nbaPlayer.getName(), nbaPlayerList);
                        nbaPlayer.setWaitReponse(true);
                    }else {
                        //在等待响应中
                        //检查响应结果
                        if (!nbaPlayer.isHasSendAccept()){
                            checkReponse(nbaPlayer);
                        }
                    }


                    //判断是否议案已决出，假如是那就退出线程，结束提议
                    if (isFinished.get()){
                        //
                        System.out.println("提异已决出，结束线程");
                        break;
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    System.out.println(String.format("球员%s执行中", nbaPlayer.getName()));
                }
            }).start();
        });

//        Thread.sleep(5000);
//        isFinished.set(true);

    }

    private static void checkReponse(NbaPlayer nbaPlayer) {

        //第二阶段
        //检查所有响应结果
        nbaPlayer.checkResponse();

    }

    private static void sendPerpareReq(String name, List<NbaPlayer> nbaPlayerList) {

        //创建一个提议编号,全局递增
        int number = COMMON_NUMBER.incrementAndGet();
        //构建PerpareRequest请求
        PerpareRequest perpareRequest = new PerpareRequest();
        perpareRequest.setNumber(number);
        perpareRequest.setName(name);
        for (NbaPlayer nbaPlayer : nbaPlayerList){
            //发送perpare请求
//            if (name.equals(nbaPlayer.getName())){
//                continue;
//            }
            nbaPlayer.receivePerpare(perpareRequest);
        }
        System.out.println(String.format("发送Perpare请求结束，球员：%s", name));

    }


}
