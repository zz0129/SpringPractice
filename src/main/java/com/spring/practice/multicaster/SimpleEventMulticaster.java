package com.spring.practice.multicaster;

import com.spring.practice.event.Event;
import com.spring.practice.factory.CustomThreadFactory;
import com.spring.practice.listener.Listener;
import com.spring.practice.util.AopTargetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SimpleEventMulticaster implements EventMulticaster, ApplicationContextAware {

    private final Set<Listener<?>> listeners = new LinkedHashSet<>();

    /**
     * 创建一个执行监听器线程池，核心线程数为1，最大线程数为5，线程空闲时间为60s，拒绝策略为打印日志并直接执行被拒绝的任务
     */
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(1, 5, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(50), new CustomThreadFactory("simpleEventMulticaster"), new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.error("Task：{},rejected from：{}", r.toString(), executor.toString());
            // 直接执行被拒绝的任务，JVM另起线程执行
            r.run();
        }
    });

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Listener> listenerMap = applicationContext.getBeansOfType(Listener.class);
        for(Map.Entry<String, Listener> entry : listenerMap.entrySet()) {
            addListener(entry.getValue());
        }
    }

    @Override
    public void addListener(Listener<?> listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(Listener<?> listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void multicastEvent(Event event) {
        System.out.println("22222222");
        multicastEvent(event, true);
    }

    /**
     *是否异步，默认为true;
     */
    public void multicastEvent(Event event, boolean sync) {
        Set<Listener> listeners = getInterestedListener(event.getClass());
        System.out.println("******* listeners.size:" + listeners.size());
        if(sync) {
            for(final Listener listener : listeners) {
                EXECUTOR.execute(new Runnable() {
                    @Override
                    public void run() {
                        listener.onEvent(event);
                    }
                });
            }
        }else {
            for(final Listener listener : listeners) {
                listener.onEvent(event);
            }
        }
    }

    private Set<Listener> getInterestedListener(Class eventClass) {
        //存放对事件感兴趣的监听器
        Set<Listener> interestedListeners = new LinkedHashSet<>();
        for(Listener listener : listeners) {
            ParameterizedType parameterizedType = (ParameterizedType) AopTargetUtil.getTarget(listener).getClass().getGenericInterfaces()[0];
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            for(Type type : actualTypeArguments) {
                if(StringUtils.equals(eventClass.getName(), ((Class)type).getName())) {
                    interestedListeners.add(listener);
                    break;
                }
            }
        }
        return interestedListeners;
    }
}
