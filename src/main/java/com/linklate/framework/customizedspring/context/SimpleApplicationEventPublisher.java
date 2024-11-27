package com.linklate.framework.customizedspring.context;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SimpleApplicationEventPublisher
 * @Description
 * @Author Linklate
 * @Date 2024/11/27 15:24
 **/

public class SimpleApplicationEventPublisher implements ApplicationEventPublisher {

    List<ApplicationListener> listeners = new ArrayList<>();

    @Override
    public void publishEvent(ApplicationEvent event) {
        for (ApplicationListener listener : listeners) {
            listener.onApplicationEvent(event);
        }
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.listeners.add(listener);
    }
}
