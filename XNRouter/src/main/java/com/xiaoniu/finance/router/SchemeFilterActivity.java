package com.xiaoniu.finance.router;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.xiaoniu.finance.router.core.XnRouterRequest;

public class SchemeFilterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**直接通过ARouter处理外部Uri*/
        Uri uri = getIntent().getData();
        XnRouterRequest.Builder builder = new XnRouterRequest.Builder().match(false).build(uri);
        XnRouter.getInstance().from(SchemeFilterActivity.this, builder);
        finish();
    }
}
