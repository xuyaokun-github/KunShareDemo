package cn.com.kun.foo.javacommon.io.copyFile;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CopyFileTestDemo {

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

//        main1();

        CopyFileUtil.copy("D:\\home\\readFile\\ReadFileExample.txt", 10);

    }

    private static void main1() throws IOException {

        String inputName = "D:\\home\\kunghsu\\wh-test\\" + "my.txt";
        File inputFile = new File(inputName);
//        FileUtils.write(inputFile, "kunghsu");

        for (int i = 0; i < 10; i++) {
            String outputName = "D:\\home\\kunghsu\\wh-test\\" + "my" + i + ".txt";
            File outputFile = new File(outputName);
            FileUtils.copyFile(inputFile, outputFile);
        }
    }

}
