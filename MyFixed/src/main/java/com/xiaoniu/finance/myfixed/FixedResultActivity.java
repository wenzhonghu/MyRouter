package com.xiaoniu.finance.myfixed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FixedResultActivity extends Activity {


    public static void startMe(Context context, int code) {
        Intent intent = new Intent(context, FixedResultActivity.class);
        ((Activity) context).startActivityForResult(intent, code);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_result);
        findViewById(R.id.btn323).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                String str1 = "来自定期的startActivityForResult";
                bundle.putString("wen", str1);
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                FixedResultActivity.this.finish();
            }
        });
    }
}
