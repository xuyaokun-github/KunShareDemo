package cn.com.kun.framework.xml.dom4j;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.List;

public class Dom4jUtils {

    public static Document createDocument(InputSource inputSource) throws Exception {

        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputSource);
        return document;
    }


    public static Document createDocument(String xmlSource) throws Exception {
        return createDocument(new InputSource(new StringReader(xmlSource)));
    }

    /**
     * 通过id获取 element 元素
     */
    public static Element getElementByAttr(Element root, String attrName, String attrValue, Element element) {

        List elements = root.elements();
        for (Object obj : elements) {
            Element elementx = (Element) obj;
            if (attrValue.equals(elementx.attributeValue(attrName))) {
                element = elementx;
                break;
            } else {
                element = getElementByAttr(elementx, attrName, attrValue, element);
                if (element != null) {
                    break;
                }
            }
        }
        return element;
    }

}
