package com.xiaoniu.finance.router.permission;

/**
 * Created by wenzhonghu on 2018/5/16.
 */

public class NothingPermission extends Permission {
    @Override
    public PermissionType getPermission() {
        return PermissionType.UNKNOWN;
    }

    @Override
    public PermissionType getParentPermission() {
        return PermissionType.UNKNOWN;
    }

    @Override
    public boolean parsePermission(PermissionType requestPermission) {
        return  false;
    }
}
