package com.xiaoniu.finance.router.core;

import com.xiaoniu.finance.router.result.XnResultCode;

/**
 * 路由响应操作
 */
public class XnRouterResponse {

    private int mCode = XnResultCode.CODE_SUCCESS;

    private String mMessage = "";

    private Object mObject;

    private XnRouterResponse() {
    }

    private XnRouterResponse(int code, String message, Object object) {
        this.mCode = code;
        this.mMessage = message;
        this.mObject = object;
    }

    public static XnRouterResponse build(int code, String message, Object object) {
        return new XnRouterResponse(code, message, object);
    }

    /**
     * 获取调用的结果
     * true表示调用成功,false表示调用失败,可以根据业务进行处理
     *
     * @return
     */
    public boolean parall() {
        return this.mCode == XnResultCode.CODE_SUCCESS ? true : false;
    }

    String getMessage() {
        return mMessage;
    }

    /**
     * 如果传递了对象数据过来可以这里获取
     * @return
     */
    public Object getObject() {
        return mObject;
    }

}
