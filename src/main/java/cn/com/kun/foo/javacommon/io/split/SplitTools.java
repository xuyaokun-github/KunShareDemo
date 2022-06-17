package cn.com.kun.foo.javacommon.io.split;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SplitTools {

    public static void main(String[] args) throws IOException {
        // "E:\\eutranrelation.csv";
        String bigFilePath = "D:\\home\\kunghsu\\big-file-test\\big-file.txt";
        // "E:\\eutranrelation-%s.csv";
        String splitFileParttern = "D:\\home\\kunghsu\\big-file-test\\big-file-%s.txt";
        // 32
        int splitFileCount = 50 * 10000;

        /*
        Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at java.util.Arrays.copyOf(Arrays.java:3332)
	at java.lang.AbstractStringBuilder.ensureCapacityInternal(AbstractStringBuilder.java:124)
	at java.lang.AbstractStringBuilder.append(AbstractStringBuilder.java:596)
	at java.lang.StringBuilder.append(StringBuilder.java:196)
	at java.io.BufferedReader.readLine(BufferedReader.java:370)
	at java.io.BufferedReader.readLine(BufferedReader.java:389)
	at cn.com.kun.foo.javacommon.io.split.SplitTools.split(SplitTools.java:26)
	at cn.com.kun.foo.javacommon.io.split.SplitTools.main(SplitTools.java:19)

         */
        split(bigFilePath, splitFileParttern, splitFileCount);
    }

    private static void split(String bigFilePath, String splitFileParttern, int splitFileCount) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(bigFilePath));
        //这个不支持大文件，假如文件很大，这里直接报错了
        String header = reader.readLine();
        int totalLine = 0;
        if (header != null) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                totalLine++;
            }
        }

        reader.close();

        int splitFileTotalLines = totalLine / splitFileCount;
        int generateFileIdx = 0;

        BufferedWriter bufferedWriter = null;
        reader = new BufferedReader(new FileReader(bigFilePath));
        header = reader.readLine();
        int currentIdx = 0;
        if (header != null) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (currentIdx == 0) {
                    String filePath = String.format(splitFileParttern,
                            String.valueOf(generateFileIdx));
                    bufferedWriter = new BufferedWriter(
                            new FileWriter(filePath));
                }

                bufferedWriter.write(line + "\r\n");

                currentIdx++;

                if (currentIdx == splitFileTotalLines) {
                    generateFileIdx++;
                    if (generateFileIdx != splitFileCount) {
                        currentIdx = 0;
                        bufferedWriter.flush();
                        bufferedWriter.close();
                    }
                }
            }

            if (bufferedWriter != null) {
                bufferedWriter.flush();
                bufferedWriter.close();
            }
        }

        reader.close();

        System.out.println("The total number of documents is:" + totalLine);
    }
}
