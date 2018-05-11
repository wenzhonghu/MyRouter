package com.xiaoniu.finance.router.permission;

import java.util.List;

/**
 * Created by wenzhonghu on 2018/4/28.
 */

public interface Permission {
    /**
     * 获取当前权限
     * @return
     */
    PermissionType getPermission();

    /**
     * 获取父权限
     * @return
     */
    PermissionType  getParentPermission();

    /**
     * 获取子权限
     * @return
     */
    PermissionType[] getChildPermission();
    /**
     * 解析权限
     */
    void parsePermission();
}
