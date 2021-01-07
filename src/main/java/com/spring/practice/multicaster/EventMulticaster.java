package com.spring.practice.multicaster;

import com.spring.practice.event.Event;
import com.spring.practice.listener.Listener;

public interface EventMulticaster {

    /**
     * 添加监听器
     */
    void addListener(Listener<?> listener);

    /**
     * 移除监听器
     */
    void removeListener(Listener<?> listener);

    /**
     * 发布事件
     */
    void multicastEvent(Event event);
}
