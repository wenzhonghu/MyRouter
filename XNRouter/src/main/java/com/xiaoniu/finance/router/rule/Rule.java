package com.xiaoniu.finance.router.rule;

import android.content.Context;

/**
 * Created by wenzhonghu on 2018/4/26.
 */

public interface Rule<Target,Result> {
    /**
     * 添加路由规则
     * @param pattern
     * @param clazz
     */
    void addRule(final String pattern, final Class<Target> clazz);

    /**
     * 查询路由规则
     * @param pattern
     */
    boolean findRule(final String pattern);

    /**
     * 执行路由规则
     * @param context
     * @param pattern
     * @return
     */
    Result invoke(final Context context, final String pattern);
}
