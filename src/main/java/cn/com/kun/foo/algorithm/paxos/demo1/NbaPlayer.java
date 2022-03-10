package cn.com.kun.foo.algorithm.paxos.demo1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.com.kun.foo.algorithm.paxos.demo1.PaxosDemoForNba.*;

/**
 * Paxos算法体现了无中心化的思想
 * 在没有上帝视角的情况下，达成共识
 *
 * author:xuyaokun_kzx
 * date:2022/3/10
 * desc:
*/
public class NbaPlayer {

    private String name;

    /**
     * 最大的编号
     */
    private AtomicInteger lastNumber = null;

    /**
     * 是否正在等待返回中
     */
    private boolean waitReponse;

    private ConcurrentSkipListSet<PerpareResponse> responseSet = new ConcurrentSkipListSet();

//    private HashSet<PerpareResponse> responseSet = new HashSet();

    private boolean hasSendAccept;

    private AtomicInteger passNumber = new AtomicInteger(0);

    private Object receiveAcceptLock = new Object();

    private Object receivePerpareLock = new Object();

    private Object receiveLearnCountLock = new Object();

    private Map<String, AtomicInteger> passNumberCountMap = new ConcurrentHashMap<>();

    public NbaPlayer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWaitReponse() {
        return waitReponse;
    }

    public void setWaitReponse(boolean waitReponse) {
        this.waitReponse = waitReponse;
    }

    public AtomicInteger getLastNumber() {
        return lastNumber;
    }

    public void setLastNumber(AtomicInteger lastNumber) {
        this.lastNumber = lastNumber;
    }

    public ConcurrentSkipListSet getResponseSet() {
        return responseSet;
    }

    public void setResponseSet(ConcurrentSkipListSet responseSet) {
        this.responseSet = responseSet;
    }


//    public HashSet<PerpareResponse> getResponseSet() {
//        return responseSet;
//    }

//    public void setResponseSet(HashSet<PerpareResponse> responseSet) {
//        this.responseSet = responseSet;
//    }

    public boolean isHasSendAccept() {
        return hasSendAccept;
    }

    public void setHasSendAccept(boolean hasSendAccept) {
        this.hasSendAccept = hasSendAccept;
    }

    public void receivePerpare(PerpareRequest perpareRequest) {

        synchronized (receivePerpareLock){
            if (lastNumber == null){
                lastNumber = new AtomicInteger(perpareRequest.getNumber());
                //返回响应
                returnResponse(perpareRequest, "receive", lastNumber.get());
                return;
            }

            //接收perpare请求
            if (perpareRequest.getNumber() >= lastNumber.get()){
                lastNumber.set(perpareRequest.getNumber());
                returnResponse(perpareRequest, "receive", lastNumber.get());
            }else {
                //
                returnResponse(perpareRequest, "reject", lastNumber.get());
            }
        }

    }

    private void returnResponse(PerpareRequest perpareRequest, String status, int number) {

        //模拟，找到节点，进行调用
        NbaPlayer nbaPlayer = NbaPlayerListHolder.find(perpareRequest.getName());

        //返回响应
        PerpareResponse perpareResponse = new PerpareResponse();
        perpareResponse.setStatus(status);
        perpareResponse.setNumber(number);
        if (status.equals("receive")){
            System.out.println(String.format("接收者[%s] 返回 响应状态[%s] 编号：%s 给提议者[%s]",
                    this.getName(), status, number, perpareRequest.getName()));
        }
        nbaPlayer.receiveResponse(perpareResponse);

    }

    private void receiveResponse(PerpareResponse perpareResponse) {

        //将响应放到集合中
        responseSet.add(perpareResponse);
    }


    public synchronized void checkResponse() {

        if (hasSendAccept){
            return;
        }

        int quorum = Math.floorDiv(NbaPlayerListHolder.size(), 2) + 1;

        //遍历队列
        List<PerpareResponse> responseList = new ArrayList<>();
        Iterator iterator = responseSet.iterator();
        while (iterator.hasNext()){
            responseList.add((PerpareResponse) iterator.next());
        }

        AtomicInteger receiveNumber = new AtomicInteger();
        AtomicInteger rejectNumber = new AtomicInteger();
        AtomicInteger tempNumber = new AtomicInteger();
        responseList.forEach(obj->{
            PerpareResponse perpareResponse = (PerpareResponse) obj;
            if (perpareResponse.getStatus().equals("receive")){
                //计数
                receiveNumber.getAndIncrement();

                if (perpareResponse.getNumber() > tempNumber.get()){
                    tempNumber.set(perpareResponse.getNumber());
                }
            }else {
                rejectNumber.getAndIncrement();
            }
        });

        if (receiveNumber.get() >= quorum){
            //大于大多数，发起accept请求
            //发送accept请求，然后修改标志，已完事
            this.hasSendAccept = true;

            //发送Accept请求
            sendAcceptReq(tempNumber.get());
            return;
        }

        if (rejectNumber.get() >= quorum){
            //否则，则重新发起选举
            //清空
            responseSet.clear();
            this.waitReponse = false;
            hasSendAccept = false;
        }

    }

