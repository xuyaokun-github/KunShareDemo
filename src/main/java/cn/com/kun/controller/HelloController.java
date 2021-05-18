package cn.com.kun.controller;

import cn.com.kun.common.vo.People;
import cn.com.kun.foo.mdc.MDCDemoController;
import cn.com.kun.common.utils.JedisUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@RestController
public class HelloController {

    public final static Logger logger = LoggerFactory.getLogger(MDCDemoController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${nexusdemo:123}")
    private String nexusdemo;

    @PostConstruct
    public void init(){
        System.out.println(nexusdemo);
    }

    @RequestMapping("/hello")
    public String testString(){
        return "kunghsu";
    }

    @RequestMapping("/testRestTemplate")
    public String testRestTemplate(){

        restTemplate.getForObject("http://127.0.0.1:8081/one/test", String.class);
        return "kunghsu";
    }


    @RequestMapping("/testExclude")
    public People testExclude(){
        People user = new People();
        user.setLastname("xyk");
        user.setFirstname("kunghsu");
        user.setPhone("10086");
        user.setEmail("12306@qq.com");
        int a = 1/0;
        return user;
    }

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




}
