package cn.com.kun.springframework.batch.batchService1;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MakeTestDataHelper {

    public static void main(String[] args) throws IOException {

        //生成一个小文件
        //111|aaa|11
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < 20; i++) {
//
//            builder.append(i + "|" + UUID.randomUUID().toString() + "|" + i);
//            builder.append("\n");
//        }
//        System.out.println(builder.toString());


//        buildMiddleFile();

        //生成一个大文件
        buildBigFile();
    }

    private static void buildMiddleFile() throws IOException {

        String inputName = "D:\\home\\kunghsu\\big-file-test\\batchDemoOne-middle-file.txt";
        File inputFile = new File(inputName);
//        FileUtils.write(inputFile, "kunghsu");

        String str = "888888|c5286930-a450-4c21-978a-3f984e8f4ec4-" +
                "c5286930-a450-4c21-978a-3f984e8f4ec4" +
                "c5286930-a450-4c21-978a-3f984e8f4ec4" +
                "c5286930-a450-4c21-978a-3f984e8f4ec4" +
                "c5286930-a450-4c21-978a-3f984e8f4ec4" +
                "c5286930-a450-4c21-978a-3f984e8f4ec4" +
                "c5286930-a450-4c21-978a-3f984e8f4ec4" +
                "c5286930-a450-4c21-978a-3f984e8f4ec4" +
                "c5286930-a450-4c21-978a-3f984e8f4ec4" +
                "|888888";
        //7个字节
        int byteCount = str.getBytes("UTF-8").length;
        System.out.println(byteCount);

        int writeCount = (5 * 1024*1024) / byteCount;
        /*
            1G = 1024M = 1024 * 1024 K = 1024*1024*1024 字节
         */
        for (int i = 0; i < writeCount; i++) {
            //注意这个是覆盖，不是追加
//            FileUtils.writeStringToFile(inputFile, "kunghsu", "UTF-8");
            //追加写入
            FileUtils.writeStringToFile(inputFile, str + "\n", "UTF-8", true);
            System.out.println(i);
        }
        System.out.println("写入结束");
    }

    private static void buildBigFile() throws IOException {

//        String inputName = "D:\\home\\kunghsu\\big-file-test\\batchDemoOne-big-file.txt";
        String inputName = "D:\\home\\kunghsu\\big-file-test\\batchDemoOne-big-file-oneline-1m.txt";
        File inputFile = new File(inputName);
//        FileUtils.write(inputFile, "kunghsu");

//        String tempString = buildTempString(5595);
        String tempString = buildTempString(1024);
        //5595个字节
        String str = "888888|" + tempString + "|888888";

        int byteCount = str.getBytes("UTF-8").length;
        System.out.println(byteCount);

        int writeCount = (1024*1024*1024) / byteCount;
        /*
            1G = 1024M = 1024 * 1024 K = 1024*1024*1024 字节
         */
        for (int i = 0; i < writeCount; i++) {
            //注意这个是覆盖，不是追加
//            FileUtils.writeStringToFile(inputFile, "kunghsu", "UTF-8");
            //追加写入(必须添加换行符)
            FileUtils.writeStringToFile(inputFile, str + "\n", "UTF-8", true);
            System.out.println(i);
        }
        System.out.println("写入结束");
    }

    private static String buildTempString(int count) throws UnsupportedEncodingException {

        //
        String str = "kunghsu";
        int length = str.getBytes("UTF-8").length;

        StringBuilder builder = new StringBuilder();
        int loopCount = 0;
        if (count < length){
            loopCount = 1;
        }else {
            loopCount = count / length;
        }
        for (int i = 0; i < loopCount; i++) {
            builder.append(str);
        }
        return builder.toString();
    }


}
