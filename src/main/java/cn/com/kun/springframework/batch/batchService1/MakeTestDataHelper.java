package cn.com.kun.springframework.batch.batchService1;

import java.util.UUID;

public class MakeTestDataHelper {

    public static void main(String[] args) {

        //111|aaa|11
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 20; i++) {

            builder.append(i + "|" + UUID.randomUUID().toString() + "|" + i);
            builder.append("\n");
        }
        System.out.println(builder.toString());


        //生成一个大文件

    }
}
