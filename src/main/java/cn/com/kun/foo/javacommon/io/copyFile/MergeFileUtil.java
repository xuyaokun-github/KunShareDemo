package cn.com.kun.foo.javacommon.io.copyFile;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.channels.FileChannel;

public class MergeFileUtil {

    public static void main(String[] args) {

        File file = new File("D:\\home\\readFile");
        String[] fpaths = new String[10];

        int index = 0;
        for (File file1 : file.listFiles()){
            if (file1.getAbsolutePath().endsWith("txt")){
                fpaths[index] = file1.getAbsolutePath();
                index++;
            }
        }

        MergeFileUtil.mergeFiles2(fpaths, "D:\\home\\readFile\\ReadFileExample.txt");
    }

    public static boolean mergeFiles(String[] fpaths, String resultPath) {

        if (fpaths == null || fpaths.length < 1 || StringUtils.isEmpty(resultPath)) {
            return false;
        }
        if (fpaths.length == 1) {
            return new File(fpaths[0]).renameTo(new File(resultPath));
        }
        File[] files = new File[fpaths.length];
        for (int i = 0; i < fpaths.length; i++) {
            files[i] = new File(fpaths[i]);
            if (StringUtils.isEmpty(fpaths[i]) || !files[i].exists() || !files[i].isFile()) {
                return false;
            }
        }
        File resultFile = new File(resultPath);
        try {
            int bufSize = 1024;
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(resultFile));
            byte[] buffer = new byte[bufSize];
            for (int i = 0; i < fpaths.length; i++) {
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(files[i]));
                int readcount;
                while ((readcount = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, readcount);
                }
                inputStream.close();
            }
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        for (int i = 0; i < fpaths.length; i++) {
            files[i].delete();
        }
        return true;
    }

    public static boolean mergeFiles2(String[] fpaths, String resultPath) {

        if (fpaths == null || fpaths.length < 1 || StringUtils.isEmpty(resultPath)) {
            return false;
        }

        if (fpaths.length == 1) {
            return new File(fpaths[0]).renameTo(new File(resultPath));
        }

        File[] files = new File[fpaths.length];

        for (int i = 0; i < fpaths.length; i++) {

            files[i] = new File(fpaths[i]);
            if (StringUtils.isEmpty(fpaths[i]) || !files[i].exists() || !files[i].isFile()) {
                return false;
            }
        }

        File resultFile = new File(resultPath);
        try {
            FileChannel resultFileChannel = new FileOutputStream(resultFile, true).getChannel();
            for (int i = 0; i < fpaths.length; i++) {
                FileChannel blk = new FileInputStream(files[i]).getChannel();
                resultFileChannel.transferFrom(blk, resultFileChannel.size(), blk.size());
                blk.close();
            }
            resultFileChannel.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


        for (int i = 0; i < fpaths.length; i++) {
            files[i].delete();
        }

        return true;
    }

}
