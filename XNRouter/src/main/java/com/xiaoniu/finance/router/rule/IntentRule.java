package com.xiaoniu.finance.router.rule;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;

/**
 * Created by wenzhonghu on 2018/4/27.
 */
public abstract class IntentRule<Target> implements Rule<Target, Intent> {
    private HashMap<String, Class<Target>> mIntentRules ;

    public IntentRule(){
        mIntentRules = new HashMap<>();
    }

    @Override
    public void addRule(String pattern, Class<Target> clazz) {
        mIntentRules.put(pattern,clazz);
    }

    @Override
    public boolean findRule(String pattern) {
        return mIntentRules.get(pattern) != null;
    }

    @Override
    public Intent invoke(Context context, String pattern) {
        Class<Target> clazz = mIntentRules.get(pattern);
        if(clazz == null){
            throwException(pattern);
        }
        return new Intent(context, clazz);
    }

    protected abstract void throwException(String pattern) ;
}
