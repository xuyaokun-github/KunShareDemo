package cn.com.kun.apache.flink.demo1;

import cn.com.kun.apache.flink.FlinkDemoJob;
import cn.com.kun.common.utils.JacksonUtils;

public class MyFlinkDemoJob implements FlinkDemoJob {

    @Override
    public void run(String[] args) {
        System.out.println("MyFlinkDemoJob running.............");
        System.out.println(JacksonUtils.toJSONString(args));
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
