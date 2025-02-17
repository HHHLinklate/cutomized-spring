package com.linklate.framework.customizedspring.web;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlScanComponentHelper {
    // 获取XML中base-package的值并加载到内存中
    public static List<String> getNodeValue(URL xmlPath) {
        List<String> packages = new ArrayList<>();
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(xmlPath); //加载配置文件
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        Iterator<Element> it = root.elementIterator();
        while (it.hasNext()) { //得到XML中所有的base-package节点
            Element element = (Element) it.next();
            packages.add(element.attributeValue("base-package"));              }
        return packages;
    }
}
