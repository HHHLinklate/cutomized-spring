package com.linklate.framework.customizedspring.web;

import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName XmlConfigReader
 * @Description
 * @Author Linklate
 * @Date 2024/12/16 23:11
 **/

public class XmlConfigReader {
    public XmlConfigReader() {
    }

    public Map<String, MappingValue> loadConfig(Resource res) {
        Map<String, MappingValue> mappings = new HashMap<>();

        while (res.hasNext()) {
            //读所有的节点，解析id, class和value
            Element element = (Element) res.next();
            String beanId = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            String beanMethod = element.attributeValue("value");

            mappings.put(beanId, new MappingValue(beanId, beanClassName, beanMethod));
        }

        return mappings;
    }
}
