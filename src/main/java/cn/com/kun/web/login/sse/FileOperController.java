package cn.com.kun.web.login.sse;

import cn.com.kun.controller.App;
import cn.com.kun.foo.mdc.MDCDemoController;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@RequestMapping("/fileoper")
@RestController
public class FileOperController {

    public final static Logger LOGGER = LoggerFactory.getLogger(MDCDemoController.class);

    @RequestMapping("/testGetResourceAsStream")
    public String testGetResourceAsStream(){

        String name = "config.txt";

        System.out.println("第一种情况--通过App.class的getResource(name)");
        /**
         * 假如用了/，表示绝对路径，是从classpath开始的路径
         * 例如下面的config.txt放在springboot工程结构的resource目录下，就能获取到资源对象
         * springboot工程结构的resource目录就是从classpath开始的路径
         */
        URL url11 = App.class.getResource("/config.txt");
        System.out.println(url11);
        /**
         * 假如不加/，表示的是相对路径，相对什么呢？相对App.class这个class所在的位置
         * 取决于App.class这个类编译后的所存在的位置，
         * 例如：/E:/IdeaWorkspaces/Github/KunShareDemo/target/classes/cn/com/kun/controller
         * 所以config2.xml必须要在/E:/IdeaWorkspaces/Github/KunShareDemo/target/classes/cn/com/kun/controller，才会获取到资源对象
         */
        URL url12 = App.class.getResource("config2.xml");
        System.out.println(url12.getPath());//输出具体的物理路径
        // 假如用/表示的是全局的classpath,例如：/E:/IdeaWorkspaces/Github/KunShareDemo/target/classes/
        URL url = App.class.getResource("/");
        System.out.println("url1:" + url.getPath());
        System.out.println("-----------------------------------");

        System.out.println("第二种情况--通过App.class.getClassLoader().getResource(name)");
        /**
         * 下面三个都获取不到，只要用了/就获取不到
         */
        URL url21 = App.class.getClassLoader().getResource("/config.txt");
        System.out.println(url21);
        URL url22 = App.class.getClassLoader().getResource("/config2.xml");
        System.out.println(url22);
        URL url23 = App.class.getClassLoader().getResource("/");
        System.out.println(url23);
        /**
         * 假如不加/，表示相对路径，相对的是全局的classpath
         * /E:/IdeaWorkspaces/Github/KunShareDemo/target/classes/config.txt
         */
        URL url2 = App.class.getClassLoader().getResource("config.txt");
        System.out.println("url2:" + url2);
        System.out.println("-----------------------------------");



        System.out.println("第3种情况--通过App.class.getResourceAsStream(name)：");
        /**
         * 下面这种方式获取不到
         *  因为用了相对路径，表示的是从App.class所在位置的路径作为相对路径的起始
         */
        InputStream inputStream = App.class.getResourceAsStream("config.txt");
        System.out.println(inputStream);
        /**
         * 下面这种能获取到，因为App.class下有config2.xml这个文件
         */
        inputStream = App.class.getResourceAsStream("config2.xml");
        System.out.println(inputStream);
        /**
         * 下面这种能获取到，因为用了/，表示用绝对路径，/表示从全局的classpath开始
         */
        inputStream = App.class.getResourceAsStream("/config.txt");
        System.out.println(inputStream);
        System.out.println("-----------------------------------");

        System.out.println("第4种情况--通过App.class.getClassLoader().getResourceAsStream(name)：");
        /**
         * 可以通过getClassLoader.getResourceAsStream成功获取到classpath下的文件，用相对路径
         * 相对路径表示从全局的classpath开始
         */
        InputStream inputStream2 = App.class.getClassLoader().getResourceAsStream("config.txt");
        System.out.println(inputStream2);
        /**
         * 下面这种获取不到，因为classpath(resource目录)下没有这个文件
         */
        inputStream2 = App.class.getClassLoader().getResourceAsStream("config2.xml");
        System.out.println(inputStream2);
        /**
         * 假如用/，表示的是什么路径呢？
         * 获取不到。
         */
        inputStream2 = App.class.getClassLoader().getResourceAsStream("/config.txt");
        System.out.println(inputStream2);
        inputStream2 = App.class.getClassLoader().getResourceAsStream("/config2.xml");
        System.out.println(inputStream2);
        System.out.println("-----------------------------------");

        return "success!!";
    }

    @RequestMapping("/testGetResourceAsStream2")
    public String testGetResourceAsStream2(){

        try {
            String path = ResourceUtils.getFile("classpath:config.txt").getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        InputStream inputStream = null;
        try {
            inputStream = new ClassPathResource("config.txt").getInputStream();
            byte[] b = IOUtils.toByteArray(inputStream);
            String templateContent = new String(b, "UTF-8");
            System.out.println(templateContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success!!";

    }

    @RequestMapping("/testGetResourceAsFile")
    public String testGetResourceAsFile(){

        try {
            //这种写法在打包成jar之后会有问题
            String path = ResourceUtils.getFile("classpath:config.txt").getAbsolutePath();
            LOGGER.info("path:{}", path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        InputStream inputStream = null;
        try {
            inputStream = new ClassPathResource("config.txt").getInputStream();
            //拿到输入流先转成字节
            byte[] b = IOUtils.toByteArray(inputStream);
            //然后再把字节写到文件，生成一个新文件
            String parentPath = new ClassPathResource("config.txt").getFile().getParentFile().getAbsolutePath();
            LOGGER.info("parentPath:{}", parentPath);
            FileUtils.writeByteArrayToFile(new File(parentPath + File.separator + "config-bak.txt"), b);
            //展示一个错误的写法  最终会是：E:\IdeaWorkspaces\Github\KunShareDemo\classpath:config.txt
            File file = new File("classpath:config.txt");
            LOGGER.info("file:{}", file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success!!";

    }

    /**
     * 测试写一个图片到resources目录下
     * @return
     */
    @RequestMapping("/testSaveFileToResource")
    public String testSaveFileToResource(){


        String fileName = null ;
        try {
            //截屏前必须加这句，否则失败
            System.setProperty("java.awt.headless", "false");
            Robot robot = new Robot();
            // 获取屏幕大小
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle screenRect = new Rectangle(screenSize);
            // 捕获屏幕
            BufferedImage screenCapture = robot.createScreenCapture(screenRect);
            //存放位置（这里的路径就是Resource目录）
            String path = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
            fileName = System.currentTimeMillis() + "byTest.png" ;
            //目标文件路径
            String fullName = path +File.separator + fileName;
            LOGGER.info("屏幕截屏路径：{}", path);
            // 把捕获到的屏幕 输出为 图片
            ImageIO.write(screenCapture, "png", new File(fullName));

        } catch (Exception e) {
            LOGGER.error("获取屏幕截屏发生异常", e);
        }
        return "success!!";

    }



}
