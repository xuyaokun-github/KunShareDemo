package cn.com.kun.springframework.batch.deadlockdemo;

public interface ICustomJobRepository {

    void show();

    void insertInstance(Object[] parameters);
}
