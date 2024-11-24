package com.linklate.framework.minis.beans.factory.annotation;

import com.linklate.framework.minis.beans.BeansException;
import com.linklate.framework.minis.beans.factory.config.AutowireCapableBeanFactory;
import com.linklate.framework.minis.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {

    private AutowireCapableBeanFactory beanFactory;

    public AutowireCapableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Object result = bean;

        Class<?> clazz = result.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if(fields!=null){
            for(Field field : fields){
                boolean isAutowired = field.isAnnotationPresent(Autowired.class);
                if(isAutowired){
                    String fieldName = field.getName();
                    Object autowiredObj = this.getBeanFactory().getBean(fieldName);
                    try {
                        field.setAccessible(true);
                        field.set(bean, autowiredObj);
                        System.out.println("autowire " + fieldName + " for bean " + beanName);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        return result;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }
}
