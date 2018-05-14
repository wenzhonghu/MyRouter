package com.xiaoniu.finance.annotation.router;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实现路由注解模式
 * Created by wenzhonghu on 2018/4/27.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Router {
    /**
     * 路由路径
     * @return
     */
    String value();

    /**
     * 是否禁用
     * @return
     */
    boolean enable() default true;

    /**
     * 权限配置
     * @see(PermissionType)
     * @return
     */
    int permission() default 0;

    /**
     * 是否支持跨进程
     * @return
     */
    boolean crossable() default false;

    /**
     * 路由路径的配置规则
     * @return
     */
    //String expression() default "";

    /**
     * 路由路径的配置开关
     * 默认为true,表示全路径完全匹配
     * 如果false,则可以通过expression参数进行部分匹配操作
     * @return
     */
    boolean match() default true;
}
