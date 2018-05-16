package com.xiaoniu.finance.router.permission;

/**
 * Created by wenzhonghu on 2018/5/16.
 */

public class ModuleServicePermission extends Permission {
    @Override
    public PermissionType getPermission() {
        return PermissionType.MODULE_SERVICE;
    }

    @Override
    public PermissionType getParentPermission() {
        return PermissionType.MODULE;
    }
}
