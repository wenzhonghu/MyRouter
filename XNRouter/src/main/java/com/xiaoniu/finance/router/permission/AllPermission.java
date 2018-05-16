package com.xiaoniu.finance.router.permission;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wenzhonghu on 2018/4/28.
 */

public class AllPermission extends Permission {

    @Override
    public PermissionType getPermission() {
        return PermissionType.ALL;
    }

    @Override
    public PermissionType getParentPermission() {
        return PermissionType.UNKNOWN;
    }

    @Override
    public List<PermissionType> getChildPermission() {
        return Arrays.asList(PermissionType.values());
    }

}
