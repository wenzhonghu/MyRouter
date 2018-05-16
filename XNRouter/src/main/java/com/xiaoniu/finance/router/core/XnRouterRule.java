package com.xiaoniu.finance.router.core;

/**
 * Created by wenzhonghu on 2018/5/16.
 * 规则参数(规则请求源和目标规则)
 * 目的主要用于实现规则匹配系统
 *
 */

public final class XnRouterRule {
    public final XnRouteMeta mTargetRule;
    public final XnRouterRequest mRequestRule;

    public XnRouterRule(XnRouteMeta mTargetRule, XnRouterRequest mRequestRule) {
        this.mTargetRule = mTargetRule;
        this.mRequestRule = mRequestRule;
    }
}
