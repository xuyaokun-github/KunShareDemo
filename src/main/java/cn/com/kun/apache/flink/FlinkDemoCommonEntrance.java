package cn.com.kun.apache.flink;

import org.apache.commons.lang3.StringUtils;

/**
 * Flink demo统一入口
 *
 * author:xuyaokun_kzx
 * date:2021/9/9
 * desc:
*/
public class FlinkDemoCommonEntrance {

    /**
     * flinkClassName=cn.com.kun.apache.flink.demo1.LocalRunningDataSourceTestJob
     * @param args
     */
    public static void main(String[] args) {

        //通过参数来控制，要运行哪一个job
        if (args.length == 0){
            System.out.println("参数长度为0，运行结束");
            return;
        }

        System.out.println("接收到的参数如下：");
        for (String arg : args){
            System.out.println(arg);
        }

        //第一个参数，要运行的job的类名
        //例子：cn.com.kun.apache.flink.demo1.LocalRunningDataSourceTestJob
        String className = getFlinkClassName(args);

        if (StringUtils.isEmpty(className)){
            System.out.println("flinkClassName为空，运行结束");
            return;
        }
        //反射创建实例
        try {
            FlinkDemoJob flinkDemoJob = (FlinkDemoJob) Class.forName(className).newInstance();
            flinkDemoJob.run(args);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    private static String getFlinkClassName(String[] args) {
        for (String arg : args){
            if (arg.startsWith("flinkClassName=")){
                String str[] = arg.split("=");
                return str[1];
            }
        }
        return null;
    }


}
