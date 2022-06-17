package cn.com.kun.foo.javacommon.io.split;


import org.junit.Test;

import java.io.*;


public class FileSplit {

    private static String fileName = "D:\\home\\kunghsu\\big-file-test\\big-file.txt";
    private static String outFile = "D:\\home\\kunghsu\\big-file-test\\split\\big-file.txt";
    private static String outDir = "D:\\home\\kunghsu\\big-file-test\\split";


    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        largeFileSplit(fileName, outDir);
        long endTime = System.currentTimeMillis();
        System.out.println("拆文件成功，共耗时：" + (float) (endTime - startTime) / 1000 + "秒");

    }

    /**
     * 2G以上大文件读取
     *
     * @param inputFile, outputFile
     */
    void largeFileIO(String inputFile, String outputFile) {
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(inputFile)));
            // 10M缓存
            BufferedReader in = new BufferedReader(new InputStreamReader(bis, "utf-8"), 10 * 1024 * 1024);
            FileWriter fw = new FileWriter(outputFile);
            while (in.ready()) {
                String line = in.readLine();
                if (line.startsWith("INSERT")) {
                    fw.append(line + "\r");
                }
            }
            in.close();
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 大文件拆分
     *
     * @param inputFile, outputFile
     * @return void
     * @author wang****
     * @date: 2021/4/8 16:19
     */
    static void largeFileSplit(String inputFile, String outputDir) {
        int j = 0;
        try {
            File out = new File(outputDir);
            if (!out.exists()) {
                out.mkdirs();
            }
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(inputFile)));
            // 20M缓存
            BufferedReader in = new BufferedReader(new InputStreamReader(bis, "utf-8"), 20 * 1024 * 1024);
            int i = 0;
            while (in.ready()) {
                long startTime = System.currentTimeMillis();
                ++i;
                FileWriter fw = new FileWriter(outputDir + i + ".sql");
                String line = null;
                //50万行一个文件拆分
                for (long lineCounter = 0; lineCounter < 500000 && (line = in.readLine()) != null; ++lineCounter) {
                    if (line.startsWith("INSERT INTO `cur_rec` VALUES")) {
                        fw.append(line + "\r");
                        ++j;
                    }
                }
                fw.flush();
                fw.close();
                long endTime = System.currentTimeMillis();
                System.out.println("第" + i + "个文件拆分成功，耗时：" + (float) (endTime - startTime) / 1000 + "秒");
            }
            in.close();
        } catch (IOException ex) {
            System.out.println("第" + j + "行数据拆分异常\r" + ex);
            ex.printStackTrace();
        }
    }

    /**
     * 读取测试
     */
    @Test
    public void test1() {
        largeFileIO(fileName, outFile);
    }

    /**
     * 大文件拆分
     */
    @Test
    public void test2() {
        long startTime = System.currentTimeMillis();
        largeFileSplit(fileName, outDir);
        long endTime = System.currentTimeMillis();
        System.out.println("拆文件成功，共耗时：" + (float) (endTime - startTime) / 1000 + "秒");
    }
}


