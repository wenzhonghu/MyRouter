package com.xiaoniu.finance.router.rule;

import android.content.Context;

import com.xiaoniu.finance.router.XnRouter;
import com.xiaoniu.finance.router.core.XnAbstractTrack;
import com.xiaoniu.finance.router.core.XnRouterException;
import com.xiaoniu.finance.router.core.XnRouterRule;
import com.xiaoniu.finance.router.permission.PermissionManager;

import static com.xiaoniu.finance.router.result.XnResultCode.CODE_PERM_DENIED;

/**
 * Created by wenzhonghu on 2018/5/16.
 */

public class RouterPermissionRule extends Rule {
    @Override
    boolean doRule(Context context, XnRouterRule rule) {
        if (PermissionManager.getInstance().parsePermission(rule.mTargetRule.getPermissionType(), rule.mRequestRule.getPermission())) {
            return true;
        }
        if (XnRouter.getInstance().getPermissionDeniedListener() != null) {
            XnRouter.getInstance().getPermissionDeniedListener().onPermissionDenied(context);
        }
        return false;

    }

    @Override
    public XnAbstractTrack invalid(Context context, XnRouterRule r) {
        return new XnRouterException(CODE_PERM_DENIED, "");
    }
}
