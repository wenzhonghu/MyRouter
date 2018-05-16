package com.xiaoniu.finance.router.permission;

/**
 * Created by wenzhonghu on 2018/4/28.
 */

public class ModulePermission extends Permission {
    @Override
    public PermissionType getPermission() {
        return PermissionType.MODULE;
    }

    @Override
    public PermissionType getParentPermission() {
        return PermissionType.ALL;
    }


}
