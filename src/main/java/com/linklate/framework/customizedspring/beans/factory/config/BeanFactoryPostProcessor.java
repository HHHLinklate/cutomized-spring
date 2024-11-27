package com.linklate.framework.customizedspring.beans.factory.config;

import com.linklate.framework.customizedspring.beans.factory.BeanFactory;

public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(BeanFactory beanFactory);
}
