package com.spring.practice.factory;

import com.spring.practice.handler.CustomUncaughtExceptionHandler;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 创建线程工厂，指定有意义的线程名称，方便回溯
 */
public class CustomThreadFactory implements ThreadFactory {

    /**
     * 线程名称前缀
     */
    private String namePrefix;

    /**
     * 用于线程名称递增排序
     */
    private AtomicInteger atomicInteger = new AtomicInteger(1);

    public CustomThreadFactory(String whatFeatureOfGroup){
        this.namePrefix = "From CustomThreadFactory-" + whatFeatureOfGroup + "-worker-";
    }
    @Override
    public Thread newThread(Runnable r) {
        String name = namePrefix + atomicInteger.getAndIncrement();
        Thread thread = new Thread(r, name);
        thread.setUncaughtExceptionHandler(new CustomUncaughtExceptionHandler());
        return thread;
    }

}