    private void sendAcceptReq(int number) {

        List<NbaPlayer> nbaPlayerList = NbaPlayerListHolder.getAll();

        AcceptRequest acceptRequest = new AcceptRequest();
        acceptRequest.setName(this.name);
        acceptRequest.setNumber(number);

        //发送accept请求
        System.out.println(String.format("--------------------------------------开始发送accept请求，球员名称：%s  发送的编号：%s",
                this.name, number));

        for (NbaPlayer nbaPlayer : nbaPlayerList){
//            if (name.equals(nbaPlayer.getName())){
//                continue;
//            }
            //模拟一点耗时
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(3) * 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //发送perpare请求
            nbaPlayer.receiveAccept(acceptRequest);
        }

    }

    private void receiveAccept(AcceptRequest acceptRequest) {

        synchronized(receiveAcceptLock){
            //比较本地，假如本地存的编号和传入的编号相等，则说明提案通过
            if (acceptRequest.getNumber() == this.lastNumber.get()){
                //提案通过
                System.out.println(String.format("编号比较通过，当前接收者：%s所拥有编号：%s 传入编号：%s 传入者：%s  当前passnumber:%s",
                        this.getName(), this.lastNumber, acceptRequest.getNumber(), acceptRequest.getName(), passNumber.get()));

                //该编号对应的提案通过后，应该发通知告诉learner
                //而且是告诉所有learner，由learner来做统计！而不是在接收者内部做统计，这是不对的

                //这里是给所有learner发送统计请求，因为learner将会将最终通过的议案进行广播
                sendLearnCountReq(acceptRequest.getNumber(), acceptRequest.getName());

            }else {
                //编号不符合，拒绝
                System.out.println(String.format("编号比较失败，当前接收者：%s所拥有编号：%s 传入编号：%s 传入者：%s",
                        this.getName(), this.lastNumber, acceptRequest.getNumber(), acceptRequest.getName()));
            }

        }

    }

    private void sendLearnCountReq(int number, String name) {

        List<NbaPlayer> nbaPlayerList = NbaPlayerListHolder.getAll();

        //构建通知请求
        LearnCountRequest learnCountRequest = new LearnCountRequest();
        learnCountRequest.setNumber(number);
        learnCountRequest.setName(name);

        //遍历发送learner count请求
        for (NbaPlayer nbaPlayer : nbaPlayerList){
//            if (name.equals(nbaPlayer.getName())){
//                continue;
//            }
            nbaPlayer.receiveLearnCount(learnCountRequest);
        }

    }

    private void receiveLearnCount(LearnCountRequest learnCountRequest) {

        synchronized (receiveLearnCountLock){
            //找到该编号对应的统计器
            if (passNumberCountMap.get(String.valueOf(learnCountRequest.getNumber())) == null){
                passNumberCountMap.put(String.valueOf(learnCountRequest.getNumber()), new AtomicInteger(0));
            }
            AtomicInteger count = passNumberCountMap.get(String.valueOf(learnCountRequest.getNumber()));
            //加1
            count.incrementAndGet();

            //进行统计
            int quorum = Math.floorDiv(NbaPlayerListHolder.size(), 2) + 1;
            if (count.get() >= quorum){
                //大多数，则发出通知，告知learner
                System.out.println(String.format("提案已决出,胜者：%s 当前的lastNumber：%s", learnCountRequest.getName(), this.lastNumber.get()));
                WINNER_SET.add(learnCountRequest.getName());
                ISFINISHED.set(true);
            }
        }

    }


    public void sendPerpareReq(String name, List<NbaPlayer> nbaPlayerList) {

        //创建一个提议编号,全局递增
        int number = COMMON_NUMBER.incrementAndGet();
        //构建PerpareRequest请求
        PerpareRequest perpareRequest = new PerpareRequest();
        perpareRequest.setNumber(number);
        perpareRequest.setName(name);
        //遍历发送Perpare请求
        for (NbaPlayer nbaPlayer : nbaPlayerList){
//            if (name.equals(nbaPlayer.getName())){
//                continue;
//            }

            //模拟一点耗时
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(3) * 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            nbaPlayer.receivePerpare(perpareRequest);
        }
        System.out.println(String.format("球员：%s 发送Perpare请求结束，", name));
    }
}
