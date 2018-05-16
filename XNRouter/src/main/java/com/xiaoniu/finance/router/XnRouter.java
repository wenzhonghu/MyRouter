package com.xiaoniu.finance.router;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.xiaoniu.finance.router.agent.AgentManager;
import com.xiaoniu.finance.router.core.XnAbstractTrack;
import com.xiaoniu.finance.router.core.XnRouteMeta;
import com.xiaoniu.finance.router.core.XnRouterRequest;
import com.xiaoniu.finance.router.core.XnRouterResponse;
import com.xiaoniu.finance.router.permission.PermissionManager;
import com.xiaoniu.finance.router.result.XnResultCode;
import com.xiaoniu.finance.router.result.XnRouterResult;

import java.util.HashMap;
import java.util.Map;

import static com.xiaoniu.finance.router.result.XnResultCode.CODE_INVALID;
import static com.xiaoniu.finance.router.result.XnResultCode.CODE_PERM_DENIED;

/**
 * XnRouter中转器
 */
public class XnRouter {
    private static final String TAG = "XnRouter";

    private volatile static XnRouter sInstance = null;
    private HashMap<String, XnRouteMeta> mTracks = null;

    private XnRouter() {
        mTracks = new HashMap<>();
    }

    /**
     * 单例模式,多个调用下只有一个单例
     *
     * @return
     */
    public static synchronized XnRouter getInstance() {
        if (sInstance == null) {
            synchronized (XnRouter.class) {
                if (sInstance == null) {
                    sInstance = new XnRouter();
                }
            }
        }
        return sInstance;
    }

    /**
     * 注册模块
     *
     * @param moduleName
     * @param module
     */
    public void registerModule(String moduleName, XnRouteMeta module) {
        mTracks.put(moduleName, module);
    }

    /**
     * 路由跳转
     *
     * @param context
     * @param builder
     * @return
     * @throws Exception
     */
    public XnRouterResponse from(Context context, @NonNull XnRouterRequest.Builder builder) {
        return from(context, builder.build());
    }

    /**
     * 路由跳转
     *
     * @param context
     * @param routerRequest
     * @return
     * @throws Exception
     */
    private XnRouterResponse from(Context context, @NonNull XnRouterRequest routerRequest) {
        //long starttime = System.currentTimeMillis();
        /**
         * 1/查询目标XnAbstractTrack
         */
        XnAbstractTrack targetTrace = findRequestTrack(context, routerRequest);
        /**
         * 2/组装参数
         */
        Object attachment = routerRequest.getAndClearObject();
        Bundle data = (Bundle) routerRequest.getData().clone();
        //Log.d(TAG, "find Track start: " + System.currentTimeMillis());
        /**使用完毕回收到对象池中*/
        //Log.e(TAG, "obtain="+routerRequest.toString());
        routerRequest.recycle();
        //Log.e(TAG, "recycle="+routerRequest.toString());
        //Log.d(TAG, "find Track end: " + System.currentTimeMillis());
        /**
         * 3/同步调用直接返回结果
         */
        XnRouterResult result = attachment == null ? targetTrace.fire(context, data) : targetTrace.fire(context, data, attachment);
        if (result == null) {
            result = new XnRouterResult.Builder().code(XnResultCode.CODE_INVALID).msg("").object(null).build();
        }
        /**
         * 4/拦截结果
         */
        agentResult(result);
        /**
         * 5/返回调用响应结果
         */
        XnRouterResponse routerResponse = XnRouterResponse.build(result.getCode(), result.getMsg(), result.getObject());
        //Log.d(TAG, "end: " + (System.currentTimeMillis() -starttime));
        return routerResponse;
    }

    private void agentResult(XnRouterResult result) {
        AgentManager.getInstance().agent(result);
    }

    /**
     * 查询目标action
     *
     * @param context
     * @param routerRequest
     * @return
     */
    private XnAbstractTrack findRequestTrack(Context context, XnRouterRequest routerRequest) {
        /**
         * 0.匹配查询XnRouteMeta
         */
        XnRouteMeta targetAction = findRouteMeta(routerRequest);

        /**
         * 1.如果未查询到action则异常出去
         */
        if (null == targetAction) {
            return XnRouteMeta.exceptionActionCtrl(routerRequest.getPath()).action;
        }
        /**
         * 2.如果是禁用则异常出去
         */
        if (!targetAction.isEnable()) {
            return XnRouteMeta.exceptionActionCtrl(routerRequest.getPath(), CODE_INVALID, "invalid").action;
        }
        /**
         * 3./如果是权限不足则异常出去
         */
        if (!parsePermission(context, targetAction.getPermissionType(), routerRequest.getPermission())) {
            return XnRouteMeta.exceptionActionCtrl(routerRequest.getPath(), CODE_PERM_DENIED, "permissionType denied").action;
        }
        /**
         * 4/如果是支持跨进程则对象跨进程处理
         * todo
         */
//        if(targetAction.crossable){
//
//        }
        return targetAction.action;
    }

    private XnRouteMeta findRouteMeta(XnRouterRequest routerRequest) {
        if (routerRequest.isMatch()) {
            return mTracks.get(routerRequest.getPath());
        } else {
            String key = findMatchKey(routerRequest);
            if (key == null) {
                return null;
            }
            return mTracks.get(key);
        }
    }

    private String findMatchKey(XnRouterRequest routerRequest) {
        for (Map.Entry<String, XnRouteMeta> entry : mTracks.entrySet()) {
            if (entry != null && routerRequest.getPath().startsWith(entry.getKey())) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * @param context
     * @param targetPermission
     * @param requestPermission
     * @return
     */
    private boolean parsePermission(final Context context, int targetPermission, int requestPermission) {
        if (PermissionManager.getInstance().parsePermission(targetPermission, requestPermission)) {
            return true;
        }
        if (mDeniedListener != null) {
            mDeniedListener.onPermissionDenied(context);
        }
        return false;
    }

    /**
     * 可以再次扩展,通过设计模式实现权限管理
     *
     * @param targetPermission
     * @param requestPermission
     * @return
     */
    private boolean permissionPatternManager(int targetPermission, int requestPermission) {
        /**
         * 目标权限为ALL则忽略请求权限则通过
         */
        if (targetPermission == 0) {
            return true;
        }
        /**
         * 目标权限为等于请求权限则通过
         */
        if (targetPermission == requestPermission) {
            return true;
        }
        int lr = getFirstInt(targetPermission, requestPermission);
        /**
         * 如果相同,则权限同一级别
         * 如果
         */
        if (lr == 0) {
            if (targetPermission < requestPermission) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断数值的第一个数字的大小
     *
     * @return
     */
    private static int getFirstInt(int left, int right) {
        int l = Integer.parseInt(Integer.toString(left, 16).substring(0, 1));
        int r = Integer.parseInt(Integer.toString(right, 16).substring(0, 1));
        if (l > r) {
            return 1;
        } else if (l == r) {
            return 0;
        } else {
            return -1;
        }
    }

    private PermissionDeniedListener mDeniedListener = new PermissionDeniedListener() {
        @Override
        public void onPermissionDenied(Context context) {
            /** Todo toast implement permision denied,production pls override it*/
            Toast.makeText(context, context.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 添加权限不足情况下的回调
     *
     * @param listener
     */
    public XnRouter setPermissionDeniedListener(PermissionDeniedListener listener) {
        mDeniedListener = listener;
        return this;
    }

    public interface PermissionDeniedListener {
        void onPermissionDenied(final Context context);
    }
}
