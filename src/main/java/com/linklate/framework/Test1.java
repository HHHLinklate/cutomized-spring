package com.linklate.framework;

import com.linklate.framework.minis.ClassPathXmlApplicationContext;
import com.linklate.framework.minis.beans.BeansException;
import com.linklate.framework.test.service.AService;

public class Test1 {
    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");
        AService aservice = (AService)ctx.getBean("aservice");
        aservice.sayHello();
    }
}
