package com.yuramax.rxjavademo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者：weijun
 * 日期：2019/3/24
 * 作用：
 */
@Target(ElementType.TYPE)//该注解作用在？（TYPE:类，接口上）
@Retention(RetentionPolicy.RUNTIME)//CLASS：编译时预操作（运行时丢失），SOURCE：源码注解，检查性操作（编译和运行时都会丢失），RUNTIME：jvm运行时通过反射获取该注解的值
public @interface initContentView {
    int value();
}
