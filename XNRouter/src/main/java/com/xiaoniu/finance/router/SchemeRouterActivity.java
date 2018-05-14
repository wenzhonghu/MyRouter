package com.xiaoniu.finance.router;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.xiaoniu.finance.router.core.XnRouterRequest;

/**
 * 处理uri模式的跳转功能
 */
public class SchemeRouterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**直接通过ARouter处理外部Uri*/
        Uri uri = getIntent().getData();
        XnRouterRequest.Builder builder = new XnRouterRequest.Builder().match(false).build(uri);
        XnRouter.getInstance().from(SchemeRouterActivity.this, builder);
        finish();
    }
}
