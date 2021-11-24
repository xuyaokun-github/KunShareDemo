package cn.com.kun.springframework.freemarker.extend;

import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.springframework.freemarker.vo.FreemarkerBook;
import cn.com.kun.springframework.freemarker.vo.FreemarkerUser;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestFmkTemplate {

    private static String sourceCode = "<!-- 获取网站根路径 -->\n" +
            "<html>\n" +
            "<!-- js目录在static目录下，request.contextPath就是从static目录开始-->\n" +
            "<body>\n" +
            "<p>${password}</p>\n" +
            "<p>${userName}</p>\n" +
            "<#--语法展示，多层级-->\n" +
            "<#list users as user>\n" +
            "<div>\n" +
            "    <P>${user.name}</P>\n" +
            "    <P>${user.address}</P>\n" +
            "    <#list user.books as book>\n" +
            "    <ul>\n" +
            "        <li>${book.name}</li>\n" +
            "        <li>${book.price}</li>\n" +
            "    </ul>\n" +
            "    </#list>\n" +
            "</div>\n" +
            "</#list>\n" +
            "\n" +
            "</body>\n" +
            "</html>";

    public static void main(String[] args) throws IOException, TemplateException {

        Model model = new ConcurrentModel();
        model.addAttribute("password", "123");
        model.addAttribute("userName", "456");

        FreemarkerBook freemarkerBook = new FreemarkerBook("red", "123");
        FreemarkerBook freemarkerBook2 = new FreemarkerBook("green", "124");

        List<FreemarkerBook> bookList = new ArrayList<>();
        bookList.add(freemarkerBook);
        bookList.add(freemarkerBook2);

        FreemarkerUser freemarkerUser = new FreemarkerUser();
        //假如是空值会报异常，所以必须塞个非空值
        freemarkerUser.setName("kunghsu");
        freemarkerUser.setAddress("shenzhen");

        freemarkerUser.setBooks(bookList);

        List<FreemarkerUser> userList = new ArrayList<>();
        userList.add(freemarkerUser);
        model.addAttribute("users", userList);

        Writer out = new StringWriter();

        Template template = new Template("myFirstTemplate", sourceCode, null);
        //可以传model，即前端很经常用的org.springframework.ui.Model
        template.createProcessingEnvironment(model, out).process();
        System.out.println(out.toString());
    }


    @Test
    public void test1() throws IOException, TemplateException {

//        String sourceCode = "<!-- 获取网站根路径 -->\n" +
//                "<html>\n" +
//                "<!-- js目录在static目录下，request.contextPath就是从static目录开始-->\n" +
//                "<body>\n" +
//                "<p> ${password}</p>\n" +
//                "<p> ${userName}</p>\n" +
//                "</body>\n" +
//                "</html>";

        Map<String, Object> map = new HashMap<>();
        map.put("password", "888");
        map.put("userName", "kunghsu");

        FreemarkerBook freemarkerBook = new FreemarkerBook("red", "123");
        FreemarkerBook freemarkerBook2 = new FreemarkerBook("green", "124");

        List<FreemarkerBook> bookList = new ArrayList<>();
        bookList.add(freemarkerBook);
        bookList.add(freemarkerBook2);

        FreemarkerUser freemarkerUser = new FreemarkerUser();
        //假如是空值会报异常，所以必须塞个非空值
        freemarkerUser.setName("kunghsu");
        freemarkerUser.setAddress("shenzhen");

        freemarkerUser.setBooks(bookList);

        List<FreemarkerUser> userList = new ArrayList<>();
        userList.add(freemarkerUser);
        map.put("users", userList);

        String result = null;
        try (Writer out = new StringWriter()) {
            Template template = new Template("myFirstTemplate", sourceCode, null);
            //参数也可以传Map<String, Object>
            template.createProcessingEnvironment(map, out).process();
            result = out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);

        System.out.println("-------------------------");
    }

    @Test
    public void test2() throws IOException, TemplateException {

        Map<String, Object> map = new HashMap<>();
        map.put("password", "888");
        map.put("userName", "kunghsu");

        FreemarkerBook freemarkerBook = new FreemarkerBook("red", "123");
        FreemarkerBook freemarkerBook2 = new FreemarkerBook("green", "124");

        List<FreemarkerBook> bookList = new ArrayList<>();
        bookList.add(freemarkerBook);
        bookList.add(freemarkerBook2);

        FreemarkerUser freemarkerUser = new FreemarkerUser();
        //假如是空值会报异常，所以必须塞个非空值
        freemarkerUser.setName("kunghsu");
        freemarkerUser.setAddress("shenzhen");

        freemarkerUser.setBooks(bookList);

        List<FreemarkerUser> userList = new ArrayList<>();
        userList.add(freemarkerUser);
        map.put("users", userList);
        System.out.println(FreemarkerTemplateUtils.replace(sourceCode, map));
    }

    @Test
    public void test3() throws IOException, TemplateException {

        Map<String, Object> map = new HashMap<>();
        map.put("password", "888");
        map.put("userName", "kunghsu");

        FreemarkerBook freemarkerBook = new FreemarkerBook("red", "123");
        FreemarkerBook freemarkerBook2 = new FreemarkerBook("green", "124");

        List<FreemarkerBook> bookList = new ArrayList<>();
        bookList.add(freemarkerBook);
        bookList.add(freemarkerBook2);

        FreemarkerUser freemarkerUser = new FreemarkerUser();
        //假如是空值会报异常，所以必须塞个非空值
        freemarkerUser.setName("kunghsu");
        freemarkerUser.setAddress("shenzhen");

        freemarkerUser.setBooks(bookList);

        List<FreemarkerUser> userList = new ArrayList<>();
        userList.add(freemarkerUser);
        map.put("users", userList);
        for (int i = 0; i < 5; i++) {
            ThreadUtils.runAsyncByRunnable(()->{
                while (true){
                    try {
                        System.out.println(FreemarkerTemplateUtils.replace("", "1000", sourceCode, map));
                        Thread.sleep(200);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }

        ThreadUtils.runForever();
    }
}
