package cn.com.kun.foo.javacommon.io;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 *
 * author:xuyaokun_kzx
 * date:2022/12/27
 * desc:
*/
public class FileStringUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(FileStringUtils.class);

    /**
     * 文件转字符串(base64编码字符串)
     *
     * @param sourceFile
     * @return
     */
    public static String fileToString(File sourceFile){

        String value = null;
        byte[] byteArr = new byte[0];
        try {
            byteArr = FileUtils.readFileToByteArray(sourceFile);
            value = Base64.encodeBase64String(byteArr);
        } catch (Exception e) {
            LOGGER.error("文件转字符串异常", e);
        }
        return value;
    }

    /**
     * 字符串转文件
     *
     * @param value
     * @param filePath
     * @return
     */
    public static File stringToFile(String value, String filePath){

        File file = new File(filePath);
        try {
            byte[] byteArr2 = Base64.decodeBase64(value);
            FileUtils.writeByteArrayToFile(new File(filePath), byteArr2);
        } catch (Exception e) {
            LOGGER.error("字符串转文件异常", e);
            return null;
        }
        return file;
    }

}
