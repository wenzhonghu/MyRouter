package com.xiaoniu.finance.router.permission;

/**
 * Created by wenzhonghu on 2018/4/28.
 */

public class OuterPermission extends Permission {
    @Override
    public PermissionType getPermission() {
        return PermissionType.OUTER;
    }

    @Override
    public PermissionType getParentPermission() {
        return PermissionType.ALL;
    }


}
