package com.linklate.framework;

import com.linklate.framework.minis.ClassPathXmlApplicationContext;
import com.linklate.framework.minis.beans.BeansException;
import com.linklate.framework.test.service.AService;
import com.linklate.framework.test.service.BaseService;

public class Test1 {
    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");
        BaseService baseService;
        baseService = (BaseService)ctx.getBean("baseservice");
        baseService.sayHello();
    }
}
