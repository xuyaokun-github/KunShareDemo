package cn.com.kun.foo.javacommon.queue;

import cn.com.kun.bean.entity.Student;
import cn.com.kun.common.utils.JacksonUtils;

import java.util.concurrent.ConcurrentLinkedQueue;

public class TestQueueRemoveIf {

    private static ConcurrentLinkedQueue taskQueue = new ConcurrentLinkedQueue();

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            Student student = new Student();
            student.setId((long) i);
            taskQueue.offer(student);
        }

        //JDK1.8的api
        taskQueue.removeIf(obj -> 5 == ((Student)obj).getId());

        //
        taskQueue.iterator().forEachRemaining(obj->{
            System.out.println(JacksonUtils.toJSONString(obj));
        });

    }


}
