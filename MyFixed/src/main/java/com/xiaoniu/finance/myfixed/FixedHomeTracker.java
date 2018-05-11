package com.xiaoniu.finance.myfixed;

import android.content.Context;
import android.os.Bundle;

import com.xiaoniu.finance.annotation.router.Param;
import com.xiaoniu.finance.annotation.router.Router;
import com.xiaoniu.finance.router.core.XnAbstractTrack;
import com.xiaoniu.finance.router.result.XnRouterResult;

import java.util.HashMap;

/**
 * Created by wenzhonghu on 2018/4/27.
 */
@Router(value = "/fix/home", permission = 0x20000)
public class FixedHomeTracker extends XnAbstractTrack {

    @Override
    public XnRouterResult fire(Context context, Bundle requestData) {
        TestActivity.startMe(context,1);
        return XnRouterResult.ok();
    }
}
