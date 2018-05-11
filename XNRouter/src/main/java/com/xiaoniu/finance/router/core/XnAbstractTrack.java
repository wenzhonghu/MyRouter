package com.xiaoniu.finance.router.core;

import android.content.Context;
import android.os.Bundle;

import com.xiaoniu.finance.router.result.XnRouterResult;

import java.util.HashMap;

/**
 * 具体路由操作
 */
public abstract class XnAbstractTrack {
    /**
     * 业务调用,支持参数传递
     * 同时支持单向的对象传递,方法如下
     * new XnRouterResult.Builder().code(CODE_SUCCESS).msg(xxx).object(new FundHomeFragment()).build()
     *
     * @param context
     * @param requestData 请求参数组
     * @return
     */
    public abstract XnRouterResult fire(Context context, Bundle requestData);

    /**
     * 业务调用,支持参数传递同时提供对象互相传递
     * 只有当XnRouter在调用了object(对象)才会被调用此方法
     * eg:
     * if (object instanceof TextView) {
     * ((TextView) object).setText("The text is param me.");
     * Toast returnToast = Toast.makeText(context, "I am returned Toast.", Toast.LENGTH_SHORT);
     * return new XnRouterResult.Builder().code(CODE_SUCCESS).msg(xxx).object(returnToast).build();
     * }
     *
     * @param context
     * @param requestData 请求参数组
     * @param object      传递对应的对象过去
     * @return
     */
    public XnRouterResult fire(Context context, Bundle requestData, Object object) {
        return XnRouterResult.dont();
    }

}
