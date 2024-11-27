package com.linklate.framework.customizedspring.beans.factory.support;

import com.linklate.framework.customizedspring.beans.BeansException;
import com.linklate.framework.customizedspring.beans.factory.config.AbstractAutowireCapableBeanFactory;
import com.linklate.framework.customizedspring.beans.factory.config.BeanDefinition;
import com.linklate.framework.customizedspring.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DefaultListableBeanFactory
 * @Description
 * @Author Linklate
 * @Date 2024/11/25 20:45
 **/

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
                    implements ConfigurableListableBeanFactory {

    @Override
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionNames.toArray(new String[0]);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        List<String> result = new ArrayList<>();
        for (String beanName : this.beanDefinitionNames) {
            boolean matchFound = false;
            BeanDefinition mbd = this.getBeanDefinition(beanName);
            Class<?> classToMatch = mbd.getClass();
            if (type.isAssignableFrom(classToMatch)) {
                matchFound = true;
            } else {
                matchFound = false;
            }
            if (matchFound) {
                result.add(beanName);
            }
        }
        return result.toArray(new String[0]);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        String[] beanNames = getBeanNamesForType(type);
        Map<String, T> result = new LinkedHashMap<>(beanNames.length);
        for (String beanName : beanNames) {
            Object beanInstance = getBean(beanName);
            result.put(beanName, (T) beanInstance);
        }
        return result;
    }


}
