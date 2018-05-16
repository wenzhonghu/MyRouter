package com.xiaoniu.finance.router.permission;

/**
 * Created by wenzhonghu on 2018/5/16.
 */

public class InnerBroadcastPermission extends Permission {
    @Override
    public PermissionType getPermission() {
        return PermissionType.INNER_BROADCAST;
    }

    @Override
    public PermissionType getParentPermission() {
        return PermissionType.INNER;
    }
}
