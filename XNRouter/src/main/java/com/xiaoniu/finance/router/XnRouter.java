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
import com.xiaoniu.finance.router.core.XnRouterRule;
import com.xiaoniu.finance.router.permission.PermissionManager;
import com.xiaoniu.finance.router.result.XnResultCode;
import com.xiaoniu.finance.router.result.XnRouterResult;
import com.xiaoniu.finance.router.rule.RuleManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.xiaoniu.finance.router.result.XnResultCode.CODE_NOT_FOUND;

/**
 * XnRouter中转器
 */
public class XnRouter {
    private static final String TAG = "XnRouter";

    private volatile static XnRouter sInstance = null;
    private ConcurrentHashMap<String, XnRouteMeta> mTracks = null;

    private XnRouter() {
        mTracks = new ConcurrentHashMap<>();
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
        XnAbstractTrack targetTrace = findAndRouterRule(context, routerRequest);
        if (targetTrace == null) {
            String error = String.format("%s dont find XnAbstractTrack, pls use @Router inject it", routerRequest.getPath());
            return XnRouterResponse.build(CODE_NOT_FOUND, error, null);
        }
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
    private XnAbstractTrack findAndRouterRule(Context context, XnRouterRequest routerRequest) {
        /**匹配查询XnRouteMeta*/
        XnRouteMeta targetRouteMeta = findRouteMeta(routerRequest);
        if(targetRouteMeta == null){
            return  null;
        }
        XnRouterRule rule = new XnRouterRule(targetRouteMeta, routerRequest);
        return RuleManager.getInstance().doRule(context,rule);

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

    /**
     * 获取权限不足情况下的回调
     */
    public PermissionDeniedListener getPermissionDeniedListener() {
        return mDeniedListener;
    }

    public interface PermissionDeniedListener {
        void onPermissionDenied(final Context context);
    }
}
