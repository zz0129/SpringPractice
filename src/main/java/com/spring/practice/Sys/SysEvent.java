package com.spring.practice.Sys;

import com.spring.practice.event.Event;

public class SysEvent extends Event<String> {

    public SysEvent(String data) {
        super(data);
    }
}
