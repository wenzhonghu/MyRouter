package com.xiaoniu.finance.router.result;

/**
 * 路由结果编码
 * Created by wenzhonghu on 2018/5/10.
 */
public final class XnResultCode {
    private XnResultCode(){}

    public static final int CODE_SUCCESS = 0x0000;
    public static final int CODE_NOT_FOUND = 0X0001;
    public static final int CODE_INVALID = 0X0002;
    public static final int CODE_PERM_DENIED = 0X0003; //权限不足
    public static final int CODE_NOT_IMPLEMENT = 0X0004;
}
