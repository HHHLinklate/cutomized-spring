package com.linklate.framework.minis;

import com.linklate.framework.minis.beans.BeansException;
import com.linklate.framework.minis.beans.factory.BeanFactory;
import com.linklate.framework.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.linklate.framework.minis.beans.factory.config.AutowireCapableBeanFactory;
import com.linklate.framework.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.linklate.framework.minis.context.ApplicationEvent;
import com.linklate.framework.minis.context.ApplicationEventPublisher;
import com.linklate.framework.minis.core.ClassPathXmlResource;
import com.linklate.framework.minis.core.Resource;

public class ClassPathXmlApplicationContext implements BeanFactory, ApplicationEventPublisher {
    AutowireCapableBeanFactory beanFactory;

    //context负责整合容器的启动过程，读外部配置，解析Bean定义，创建BeanFactory
    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        AutowireCapableBeanFactory bf = new AutowireCapableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(bf);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = bf;
        if (isRefresh) {
            try {
                refresh();
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //context再对外提供一个getBean，底下就是调用的BeanFactory对应的方法
    public Object getBean(String beanName) throws BeansException {
        return this.beanFactory.getBean(beanName);
    }

    public boolean containsBean(String name) {
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

    public void refresh() throws BeansException, IllegalStateException {
        // Register bean processors that intercept bean creation.
        registerBeanPostProcessors(this.beanFactory);

        // Initialize other special beans in specific context subclasses.
        onRefresh();
    }

    private void registerBeanPostProcessors(AutowireCapableBeanFactory bf) {
        //if (supportAutowire) {
        bf.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
        //}
    }

    private void onRefresh() {
        this.beanFactory.refresh();
    }
}
