package cn.com.kun.apache.flink.tableapi.hive;

/**
 * hive造数
 * author:xuyaokun_kzx
 * date:2022/1/6
 * desc:
*/
public class MakeTestData {

    public static void main(String[] args) {

        StringBuilder builder = new StringBuilder();
        builder.append(String.valueOf(11));
        builder.append("\001");
        builder.append("abc");
        builder.append("\001");
        builder.append("jkl");
        builder.append("\n");
        builder.append(String.valueOf(11));
        builder.append("\001");
        builder.append("abc");
        builder.append("\001");
        builder.append("jkl");
        builder.append("\n");
        System.out.println(builder.toString());
    }
}
