package com.linklate.framework.customizedspring.context;

import java.util.EventListener;

/**
 * @ClassName ApplicationListener
 * @Description
 * @Author Linklate
 * @Date 2024/11/27 14:58
 **/

public class ApplicationListener implements EventListener {
    void onApplicationEvent(ApplicationEvent event) {
        System.out.println(event.toString());
    }
}
