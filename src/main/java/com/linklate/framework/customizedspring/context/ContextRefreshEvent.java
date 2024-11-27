package com.linklate.framework.customizedspring.context;

/**
 * @ClassName ContextRefreshEvent
 * @Description
 * @Author Linklate
 * @Date 2024/11/27 15:02
 **/

public class ContextRefreshEvent extends ApplicationEvent{

    private static final long serialVersionUID = 1L;

    public ContextRefreshEvent(Object arg0) {
        super(arg0);
    }

    @Override
    public String toString() {
        return this.msg;
    }

}

