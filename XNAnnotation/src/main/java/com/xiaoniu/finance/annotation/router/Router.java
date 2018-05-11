package com.xiaoniu.finance.annotation.router;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wenzhonghu on 2018/4/27.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Router {
    String value();
    boolean enable() default true;
    int permission() default 0;
    boolean crossable() default false;
    //String expression() default "";
    boolean match() default true;
}
