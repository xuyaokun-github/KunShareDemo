package cn.com.kun.foo.javacommon.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class TestMoveFileToFatherDir {

    private final static Logger log = LoggerFactory.getLogger(TestMoveFileToFatherDir.class);

    public static void main(String[] args) {


        File sourceFile = new File("E:\\Download\\xxxxx");
        System.out.println(sourceFile.getAbsolutePath());

        File[] files = sourceFile.listFiles();
        for (File file : files){
            if (file.isDirectory()){
                System.out.println(file.getAbsolutePath());
                //
//                if (file.getAbsolutePath().contains("175")){
                    File[] files2 = file.listFiles();
                    for (File file2 : files2){
                        moveTotherFolders(file2.getAbsolutePath(), sourceFile.getAbsolutePath());
                    }
//                }

            }
        }
    }

    private static void moveTotherFolders(String startPath, String endPath){
        try {
            File startFile = new File(startPath);
            File tmpFile = new File(endPath);//获取文件夹路径
            if(!tmpFile.exists()){//判断文件夹是否创建，没有创建则创建新文件夹
                tmpFile.mkdirs();
            }
            System.out.println(endPath + File.separator + startFile.getName());
            if (startFile.renameTo(new File(endPath + File.separator + startFile.getName()))) {
                System.out.println("File is moved successful!");
                log.info("文件移动成功！文件名：《{}》 目标路径：{}",startFile.getName(),endPath);
            } else {
                System.out.println("File is failed to move!");
                log.info("文件移动失败！文件名：《{}》 起始路径：{}",startFile.getName(),startPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
