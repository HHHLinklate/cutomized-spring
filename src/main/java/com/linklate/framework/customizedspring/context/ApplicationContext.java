package com.linklate.framework.customizedspring.context;

import com.linklate.framework.customizedspring.beans.BeansException;
import com.linklate.framework.customizedspring.beans.factory.ListableBeanFactory;
import com.linklate.framework.customizedspring.beans.factory.config.BeanFactoryPostProcessor;
import com.linklate.framework.customizedspring.beans.factory.config.ConfigurableBeanFactory;
import com.linklate.framework.customizedspring.beans.factory.config.ConfigurableListableBeanFactory;
import com.linklate.framework.customizedspring.core.env.Environment;
import com.linklate.framework.customizedspring.core.env.EnvironmentCapable;

/**
 * @ClassName ApplicationContext
 * @Description
 * @Author Linklate
 * @Date 2024/11/25 21:09
 **/

public interface ApplicationContext
        extends EnvironmentCapable, ListableBeanFactory, ConfigurableBeanFactory, ApplicationEventPublisher{
    String getApplicationName();
    long getStartupDate();
    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;
    void setEnvironment(Environment environment);
    Environment getEnvironment();
    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);
    void refresh() throws BeansException, IllegalStateException;
    void close();
    boolean isActive();
}
