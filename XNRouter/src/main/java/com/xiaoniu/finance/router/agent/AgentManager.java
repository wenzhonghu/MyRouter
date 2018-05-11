package com.xiaoniu.finance.router.agent;

import com.xiaoniu.finance.router.result.XnRouterResult;

/**
 *  结果代理处理中心
 * Created by wenzhonghu on 2018/5/11.
 */

public final class AgentManager {
    private volatile static AgentManager sInstance = null;
    /**
     * 单例模式,多个调用下只有一个单例
     *
     * @return
     */
    public static synchronized AgentManager getInstance() {
        if (sInstance == null) {
            synchronized (AgentManager.class) {
                if (sInstance == null) {
                    sInstance = new AgentManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 结果代理处理中心
     * //Todo
     * @param result
     */
    public void agent(XnRouterResult result){

    }
}
