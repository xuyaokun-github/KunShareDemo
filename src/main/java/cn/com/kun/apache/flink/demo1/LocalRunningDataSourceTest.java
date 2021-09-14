package cn.com.kun.apache.flink.demo1;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

/**
 * 当前内容主要为测试和使用当前的Flink，主要为本地方式运行这个Flink
 * 引入flink jar包后，直接run起main方法就可以运行
 *
 * 读取本地文件作为数据源
 *
 * 假如我本地启动了个flink服务端程序，要将该类打包成jar包运行，该怎么通过命令打包？
 *
 * author:xuyaokun_kzx
 * date:2021/9/10
 * desc:
*/
public class LocalRunningDataSourceTest {

    public static void main(String[] args) {

        // 采用本地模式（createLocalEnvironment表示用本地环境运行该流处理程序）
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        // 从本地的webUI方式提供createLocalEnvironmentWithWebUI
        // Configuration conf=new Configuration();
        /* conf.set(option, value)*/
        // StreamExecutionEnvironment env =
        // StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);// 采用默认配置

        String filePath = "D:\\Ideaworkspaces\\KunShareDemo2\\src\\main\\resources\\demoData\\flink\\abc.txt";
        System.out.println("filePath:" + filePath);

        // 设置数据来源为当前的文本文件：
        DataStreamSource<String> readTextFile = env.readTextFile(filePath);

        SingleOutputStreamOperator<Object> singleOutputStreamOperator = readTextFile.process(new ProcessFunction<String, Object>() {
            @Override
            public void processElement(String s, Context context, Collector<Object> collector) throws Exception {
                System.out.println("执行processElement，s:" + s);
                if (s.contains("李四")){
                    //假如等于李四，就进行收集
                    //Collector有什么用？
                    collector.collect("new" + s);
                }
            }
        });
        // 直接读取文件为文本类型，最后进行print操作
        readTextFile.print();

        singleOutputStreamOperator.print();

        try {
            /*
                最后开始执行
                参数名表示这次执行实例，标识这次执行过程
             */
            JobExecutionResult result = env.execute("LocalRunningDataSourceTest");
            if(result.isJobExecutionResult()) {
                System.out.println("LocalRunningDataSourceTest执行完毕......");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
