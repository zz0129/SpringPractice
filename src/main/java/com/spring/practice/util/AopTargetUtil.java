package com.spring.practice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;

/**
 * 通过代理对象获取被代理对象
 */
@Slf4j
public class AopTargetUtil {

    /**
     * 获取 目标对象
     */
    public static Object getTarget(Object proxy) {

        // 不是代理对象,即没有被aop代理，直接返回
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;
        }
        try {
            // cglibProxy
            if (AopUtils.isCglibProxy(proxy)) {
                return getCglibProxyTargetObject(proxy);
                // jdkDynamicProxy
            } else {
                return getJdkDynamicProxyTargetObject(proxy);
            }
        } catch (Exception e) {
            log.error("获取被代理类对象异常：" + e.getMessage(), e);
            return proxy;
        }
    }

    /**
     * CGLIB方式被代理类的获取
     */
    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);

        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();

        return target;
    }

    /**
     * JDK动态代理方式被代理类的获取
     */
    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);

        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();

        return target;
    }
}
