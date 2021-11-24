package cn.com.kun.classlibrary.xml;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

/**
 * 失败的例子
 * 还是dom4j好用
 *
 * author:xuyaokun_kzx
 * date:2021/11/23
 * desc:
*/
public class TestXmlReplace {

    public static void main(String[] args) {

        String headerLine = "a,b,list1,c,d,list2";
        //先用逗号切割，然后用竖切割
        String contentLine = "1,2,qq|ww|ee|rr,3,4,jj|kk|ll";


        String xmlSource = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html>\n" +
                "<body>" +
                "<a name=\"a\">$a$</a>" +
                "<a name=\"b\">$b$</a>" +
                "<a name=\"c\">$c$</a>" +
                "<a name=\"d\">$d$</a>" +
                "<ul>" +
                "<li id=\"list1\" name=\"list1\">$q$,$w$,$e$,$r$</li>" +
                "</ul>" +
                "<ul>" +
                "<li id=\"list2\">$j$,$k$,$l$</li>" +
                "</ul>" +
                "</body>\n" +
                "</html>";

        XPathParser xPathParser = new XPathParser(xmlSource);

//        XNode xNode = xPathParser.evalNode("/ul");
        XNode xNode = xPathParser.evalNode("/html/body");
        System.out.println(xNode);

        String[] headerLineArr = headerLine.split(",");

        String[] contentLineArr = contentLine.split(",");
        for (int i = 0; i < contentLineArr.length; i++) {
            //判断是不是集合类型
            if (headerLineArr[i].startsWith("list")){
                //处理数组
                xmlSource = dealWithListType(xmlSource, headerLineArr[i], contentLineArr[i]);
            }else {
                //
                xmlSource = xmlSource.replace("$" + headerLineArr[i] + "$", contentLineArr[i]);
            }
        }

        System.out.println(xmlSource);
    }

    private static String dealWithListType(String xmlSource, String header, String content) {

        String[] strArr = content.split("\\|");

        //递归找到ID为header的标签，拿到该标签的xml内容
        Document document = createDocument(new InputSource(new StringReader(xmlSource)));

        Element element = document.getElementById(header);

        //正则
        String source = "";

        //输出一个新的xmlSource

        //
        System.out.println(document.getTextContent());
        System.out.println(document.toString());
        return null;
    }


    private static Document createDocument(InputSource inputSource) {
        // important: this must only be called AFTER common constructor
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(false);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(false);
            factory.setCoalescing(false);
            factory.setExpandEntityReferences(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
//            builder.setEntityResolver(entityResolver);
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void error(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void warning(SAXParseException exception) throws SAXException {
                }
            });
            return builder.parse(inputSource);
        } catch (Exception e) {
            throw new BuilderException("Error creating document instance.  Cause: " + e, e);
        }
    }
}
