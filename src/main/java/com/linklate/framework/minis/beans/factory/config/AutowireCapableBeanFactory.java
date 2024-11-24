package com.linklate.framework.minis.beans.factory.config;

import com.linklate.framework.minis.beans.BeansException;
import com.linklate.framework.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.linklate.framework.minis.beans.factory.support.AbstractBeanFactory;

import java.util.ArrayList;
import java.util.List;

public class AutowireCapableBeanFactory extends AbstractBeanFactory {

    private final List<AutowiredAnnotationBeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public void addBeanPostProcessor(AutowiredAnnotationBeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public List<AutowiredAnnotationBeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (AutowiredAnnotationBeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            beanProcessor.setBeanFactory(this);
            beanProcessor.postProcessBeforeInitialization(result, beanName);
            if (result == null) {
                return result;
            }
        }

        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (AutowiredAnnotationBeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            beanProcessor.setBeanFactory(this);
            beanProcessor.postProcessAfterInitialization(result, beanName);
            if (result == null) {
                return result;
            }
        }
        return result;
    }
}
