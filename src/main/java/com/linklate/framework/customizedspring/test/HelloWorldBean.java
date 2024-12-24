package com.linklate.framework.customizedspring.test;

import com.linklate.framework.customizedspring.web.RequestMapping;

/**
 * @ClassName HelloWorldBean
 * @Description
 * @Author Linklate
 * @Date 2024/12/17 08:11
 **/

public class HelloWorldBean {
    @RequestMapping("/test")
    public String doTest() {
        return "hello world for doTest";
    }

    public String doGet() {
        return "hello world!";
    }

    public String doPost() {
        return "hello world!";
    }
}
