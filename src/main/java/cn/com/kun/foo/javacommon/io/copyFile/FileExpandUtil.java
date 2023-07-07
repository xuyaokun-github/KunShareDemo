package cn.com.kun.foo.javacommon.io.copyFile;

import java.io.IOException;
import java.util.List;

/**
 * 文件扩大工具类
 *
 * 注意，文件最后一行要敲一下回车，让它换行，否则第二个文件的首行和第一个文件的尾行会接在一起并为一行
 * author:xuyaokun_kzx
 * date:2023/7/6
 * desc:
*/
public class FileExpandUtil {

    public static void main(String[] args) throws IOException {

        String targetFilePath = "D:\\home\\readFile\\ReadFileExample.txt";
        String newTargetFilePath = "D:\\home\\readFile\\ReadFileExample-big.txt";
        int multiple = 10;

        fileExpand(targetFilePath, newTargetFilePath, multiple);
    }

    public static void fileExpand(String targetFilePath, String newTargetFilePath, int multiple) throws IOException {

        List<String> fileNameList = CopyFileUtil.copy(targetFilePath, multiple);
        String[] fpaths = new String[fileNameList.size()];

        int index = 0;
        for (String path : fileNameList){
            fpaths[index] = path;
            index++;
        }
        MergeFileUtil.mergeFiles2(fpaths, newTargetFilePath);
    }


}
