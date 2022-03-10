package cn.com.kun.foo.algorithm.paxos.demo1;

import java.util.UUID;

public class PerpareResponse implements Comparable{

    private String uuid = UUID.randomUUID().toString();

    private String status;

    private int number;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int compareTo(Object o) {

        //如何比较PerpareResponse
        return uuid.compareTo(((PerpareResponse)o).getUuid());
    }
}
