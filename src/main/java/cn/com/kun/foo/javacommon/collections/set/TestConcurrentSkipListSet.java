package cn.com.kun.foo.javacommon.collections.set;

import cn.com.kun.foo.algorithm.paxos.demo1.PerpareResponse;

import java.util.concurrent.ConcurrentSkipListSet;

public class TestConcurrentSkipListSet {

    public static void main(String[] args) {

        ConcurrentSkipListSet<PerpareResponse> responseSet = new ConcurrentSkipListSet();

        for (int i = 0; i < 5; i++) {
            PerpareResponse perpareResponse = new PerpareResponse();
            responseSet.add(perpareResponse);
        }

        System.out.println(responseSet.size());
    }
}
