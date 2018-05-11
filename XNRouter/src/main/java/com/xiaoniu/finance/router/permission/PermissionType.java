package com.xiaoniu.finance.router.permission;

/**
 * Created by wenzhonghu on 2018/4/27.
 * 权限控制
 * 未来可以细分力度到具体的组件上
 */

public enum PermissionType {
    ALL(0x00000, "全局总权限"),
    OUTER(0x10000, "外部总权限"),
    OUTER_WEB(0x10001, "外部Web权限"),
    OUTER_ACTIVITY(0x10002, "外部Activity权限"),
    INNER(0x20000, "内部总权限"),
    INNER_WEB(0x20001, "内部权限"),
    ACTIVITY(0x20002, "内部权限"),
    SERVICE(0x20002, "内部权限"),
    BROADCAST(0x20002, "内部权限"),
    CONTENTPROVIDER(0x20002, "内部权限"),
    UNKNOWN(0x90000, "未知权限");


    int permission;
    String desc;

    PermissionType(int permission, String desc) {
        this.permission = permission;
        this.desc = desc;
    }

    public static PermissionType parse(int permission) {
        for (PermissionType routeType : PermissionType.values()) {
            if (routeType.getPermission() == permission) {
                return routeType;
            }
        }

        return UNKNOWN;
    }


    public int getPermission() {
        return permission;
    }
}
