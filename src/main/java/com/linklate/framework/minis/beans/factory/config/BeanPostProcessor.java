package com.linklate.framework.minis.beans.factory.config;

import com.linklate.framework.minis.beans.BeansException;

public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

}
