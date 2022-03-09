package cn.com.kun.foo.algorithm.paxos.demo1;

public class PerpareResponse implements Comparable{

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

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
