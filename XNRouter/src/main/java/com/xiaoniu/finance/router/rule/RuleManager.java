package com.xiaoniu.finance.router.rule;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xiaoniu.finance.router.core.XnAbstractTrack;
import com.xiaoniu.finance.router.core.XnRouterRule;

/**
 * Created by wenzhonghu on 2018/4/26.
 */

public class RuleManager {
    private volatile static RuleManager sInstance = null;

    /**
     * 单例模式,多个调用下只有一个单例
     *
     * @return
     */
    public static synchronized RuleManager getInstance() {
        if (sInstance == null) {
            synchronized (RuleManager.class) {
                if (sInstance == null) {
                    sInstance = new RuleManager();
                }
            }
        }
        return sInstance;
    }

    public XnAbstractTrack doRule(Context context, XnRouterRule rule){
        RuleContext ruleContext = addRules(context);
        return ruleContext.request(rule);
    }

    @NonNull
    private RuleContext addRules(Context context) {
        RuleContext ruleContext = new RuleContext(context);
        ruleContext.addRule(new RouterEnableRule());
        ruleContext.addRule(new RouterPermissionRule());
        ruleContext.addRule(new RouterCrossableRule());
        return ruleContext;
    }
}
