package com.xiaoniu.finance.router.rule;

import android.content.Context;

import com.xiaoniu.finance.router.core.XnAbstractTrack;
import com.xiaoniu.finance.router.core.XnRouterException;
import com.xiaoniu.finance.router.core.XnRouterRule;

import static com.xiaoniu.finance.router.result.XnResultCode.CODE_INVALID;

/**
 * Created by wenzhonghu on 2018/5/16.
 */

public class RouterCrossableRule extends Rule {
    @Override
    boolean doRule(Context context, XnRouterRule rule) {
        return rule.mTargetRule.isCrossable();
    }

    @Override
    public XnAbstractTrack invalid(Context context, XnRouterRule r) {
        return new XnRouterException(CODE_INVALID, "");
    }
}
