package com.linklate.framework.minis.beans.factory;

import com.linklate.framework.minis.beans.factory.config.*;
import com.linklate.framework.minis.beans.BeansException;
import com.linklate.framework.minis.beans.factory.support.BeanDefinitionRegistry;
import com.linklate.framework.minis.beans.factory.support.DefaultSingletonBeanRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    private Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(256);

    private List<String> beanDefinitionNames = new ArrayList<>();

    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(name, beanDefinition);
        this.beanDefinitionNames.add(name);
    }

    public void removeBeanDefinition(String name) {
        this.beanDefinitionMap.remove(name);
        this.beanDefinitionNames.remove(name);
        this.removeSingleton(name);
    }

    public BeanDefinition getBeanDefinition(String name) {
        return this.beanDefinitionMap.get(name);
    }

    public boolean containsBeanDefinition(String name) {
        return this.beanDefinitionMap.containsKey(name);
    }

    public void refresh() throws BeansException {
        for (String beanName : beanDefinitionNames) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                throw new BeansException(e.getMessage());
            }
        }
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        //先尝试直接从容器中获取bean实例
        Object singleton = this.getSingleton(beanName);
        if (singleton == null) {
            //如果没有实例，则尝试从毛胚实例中获取
            singleton = this.earlySingletonObjects.get(beanName);
            if (singleton == null) {
                //如果连毛胚都没有，则创建bean实例并注册
                BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
                singleton = createBean(beanDefinition);
                this.registerSingleton(beanName, singleton);
                // 预留beanpostprocessor位置
                // step 1: postProcessBeforeInitialization
                // step 2: afterPropertiesSet
                // step 3: init-method
                // step 4: postProcessAfterInitialization
            }
        }
        return singleton;
    }

    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clz = null;
        //创建毛胚bean实例
        Object obj = doCreateBean(beanDefinition);
        //存放到毛胚实例缓存中
        this.earlySingletonObjects.put(beanDefinition.getId(), obj);
        try {
            clz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //处理属性
        handleProperties(beanDefinition, clz, obj);
        return obj;
    }

    //doCreateBean创建毛胚实例，仅仅调用构造方法，没有进行属性处理
    private Object doCreateBean(BeanDefinition bd) {
        Class<?> clz = null;
        Object obj = null;
        Constructor<?> con = null;

        try {
            clz = Class.forName(bd.getClassName());

            //handle constructor
            ArgumentValues argumentValues = bd.getConstructorArgumentValues();
            if (!argumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
                Object[] paramValues =   new Object[argumentValues.getArgumentCount()];
                for (int i=0; i<argumentValues.getArgumentCount(); i++) {
                    ArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);
                    if ("String".equals(argumentValue.getType()) || "java.lang.String".equals(argumentValue.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    }
                    else if ("Integer".equals(argumentValue.getType()) || "java.lang.Integer".equals(argumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
                    }
                    else if ("int".equals(argumentValue.getType())) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue()).intValue();
                    }
                    else {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    }
                }

                con = clz.getConstructor(paramTypes);
                obj = con.newInstance(paramValues);

            }
            else {
                obj = clz.newInstance();
            }
        } catch (ClassNotFoundException |
                 InvocationTargetException |
                 NoSuchMethodException |
                 InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        System.out.println(bd.getId() + " bean created. " + bd.getClassName() + " : " + obj.toString());
        return obj;

    }


    @Override
    public Boolean containsBean(String name) {
        return null;
    }

    public boolean isSingleton(String name) {
        return this.beanDefinitionMap.get(name).isSingleton();
    }
    public boolean isPrototype(String name) {
        return this.beanDefinitionMap.get(name).isPrototype();
    }
    public Class<?> getType(String name) {
        return this.beanDefinitionMap.get(name).getClass();
    }

    /*private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clz = null;
        Object obj = null;
        Constructor<?> con = null;
        try {
            clz = Class.forName(beanDefinition.getClassName());
            // 处理构造器参数
            ArgumentValues argumentValues = beanDefinition.getConstructorArgumentValues();
            //如果有参数
            if (!argumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
                Object[] paramValues = new Object[argumentValues.getArgumentCount()];
                //对每一个参数，分数据类型分别处理
                for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
                    ArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);
                    if ("String".equals(argumentValue.getType()) ||
                            "java.lang.String".equals(argumentValue.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    } else if ("Integer".equals(argumentValue.getType()) ||
                            "java.lang.Integer".equals(argumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String)argumentValue.getValue());
                    } else if ("int".equals(argumentValue.getType())) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
                    } else { //默认为string
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    }
                }
                try {
                    //按照特定构造器创建实例
                    con = clz.getConstructor(paramTypes);
                    obj = con.newInstance(paramValues);
                } catch (Exception e) {}
            } else { //如果没有参数，直接创建实例
                obj = clz.newInstance();
            }
        } catch (Exception e) {
        }

        handleProperties(beanDefinition, clz, obj); 
        return obj;

    }
*/


    private void handleProperties(BeanDefinition bd, Class<?> clz, Object obj)  {
        // 处理属性
        System.out.println("handle properties for bean : " + bd.getId());
        PropertyValues propertyValues = bd.getPropertyValues();
        //如果有属性
        if (!propertyValues.isEmpty()) {
            for (int i=0; i<propertyValues.size(); i++) {
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                String pName = propertyValue.getName();
                String pType = propertyValue.getType();
                Object pValue = propertyValue.getValue();
                boolean isRef = propertyValue.getIsRef();
                Class<?>[] paramTypes = new Class<?>[1];
                Object[] paramValues =   new Object[1];
                if (!isRef) { //如果不是ref，只是普通属性
                    //对每一个属性，分数据类型分别处理
                    if ("String".equals(pType) || "java.lang.String".equals(pType)) {
                        paramTypes[0] = String.class;
                    }
                    else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
                        paramTypes[0] = Integer.class;
                    }
                    else if ("int".equals(pType)) {
                        paramTypes[0] = int.class;
                    }
                    else {
                        paramTypes[0] = String.class;
                    }

                    paramValues[0] = pValue;
                }
                else { //is ref, create the dependent beans
                    try {
                        paramTypes[0] = Class.forName(pType);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        //再次调用getBean创建ref的bean实例
                        paramValues[0] = getBean((String)pValue);
                    } catch (BeansException e) {

                    }
                }

                //按照setXxxx规范查找setter方法，调用setter方法设置属性
                String methodName = "set" + pName.substring(0,1).toUpperCase() + pName.substring(1);
                Method method = null;
                try {
                    method = clz.getMethod(methodName, paramTypes);
                } catch (NoSuchMethodException e) {

                }
                try {
                    method.invoke(obj, paramValues);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}


