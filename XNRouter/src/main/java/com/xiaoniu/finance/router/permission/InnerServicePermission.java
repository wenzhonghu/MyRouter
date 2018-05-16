package com.xiaoniu.finance.router.permission;

/**
 * Created by wenzhonghu on 2018/5/16.
 */

public class InnerServicePermission extends Permission {
    @Override
    public PermissionType getPermission() {
        return PermissionType.INNER_SERVICE;
    }

    @Override
    public PermissionType getParentPermission() {
        return PermissionType.INNER;
    }
}
