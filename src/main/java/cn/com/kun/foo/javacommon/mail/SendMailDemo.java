package cn.com.kun.foo.javacommon.mail;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class SendMailDemo {

    public static void main(String[] args) throws MessagingException, GeneralSecurityException {

        //1.创建一个配置文件保存并读取信息
        Properties properties = new Properties();

        //设置QQ邮件服务器
        properties.setProperty("mail.host", "smtp.qq.com");
        //设置发送协议
        properties.setProperty("mail.transport.protocol", "smtp");
        //设置用户是否需要验证
        properties.setProperty("mail.smtp.auth", "true");

        // 关于QQ邮箱，还要设置SSL加密，加上以下代码即可
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        //1.创建一个session会话对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("304813473@qq.com", "qpbjvbfqntvfbigi");
            }
        });
        //通过session开启debug模式，查看所有的过程
        session.setDebug(true);

        //2.连接服务器，通过session对象获得Transport，需要补货或者抛出异常
        Transport transport = session.getTransport();

        //1.连接服务器,需要抛出异常
        transport.connect("smtp.qq.com", "304813473@qq.com", "qpbjvbfqntvfbigi");

        //连接之后我们需要发送邮件

        //创建邮件对象
        MimeMessage mimeMessage = new MimeMessage(session);
        //邮件发送人
        mimeMessage.setFrom(new InternetAddress("304813473@qq.com"));
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress("304813473@qq.com"));

        //邮件标题
        mimeMessage.setSubject("Hello JavaMail Test!!!!");

        //邮件内容
        mimeMessage.setContent("Lakers Win", "text/html;charset=UTF-8");
        //发送邮件
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        //关闭连接
        transport.close();
    }
}
