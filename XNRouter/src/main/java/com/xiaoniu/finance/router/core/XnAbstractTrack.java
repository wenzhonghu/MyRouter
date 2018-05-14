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
     * return XnRouterResult.ok(new FundHomeFragment())
     *
     * @param context
     * @param requestData 请求参数组
     * @return
     */
    public abstract XnRouterResult fire(Context context, Bundle requestData);

    /**
     * 业务调用,支持参数传递同时提供对象互相传递
     * 只有当XnRouter在调用了object(对象object)才会被调用此方法
     * eg:
     * if (object instanceof Test) { //此处为调用方传递过来的对象引用
     * ((Test) object).setText("The object is param.");
     * Test returnTest = new Test("new test");
     * return XnRouterResult.ok(returnTest);//回调给调用方的对象引用
     * }
     *
     * @param context
     * @param requestData 请求参数组
     * @param object      传递对应的对象过去,不支持跨进程对称传递
     * @return
     */
    public XnRouterResult fire(Context context, Bundle requestData, Object object) {
        return XnRouterResult.dont();
    }

}
