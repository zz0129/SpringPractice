package com.spring.practice.listener;

import com.spring.practice.event.Event;

public interface Listener<E extends Event> {

    /**
     * 当事件发生时进行操作
     */
    void onEvent(E event);
}
