package com.xiaoniu.finance.annotation.conf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wenzhonghu on 2018/4/27.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Conf {
    /**
     * 文件路径
     * @return
     */
    String value();

    /**
     * 是否缓存
     * @return
     */
    boolean cacheable() default true;

    /**
     * 使用那种获取算法,LRU,LFLO
     * @return
     */
    int algorithm() default 0;
}
