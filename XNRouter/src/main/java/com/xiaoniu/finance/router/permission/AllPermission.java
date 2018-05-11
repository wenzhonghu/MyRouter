package com.xiaoniu.finance.router.permission;

/**
 * Created by wenzhonghu on 2018/4/28.
 */

public class AllPermission  implements Permission{

    @Override
    public PermissionType getPermission() {
        return PermissionType.ALL;
    }

    @Override
    public PermissionType getParentPermission() {
        return PermissionType.UNKNOWN;
    }

    @Override
    public PermissionType[] getChildPermission() {
        return PermissionType.values();
    }

    @Override
    public void parsePermission() {

    }
}
