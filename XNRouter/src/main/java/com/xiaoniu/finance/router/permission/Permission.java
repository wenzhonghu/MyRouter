package com.xiaoniu.finance.router.permission;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenzhonghu on 2018/4/28.
 */

public abstract class Permission {
    /**
     * 获取当前权限
     * @return
     */
    public abstract PermissionType getPermission();

    /**
     * 获取父权限
     * @return
     */
    public abstract PermissionType getParentPermission();

    /**
     * 获取子权限
     * @return
     */
    public List<PermissionType> getChildPermission() {
        List<PermissionType> pts = new ArrayList<>();
        String tmp = getPermission().getGroupPermission();
        for (PermissionType pt : PermissionType.values()) {
            if (tmp.equalsIgnoreCase(pt.getGroupPermission()) &&
                    getPermission().getPermission() != pt.getPermission()) {
                pts.add(pt);
            }
        }
        return pts;
    }
    /**
     * 是否符合权限
     * 要么自己本身权限要么就是子权限,其他情况都是不符合权限条件的
     */
    public boolean parsePermission(PermissionType requestPermission) {
        return requestPermission == this.getPermission() || getChildPermission().contains(requestPermission);
    }
}
