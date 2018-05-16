package com.xiaoniu.finance.router.core;

import com.xiaoniu.finance.annotation.model.RouteMeta;
import com.xiaoniu.finance.router.permission.PermissionType;

/**
 * 业务操作项
 */
public final class XnRouteMeta extends RouteMeta {
    public XnAbstractTrack action;

    public XnRouteMeta(String url, XnAbstractTrack action) {
        this(url, false, PermissionType.UNKNOWN.getPermission(), false, true, action);
    }

    public XnRouteMeta(String url, boolean enable, int permissionType, boolean crossable, boolean match, XnAbstractTrack action) {
        super(url, enable, permissionType, crossable, match);
        this.action = action;
    }

    public static XnRouteMeta exceptionActionCtrl(String url) {
        return new XnRouteMeta(url, new XnRouterException());
    }

    public static XnRouteMeta exceptionActionCtrl(String url, int code, String msg) {
        return new XnRouteMeta(url, new XnRouterException(code, msg));
    }
}
