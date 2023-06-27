package cn.com.kun.foo.javacommon.io.copyFile;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CopyFileUtil {

    /**
     *
     * @param sourceFilePath
     * @param num
     * @throws IOException
     */
    public static void copy(String sourceFilePath, int num) throws IOException {

        File inputFile = new File(sourceFilePath);
//        FileUtils.write(inputFile, "kunghsu");

        int index = sourceFilePath.indexOf(".");
        for (int i = 0; i < num; i++) {
            String outputName = "";
            if (sourceFilePath.indexOf(".") > 0){
                outputName = sourceFilePath.substring(0, index) + i + sourceFilePath.substring(index);
            }else {
                outputName = outputName + i;
            }
            File outputFile = new File(outputName);
            FileUtils.copyFile(inputFile, outputFile);
        }

    }
}
