package cn.com.kun.apache.io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;


public class LineIteratorTest {

    public static void main(String[] args) {
        try {
            usingLineIterator();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 一样会有OOM问题，本质还是使用BufferedReader
     * BufferedReader的readLine方法就会导致OOM
     *
     * @throws IOException
     */
    public static void usingLineIterator() throws IOException {
        //get the file object
        String bigFilePath = "D:\\home\\kunghsu\\big-file-test\\big-file.txt";
        File file = FileUtils.getFile(bigFilePath);
        try(LineIterator lineIterator = FileUtils.lineIterator(file)) {
//            System.out.println("Contents of input.txt");
            while(lineIterator.hasNext()) {
                System.out.println(lineIterator.next());
            }
        }
    }

}
