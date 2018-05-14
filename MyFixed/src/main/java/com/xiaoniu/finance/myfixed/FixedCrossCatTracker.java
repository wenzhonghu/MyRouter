package com.xiaoniu.finance.myfixed;

import android.content.Context;
import android.os.Bundle;

import com.xiaoniu.finance.annotation.router.Router;
import com.xiaoniu.finance.router.core.XnAbstractTrack;
import com.xiaoniu.finance.router.result.XnRouterResult;

/**
 * Created by wenzhonghu on 2018/4/27.
 */
@Router(value = "/fixed/cross")
public class FixedCrossCatTracker extends XnAbstractTrack {
    @Override
    public  XnRouterResult fire(Context context, Bundle requestData) {
        CrossActivity.startMe(context,requestData.getInt("request"));
        return XnRouterResult.ok();
    }
}
