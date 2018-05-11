package com.xiaoniu.finance.myfixed;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import com.xiaoniu.finance.annotation.router.Param;
import com.xiaoniu.finance.annotation.router.Router;
import com.xiaoniu.finance.router.core.XnAbstractTrack;
import com.xiaoniu.finance.router.result.XnRouterResult;

/**
 * Created by wenzhonghu on 2018/4/27.
 */
@Router(value = "/fixed/fragment")
public class FixedGetFragmentTracker extends XnAbstractTrack {
    @Param("requestCode")
    private int requestCode;


    @Override
    public XnRouterResult fire(Context context, Bundle requestData) {
        Fragment fragment = AccountFragment.newInstance(requestData.getString("one"), requestData.getString("two"));
        return XnRouterResult.ok(fragment);
    }
}
