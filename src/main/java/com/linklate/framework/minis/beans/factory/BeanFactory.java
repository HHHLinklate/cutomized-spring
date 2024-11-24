package com.linklate.framework.minis.beans.factory;

import com.linklate.framework.minis.beans.BeansException;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;

    boolean containsBean(String name);

    //void registerBean(String beanName, Object obj);

    boolean isSingleton(String name);

    boolean isPrototype(String name);

    Class<?> getType(String name);
}
