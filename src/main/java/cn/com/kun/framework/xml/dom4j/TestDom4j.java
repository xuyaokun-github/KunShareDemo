package cn.com.kun.framework.xml.dom4j;

import org.dom4j.Document;
import org.dom4j.Element;

public class TestDom4j {

    public static void main(String[] args) throws Exception {

        String xmlSource = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html>\n" +
                "<body>" +
                "<a name=\"a\">$a$</a>" +
                "<a name=\"b\">$b$</a>" +
                "<a name=\"c\">$c$</a>" +
                "<a name=\"d\">$d$</a>" +
                "<ul>" +
                "<li itcsname=\"list1\">$q$,$w$,$e$,$r$</li>" +
                "</ul>" +
                "<ul>" +
                "<li itcsname=\"list2\" >$j$,$k$,$l$</li>" +
                "</ul>" +
                "</body>\n" +
                "</html>";

        Document document = Dom4jUtils.createDocument(xmlSource);

        //替换ul节点里的内容，复制出多个li节点
        Element element = Dom4jUtils.getElementByAttr(document.getRootElement(), "itcsname", "list1", null);
        Element parentElement = element.getParent();
        parentElement.remove(element);

        for (int i = 0; i < 5; i++) {
            Element newElement = element.createCopy();
            parentElement.add(newElement);
        }

        System.out.println(document.asXML());
    }


}
