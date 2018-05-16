package com.xiaoniu.finance.router.rule;

import android.content.Context;

import com.xiaoniu.finance.router.core.XnAbstractTrack;
import com.xiaoniu.finance.router.core.XnRouterRule;

import java.util.ArrayList;
import java.util.List;

/**
 * 规则处理器
 * 如果规则符合则一直执行,一旦异常则退出
 * Created by wenzhonghu on 2018/5/16.
 */

public final class RuleContext {
    private Context mContext;
    private List<Rule> rules;

    public RuleContext(Context context) {
        this.mContext = context;
        rules = new ArrayList<>();
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public XnAbstractTrack request(XnRouterRule r) {
        for (Rule rule : rules) {
            if (!rule.doRule(mContext, r)) {
                return rule.invalid(mContext, r);
            }
        }
        return r.mTargetRule.action;
    }
}
