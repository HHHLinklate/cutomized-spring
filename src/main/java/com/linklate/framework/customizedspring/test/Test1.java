package com.linklate.framework.customizedspring.test;

import com.linklate.framework.customizedspring.context.ClassPathXmlApplicationContext;
import com.linklate.framework.customizedspring.beans.BeansException;
import com.linklate.framework.customizedspring.test.service.AService;
import com.linklate.framework.customizedspring.test.service.BaseService;

public class Test1 {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        AService aService;
        BaseService bService;
        try {
            aService = (AService)ctx.getBean("aservice");
            aService.sayHello();

            bService = (BaseService)ctx.getBean("baseservice");
            bService.sayHello();
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

}
