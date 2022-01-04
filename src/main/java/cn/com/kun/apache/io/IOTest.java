package cn.com.kun.apache.io;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class IOTest {

    public static void main(String[] args) throws IOException {

//        Path path = Paths.get("D:\\home2");
        Path path = Paths.get("D:\\home");
        File sourceFile = path.toFile();
        if (sourceFile.exists()){
            //列举一个目录下所有文件
            Iterator<File> fileIterator = FileUtils.iterateFiles(sourceFile,null, false);
            fileIterator.forEachRemaining(file -> {
                if (!file.isDirectory()){
                    System.out.println(file.getAbsolutePath());
                }
            });
        }

        //拷贝文件
        FileUtils.copyFile(Paths.get("D:\\home\\User.txt").toFile(), Paths.get("D:\\home\\User.txt.bak").toFile());

    }

}
