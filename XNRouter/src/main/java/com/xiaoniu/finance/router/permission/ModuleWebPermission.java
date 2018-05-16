package com.xiaoniu.finance.router.permission;

/**
 * Created by wenzhonghu on 2018/5/16.
 */

public class ModuleWebPermission extends Permission {
    @Override
    public PermissionType getPermission() {
        return PermissionType.MODULE_URL;
    }

    @Override
    public PermissionType getParentPermission() {
        return PermissionType.MODULE;
    }
}
