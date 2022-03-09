package cn.com.kun.foo.algorithm.paxos.demo1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.com.kun.foo.algorithm.paxos.demo1.PaxosDemo1.isFinished;

public class NbaPlayer {

    private String name;

    private AtomicInteger lastNumber = null;

    /**
     * 是否正在等待返回中
     */
    private boolean waitReponse;

//    private ConcurrentSkipListSet<PerpareResponse> responseSet = new ConcurrentSkipListSet();

    private HashSet<PerpareResponse> responseSet = new HashSet();

    private boolean hasSendAccept;

    private AtomicInteger passNumber = new AtomicInteger(0);

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

//    public ConcurrentSkipListSet getResponseSet() {
//        return responseSet;
//    }
//
//    public void setResponseSet(ConcurrentSkipListSet responseSet) {
//        this.responseSet = responseSet;
//    }


    public HashSet<PerpareResponse> getResponseSet() {
        return responseSet;
    }

    public void setResponseSet(HashSet<PerpareResponse> responseSet) {
        this.responseSet = responseSet;
    }

    public boolean isHasSendAccept() {
        return hasSendAccept;
    }

    public void setHasSendAccept(boolean hasSendAccept) {
        this.hasSendAccept = hasSendAccept;
    }

    public synchronized void receivePerpare(PerpareRequest perpareRequest) {

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

    private void returnResponse(PerpareRequest perpareRequest, String status, int number) {

        //模拟，找到节点，进行调用
        NbaPlayer nbaPlayer = NbaPlayerListHolder.find(perpareRequest.getName());

        //返回响应
        PerpareResponse perpareResponse = new PerpareResponse();
        perpareResponse.setStatus(status);
        perpareResponse.setNumber(number);
        if (status.equals("receive")){
            System.out.println(String.format("接收者[%s]返回响应状态[%s]给提议者[%s],当前number：%s",
                    this.getName(), status, perpareRequest.getName(), number));
        }
        nbaPlayer.receiveResponse(perpareResponse);

    }

    private void receiveResponse(PerpareResponse perpareResponse) {

        //将响应放到集合中
        responseSet.add(perpareResponse);
    }


    public void checkResponse() {

        if (hasSendAccept){
            return;
        }

        int quorum = Math.floorDiv(NbaPlayerListHolder.size(), 2) + 1;

        //遍历队列
        AtomicInteger receiveNumber = new AtomicInteger();
        AtomicInteger rejectNumber = new AtomicInteger();

        List<PerpareResponse> responseList = new ArrayList<>();
        Iterator iterator = responseSet.iterator();
        while (iterator.hasNext()){
            responseList.add((PerpareResponse) iterator.next());
        }

        responseList.forEach(obj->{
            PerpareResponse perpareResponse = (PerpareResponse) obj;
            if (perpareResponse.getStatus().equals("receive")){
                //计数
                receiveNumber.getAndIncrement();
            }else {
                rejectNumber.getAndIncrement();
            }
        });

        if (receiveNumber.get() >= quorum){
            //大于大多数，发起accept请求
            //发送accept请求，然后修改标志，已完事
            this.hasSendAccept = true;
            //发送accept请求
            System.out.println("--------------------------------------开始发送accept请求，球员名称：" + this.name);

            //
            sendAcceptReq();


            return;
        }

        if (rejectNumber.get() >= quorum){
            //否则，则重新发起选举
            //清空
            responseSet.clear();
//            this.waitReponse = false;
            hasSendAccept = false;
        }

    }

    private void sendAcceptReq() {

        List<NbaPlayer> nbaPlayerList = NbaPlayerListHolder.getAll();

        AcceptRequest acceptRequest = new AcceptRequest();
        acceptRequest.setName(this.name);
        acceptRequest.setNumber(this.lastNumber.get());
        for (NbaPlayer nbaPlayer : nbaPlayerList){
            //发送perpare请求
//            if (name.equals(nbaPlayer.getName())){
//                continue;
//            }
            nbaPlayer.receiveAccept(acceptRequest);
        }

    }

    private void receiveAccept(AcceptRequest acceptRequest) {

        //比较本地，假如本地存的编号和传入的编号相等，则说明提案通过
        if (acceptRequest.getNumber() == this.lastNumber.get()){
            //提案通过，加1
            passNumber.incrementAndGet();
        }
        int quorum = Math.floorDiv(NbaPlayerListHolder.size(), 2) + 1;
        if (passNumber.get() >= quorum){
            //大多数，则发出通知，告知learner
            System.out.println(String.format("提案已决出,胜者：%s", acceptRequest.getName()));
        }
    }


}
