package cn.com.kun.foo.javacommon.mail;

import org.apache.commons.io.output.WriterOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 生成邮件原文Demo
 *
 * author:xuyaokun_kzx
 * date:2023/2/14
 * desc:
*/
public class TestMail {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestMail.class);

    public static void main(String[] args) throws MessagingException {

        //创建邮件对象
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        //邮件发送人
        mimeMessage.setFrom(new InternetAddress("304813473@qq.com"));
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress("304813473@qq.com"));

        //邮件标题
        mimeMessage.setSubject("Hello JavaMail Test!!!!");

        //邮件内容
        mimeMessage.setContent("Lakers Win", "text/html;charset=UTF-8");

        try {
            System.out.println("输出原文：");
            System.out.println(mimeMessage.getContent());
            System.out.println(mimeMessage.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Writer out = new StringWriter(); WriterOutputStream outputStream = new WriterOutputStream(out);) {
            mimeMessage.writeTo(outputStream);
            String result = out.toString();
            System.out.println(result);
        } catch (Exception e) {
            LOGGER.error(String.format("生成邮件原文异常"), e);
            e.printStackTrace();
        }

    }
}
