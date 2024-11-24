package com.linklate.framework.minis;

import com.linklate.framework.minis.beans.BeansException;
import com.linklate.framework.minis.beans.factory.BeanFactory;
import com.linklate.framework.minis.beans.factory.SimpleBeanFactory;
import com.linklate.framework.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.linklate.framework.minis.context.ApplicationEvent;
import com.linklate.framework.minis.context.ApplicationEventPublisher;
import com.linklate.framework.minis.core.ClassPathXmlResource;
import com.linklate.framework.minis.core.Resource;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import sun.rmi.runtime.NewThreadAction;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassPathXmlApplicationContext implements BeanFactory, ApplicationEventPublisher {
    SimpleBeanFactory beanFactory;

    //context负责整合容器的启动过程，读外部配置，解析Bean定义，创建BeanFactory
    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        SimpleBeanFactory simpleBeanFactory = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(simpleBeanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = simpleBeanFactory;
        if (isRefresh) {
            try {
                this.beanFactory.refresh();
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //context再对外提供一个getBean，底下就是调用的BeanFactory对应的方法
    public Object getBean(String beanName) throws BeansException {
        return this.beanFactory.getBean(beanName);
    }

    public Boolean containsBean(String name) {
        return this.beanFactory.containsBean(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return false;
    }

    @Override
    public boolean isPrototype(String name) {
        return false;
    }

    @Override
    public Class<?> getType(String name) {
        return null;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {

    }
}
