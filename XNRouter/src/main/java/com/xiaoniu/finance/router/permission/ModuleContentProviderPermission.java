package com.xiaoniu.finance.router.permission;

/**
 * Created by wenzhonghu on 2018/5/16.
 */

public class ModuleContentProviderPermission extends Permission {
    @Override
    public PermissionType getPermission() {
        return PermissionType.MODULE_CONTENT_PROVIDER;
    }

    @Override
    public PermissionType getParentPermission() {
        return PermissionType.MODULE;
    }
}
