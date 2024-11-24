package com.linklate.framework.minis.beans.factory.xml;

import com.linklate.framework.minis.beans.factory.config.*;
import com.linklate.framework.minis.beans.factory.SimpleBeanFactory;
import com.linklate.framework.minis.core.Resource;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class XmlBeanDefinitionReader {
    SimpleBeanFactory simpleBeanFactory;

    public XmlBeanDefinitionReader(SimpleBeanFactory bf) {
        this.simpleBeanFactory = bf;
    }

    public void loadBeanDefinitions(Resource res) {
        while (res.hasNext()) {
            Element element = (Element) res.next();
            String beanID = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);

            //处理属性
            List<Element> propertyElements = element.elements("property");
            PropertyValues PVS = new PropertyValues();
            ArrayList<Object> refs = new ArrayList<>();
            for (Element e : propertyElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                String pRef = e.attributeValue("ref");
                String pV = "";
                boolean isRef = false;
                if (pValue != null && !pValue.equals("")) {
                    isRef = false;
                    pV = pValue;
                } else if (pRef != null && !pRef.equals("")) {
                    isRef = true;
                    pV = pRef;
                    refs.add(pRef);
                }
                PVS.addPropertyValue(new PropertyValue(pType, pName, pV, isRef));
            }
            beanDefinition.setPropertyValues(PVS);

            //处理构造器参数
            List<Element> constructorElements = element.elements("constructor-arg");
            ArgumentValues AVS = new ArgumentValues();
            for (Element e : constructorElements) {
                String aType = e.attributeValue("type");
                String aName = e.attributeValue("name");
                String aValue = e.attributeValue("value");
                AVS.addArgumentValue(new ArgumentValue(aType, aName, aValue));
            }
            beanDefinition.setConstructorArgumentValues(AVS);

            String[] refArray = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArray);

            this.simpleBeanFactory.registerBeanDefinition(beanID, beanDefinition);
        }
    }
}