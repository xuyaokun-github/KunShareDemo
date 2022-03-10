package cn.com.kun.foo.algorithm.paxos.demo1;

import cn.com.kun.common.utils.JacksonUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 五个球员，都有机会成为联盟主席，机会均等
 * 例子中，我为了简单，用了一个全局的编号发号器，但实际中不会这样做的，因为这个发号器相当于是外部依赖
 * 达成共识的过程是不能对外部产生依赖的，正确的编号发号器可以参考（递增整数+serverId的方式）实现 TODO 后面再优化
 *
 * author:xuyaokun_kzx
 * date:2022/3/9
 * desc:
*/
public class PaxosDemoForNba {

    public static AtomicBoolean ISFINISHED = new AtomicBoolean(false);

    public static HashSet<String> WINNER_SET = new HashSet();

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

                    if (!nbaPlayer.isWaitReponse()){
                        //第一阶段
                        //开始向接受者发出perpare请求
                        nbaPlayer.sendPerpareReq(nbaPlayer.getName(), nbaPlayerList);
                        nbaPlayer.setWaitReponse(true);
                    }else {
                        //是否已经发送Accept请求
                        if (!nbaPlayer.isHasSendAccept()){
                            //检查响应结果
                            nbaPlayer.checkResponse();
                        }
                    }


                    //判断是否议案已决出，假如是那就退出线程，结束提议
                    if (ISFINISHED.get()){
                        //
                        System.out.println("提异已决出，即将结束线程");
                        break;
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    System.out.println(String.format("球员%s执行中", nbaPlayer.getName()));
                }

                System.out.println(String.format("球员:%s 宣布 最终胜者为：%s", nbaPlayer.getName(), JacksonUtils.toJSONString(WINNER_SET)));

            }, "NBA-Thread-" + nbaPlayer.getName()).start();
        });

//        Thread.sleep(5000);
//        isFinished.set(true);
    }

}
