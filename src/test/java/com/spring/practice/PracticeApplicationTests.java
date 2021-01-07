package com.spring.practice;

import com.spring.practice.Sys.SysEvent;
import com.spring.practice.multicaster.SimpleEventMulticaster;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PracticeApplicationTests {

    @Autowired
    public SimpleEventMulticaster simpleEventMulticaster;

    @Test
    void contextLoads() {
        System.out.println("********" + simpleEventMulticaster);

        int i = 0;
        while (i < 4) {
            if(i == 3) {
                System.out.println("&&&&&&&&&&&&&");
                simpleEventMulticaster.multicastEvent(new SysEvent(String.valueOf(i)));
            }
            i++;
        }
    }

}
