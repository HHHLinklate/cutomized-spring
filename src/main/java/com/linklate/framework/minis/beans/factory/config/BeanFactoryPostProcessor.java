package com.linklate.framework.minis.beans.factory.config;

import com.linklate.framework.minis.beans.factory.BeanFactory;

public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(BeanFactory beanFactory);
}
