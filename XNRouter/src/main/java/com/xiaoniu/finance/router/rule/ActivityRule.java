package com.xiaoniu.finance.router.rule;

import android.app.Activity;

/**
 * Created by wenzhonghu on 2018/4/27.
 */

public class ActivityRule extends IntentRule<Activity> {
    @Override
    protected void throwException(String pattern) {
        throw new RuntimeException(pattern);
    }
}
