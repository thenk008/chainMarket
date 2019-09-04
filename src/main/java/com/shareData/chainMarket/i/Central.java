package com.shareData.chainMarket.i;

import java.lang.annotation.*;


@Target({ElementType.METHOD,ElementType.TYPE}) //注解应用类型(应用到方法的注解,还有类的可以自己试试)
@Retention(RetentionPolicy.RUNTIME) // 注解的类型
public @interface Central {
    String url() default "";
}
