package com.xiaoniu.finance.router.result;


import static com.xiaoniu.finance.router.result.XnResultCode.CODE_NOT_FOUND;
import static com.xiaoniu.finance.router.result.XnResultCode.CODE_NOT_IMPLEMENT;
import static com.xiaoniu.finance.router.result.XnResultCode.CODE_SUCCESS;

/**
 * 业务操作反馈的结果,其设计对应小牛网络层的架构
 * 后续可以通过网络方式后端控制业务操作
 */
public class XnRouterResult {
    private final int code;
    private final String msg;
    private final String data;
    private final Object object;

    private XnRouterResult(Builder builder) {
        this.code = builder.mCode;
        this.msg = builder.mMsg;
        this.data = builder.mData;
        this.object = builder.mObject;
    }

    public Object getObject() {
        return object;
    }

    public String getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     *
     * @return
     */
    public static XnRouterResult dont(){
       return new XnRouterResult.Builder().code(CODE_NOT_IMPLEMENT).msg("no implemented").build();
    }

    /**
     * 路由成功
     * @return
     */
    public static XnRouterResult ok(){
        return ok(null);
    }

    /**
     * 路由成功
     * @param param 传递的对象参数
     * @return
     */
    public static XnRouterResult ok(Object param){
        return new XnRouterResult.Builder().code(CODE_SUCCESS).msg(null).object(param).build();
    }

    public static class Builder {
        private int mCode;
        private String mMsg;
        private Object mObject;
        private String mData;

        public Builder() {
            mCode = CODE_NOT_FOUND;
            mMsg = "";
            mObject = null;
            mData = null;
        }

        public Builder code(int code) {
            this.mCode = code;
            return this;
        }

        public Builder msg(String msg) {
            this.mMsg = msg;
            return this;
        }

        public Builder data(String data) {
            this.mData = data;
            return this;
        }

        public Builder object(Object object) {
            this.mObject = object;
            return this;
        }

        public XnRouterResult build() {
            return new XnRouterResult(this);
        }
    }


}
