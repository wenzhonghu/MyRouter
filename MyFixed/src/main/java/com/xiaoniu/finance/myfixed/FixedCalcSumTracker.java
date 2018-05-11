package com.xiaoniu.finance.myfixed;

import android.content.Context;
import android.os.Bundle;

import com.xiaoniu.finance.annotation.router.Param;
import com.xiaoniu.finance.annotation.router.Router;
import com.xiaoniu.finance.router.core.XnAbstractTrack;
import com.xiaoniu.finance.router.result.XnRouterResult;

/**
 * Created by wenzhonghu on 2018/4/27.
 */
@Router(value = "/fixed/sumfixed")
public class FixedCalcSumTracker extends XnAbstractTrack {
    @Param("requestCode")
    private int requestCode;


    @Override
    public  XnRouterResult fire(Context context, Bundle requestData) {
        int sum = requestData.getInt("count",0);
        int r = 0;
        for(int i =0;i<sum;i++){
            r += i;
        }
        //模拟其他业务操作
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return XnRouterResult.ok(r);
    }
}
