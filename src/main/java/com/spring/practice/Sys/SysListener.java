package com.spring.practice.Sys;

import com.spring.practice.listener.Listener;
import org.springframework.stereotype.Component;

@Component
public class SysListener implements Listener<SysEvent> {

    @Override
    public void onEvent(SysEvent event) {
        System.out.println("收到收到~ event :" + event.getSource());
    }
}
