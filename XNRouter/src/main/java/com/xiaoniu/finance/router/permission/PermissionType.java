package com.xiaoniu.finance.router.permission;

/**
 * Created by wenzhonghu on 2018/4/27.
 * 权限控制
 * 未来可以细分力度到具体的组件上<br/>
 * 目前通过group+perm(最后一位模)实现权限控制
 * eg:0x10000 = 1000(group)+0(perm,最小值为权限最大)
 */

public enum PermissionType {
    ALL(0x00000, "全局总权限"),

    OUTER(0x10000, "外部总权限"),
    OUTER_URL(0x10001, "外部Web权限"),
    OUTER_ACTIVITY(0x10002, "外部Activity权限"),

    MODULE(0x20000, "模块间权限"),
    MODULE_URL(0x20001, "模块间URL权限"),
    MODULE_ACTIVITY(0x20002, "模块间Activity权限"),
    MODULE_SERVICE(0x20003, "模块间Service权限"),
    MODULE_BROADCAST(0x20004, "模块间Broadcast权限"),
    MODULE_CONTENT_PROVIDER(0x20005, "模块间ContentProvider权限"),

    INNER(0x30000, "模块内部总权限"),
    INNER_URL(0x30001, "模块URL权限"),
    INNER_ACTIVITY(0x30002, "模块Activity权限"),
    INNER_SERVICE(0x30003, "模块Service权限"),
    INNER_BROADCAST(0x30004, "模块Broadcast权限"),
    INNER_CONTENT_PROVIDER(0x30005, "模块ContentProvider权限"),

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

    /**
     * 获取权限组的字符串
     *
     * @return
     */
    public String getGroupPermission() {
        String x = Integer.toString(permission, 16);
        int length = x.length();
        return x.substring(0, length - 1);
    }

    /**
     * 获取权限模的字符串
     *
     * @return
     */
    public String getModePermission() {
        String x = Integer.toString(permission, 16);
        int length = x.length();
        return x.substring(length - 1, length);
    }
}
