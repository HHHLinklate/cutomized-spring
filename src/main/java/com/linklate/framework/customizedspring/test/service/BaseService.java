package com.linklate.framework.customizedspring.test.service;

import com.linklate.framework.customizedspring.beans.factory.annotation.Autowired;

public class BaseService {

    @Autowired
    private BaseBaseService bbs;

    public BaseService() {
    }

    public void init() {
        System.out.print("Base Service init method.");
    }

    public BaseBaseService getBbs() {
        return bbs;
    }

    public void setBbs(BaseBaseService bbs) {
        this.bbs = bbs;
    }

    public void sayHello() {
        System.out.print("Base Service says hello");
        bbs.sayHello();
    }
}

