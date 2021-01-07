package com.spring.practice.handler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{

    /**
     *自定义处理方案，如重试机制，将异常信息入库等操作
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.info("{}:uncaughtException ", t.getName(), e.getMessage());
    }
}
