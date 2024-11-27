package com.linklate.framework.customizedspring.beans.factory.config;

import com.linklate.framework.customizedspring.beans.BeansException;
import com.linklate.framework.customizedspring.beans.factory.BeanFactory;

public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

    void setBeanFactory(BeanFactory beanFactory);

}
