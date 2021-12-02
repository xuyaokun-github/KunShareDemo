package cn.com.kun.framework.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 利用dom4j做xml替换
 *
 * author:xuyaokun_kzx
 * date:2021/11/23
 * desc:
*/
public class TestXmlReplaceByDom4j {

    public static void main(String[] args) throws Exception {

        String headerLine = "a,b,list1,c,d,list2";
        //先用逗号切割，然后用竖切割
        String contentLine = "1,2,qq#QQ#QQQ|ww#WW#WWW|ee#EE#EEE|rr#RR#RRR,3,4,jj#JJ#JJJ|kk#KK#KKK|ll#LL#LLL";


        String xmlSource = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html>\n" +
                "<body>" +
                "<a name=\"a\">$a$</a>" +
                "<a name=\"b\">$b$</a>" +
                "<a name=\"c\">$c$</a>" +
                "<a name=\"d\">$d$</a>" +
                "<ul>" +
                "<li itcsname=\"list1\">尊敬的$q$,,,,您的账号：$w$,,,,,消费：$e$</li>" +
                "</ul>" +
                "<ul>" +
                "<li itcsname=\"list2\" >尊敬的$q$,,,,您的信用卡：$w$,,,,,支出：$e$</li>" +
                "</ul>" +
                "</body>\n" +
                "</html>";

        System.out.println("模板：" + xmlSource);

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
        System.out.println("------------------------------------------------------------------------------");
        System.out.println(xmlSource);
    }

    private static String dealWithListType(String xmlSource, String header, String content) throws Exception {

        //一个content表示一个明细列表，一个元素表示明细中的一行
        String[] strArr = content.split("\\|");

        //递归找到ID为header的标签，拿到该标签的xml内容
        Document document = createDocument(new InputSource(new StringReader(xmlSource)));

        Element element = getElementById(document.getRootElement(), header, null);

        //正则
        String source = element.getText();
        String stringValue = element.getStringValue();
        //节点的整个xml内容
        String asXml = element.asXML();

        Element parentElement = element.getParent();
        //因为element即将会被替换，所以将模板的移除
        parentElement.remove(element);
        String getTestByParent = parentElement.getText();
        String getStringValueByParent = parentElement.getStringValue();
        //输出一个新的xmlSource
        StringBuilder builder = new StringBuilder();
        for (String str : strArr) {
            //str表示一行
            //产生新节点
            Element newElement = element.createCopy();
            newElement.setText(replaceListItem(source, str));
            parentElement.add(newElement);
        }

        //因为是引用类型，直接改即可
//        element.setText(builder.toString());


        System.out.println(document.asXML());
        return document.asXML();
    }

    private static String replaceListItem(String source, String str) {

        //str是一行的内容，用#号拼接
        //source即标签里的内容，例子 尊敬的$q$,,,,您的账号：$w$,,,,,消费：$e$
        //通过正则找到这种$符号的文本

        String[] strings = str.split("#");

//        source
        Pattern pattern2 = Pattern.compile("\\$(.*?)\\$");
        Matcher matcher2 = pattern2.matcher(source);
        List<String> groupList = new ArrayList<>();
        while(matcher2.find()) {
            System.out.println(matcher2.group());//group() 默认就是 group(0)
//            System.out.println(matcher2.group(1));
            groupList.add(matcher2.group());
        }

        for (int i = 0; i < groupList.size(); i++) {
            source = source.replace(groupList.get(i), strings[i]);
        }

        //正则表达式进行替换
        return source;
    }

    private static Document createDocument(InputSource inputSource) throws Exception {

        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputSource);
        return document;
    }

    /**
     * 通过id获取 element 元素
     */
    public static Element getElementById(Element root, String id, Element element) {

        List elements = root.elements();
        for (Object obj : elements) {
            Element elementx = (Element) obj;
            if (id.equals(elementx.attributeValue("itcsname"))) {
                element = elementx;
                break;
            } else {
                element = getElementById(elementx, id, element);
                if (element != null) {
                    break;
                }
            }
        }
        return element;
    }
}
