package com.xiaoniu.finance.router.permission;

/**
 * Created by wenzhonghu on 2018/4/28.
 */

public class PermissionManager {
    private volatile static PermissionManager sInstance = null;

    /**
     * 单例模式,多个调用下只有一个单例
     *
     * @return
     */
    public static synchronized PermissionManager getInstance() {
        if (sInstance == null) {
            synchronized (PermissionManager.class) {
                if (sInstance == null) {
                    sInstance = new PermissionManager();
                }
            }
        }
        return sInstance;
    }

    /**
     *
     * @param targetPermission
     * @param requestPermission
     * @return
     */
    public boolean parsePermission(int targetPermission, int requestPermission){
        Permission permission = strategy(targetPermission);
        return permission.parsePermission(PermissionType.parse(requestPermission));
    }

    private Permission strategy(int targetPermission){
        if(PermissionType.ALL.getPermission() == targetPermission){
            return new AllPermission();
        }else if(PermissionType.OUTER.getPermission() == targetPermission){
            return new OuterPermission();
        }else if(PermissionType.OUTER_ACTIVITY.getPermission() == targetPermission){
            return new OuterActivityPermission();
        }else if(PermissionType.OUTER_URL.getPermission() == targetPermission){
            return new OuterWebPermission();
        }else if(PermissionType.MODULE.getPermission() == targetPermission){
            return new ModulePermission();
        }else if(PermissionType.MODULE_ACTIVITY.getPermission() == targetPermission){
            return new ModuleActivityPermission();
        }else if(PermissionType.MODULE_BROADCAST.getPermission() == targetPermission){
            return new ModuleBroadcastPermission();
        }else if(PermissionType.MODULE_CONTENT_PROVIDER.getPermission() == targetPermission){
            return new ModuleContentProviderPermission();
        }else if(PermissionType.MODULE_SERVICE.getPermission() == targetPermission){
            return new ModuleServicePermission();
        }else if(PermissionType.MODULE_URL.getPermission() == targetPermission){
            return new ModuleWebPermission();
        }else {
            //其他所有情况则权限被禁用
            return new NothingPermission();
        }
    }
}
