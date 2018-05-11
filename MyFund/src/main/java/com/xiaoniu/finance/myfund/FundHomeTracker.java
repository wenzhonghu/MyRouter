package com.xiaoniu.finance.myfund;

import android.content.Context;
import android.os.Bundle;

import com.xiaoniu.finance.annotation.router.Router;
import com.xiaoniu.finance.router.core.XnAbstractTrack;
import com.xiaoniu.finance.router.result.XnRouterResult;

/**
 * Created by wenzhonghu on 2018/4/27.
 */
@Router(value = "/fund/home")
public class FundHomeTracker extends XnAbstractTrack {

    @Override
    public XnRouterResult fire(Context context, Bundle requestData){
        FundActivity.startMe(context,1);
        return XnRouterResult.ok();
    }
}
