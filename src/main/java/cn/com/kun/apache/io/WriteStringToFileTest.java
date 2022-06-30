package cn.com.kun.apache.io;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class WriteStringToFileTest {

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        main1();
    }

    private static void main1() throws IOException {

        String inputName = "D:\\home\\kunghsu\\big-file-test\\batchDemoOne-small-file.txt";
        File inputFile = new File(inputName);
        String str = "888888|c5286930-a450-4c21-978a-3f984e8f4ec4" +
                "|888888";
        //7个字节
        int byteCount = str.getBytes("UTF-8").length;
        System.out.println(byteCount);

        int writeCount = (2*1024) / byteCount;
        /*
            1G = 1024M = 1024 * 1024 K = 1024*1024*1024 字节
         */
        for (int i = 0; i < writeCount; i++) {
            //注意这个是覆盖，不是追加
//            FileUtils.writeStringToFile(inputFile, "kunghsu", "UTF-8");
            //追加写入
            FileUtils.writeStringToFile(inputFile, str, "UTF-8", true);
            System.out.println(i);
        }
        System.out.println("写入结束");
    }

}
