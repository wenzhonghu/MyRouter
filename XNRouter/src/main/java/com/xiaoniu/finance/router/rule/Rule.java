package com.xiaoniu.finance.router.rule;

import android.content.Context;

import com.xiaoniu.finance.router.core.XnAbstractTrack;
import com.xiaoniu.finance.router.core.XnRouterException;
import com.xiaoniu.finance.router.core.XnRouterRule;

import static com.xiaoniu.finance.router.result.XnResultCode.CODE_NOT_IMPLEMENT;

/**
 * Created by wenzhonghu on 2018/4/26.
 */

public abstract class Rule {
    /**
     * 规则执行器
     * @param context
     * @param rule
     * @return
     */
    abstract boolean doRule(Context context, XnRouterRule rule);

    /**
     * 规则异常处理
     *
     * @param context
     * @param r
     * @return
     */
    public XnAbstractTrack invalid(Context context, XnRouterRule r) {
        return new XnRouterException(CODE_NOT_IMPLEMENT, "");
    }
}
