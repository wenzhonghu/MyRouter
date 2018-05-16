package com.xiaoniu.finance.myfund;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xiaoniu.finance.router.XnRouter;
import com.xiaoniu.finance.router.core.XnRouterRequest;
import com.xiaoniu.finance.router.core.XnRouterResponse;
import com.xiaoniu.finance.router.permission.PermissionType;

public class FundActivity extends Activity {

    public static final int REQ_CODE = 100;
    public static final int REQ_CODE1 = 101;

    public static void startMe(Context context, int code) {
        Intent intent = new Intent(context, FundActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund);
        /**
         * 简单的调用startActivity
         */
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XnRouter.getInstance().setPermissionDeniedListener(new XnRouter.PermissionDeniedListener() {
                    @Override
                    public void onPermissionDenied(Context context) {
                        Toast.makeText(context, "没有权限访问此地址", Toast.LENGTH_SHORT).show();
                    }
                }).from(FundActivity.this, new XnRouterRequest.Builder().build("/fix/home").permission(PermissionType.INNER_ACTIVITY.getPermission()));
            }
        });

        /**
         * 简单的调用其他模块的startActivityForResult
         */
        findViewById(R.id.btn21).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XnRouter.getInstance().from(FundActivity.this, new XnRouterRequest.Builder().build("/fixed/result")
                        .withInt("request", REQ_CODE)
                        .permission(PermissionType.MODULE.getPermission()));
            }
        });

        /**
         * 简单的调用startActivityForResult
         */
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XnRouter.getInstance().from(FundActivity.this, new XnRouterRequest.Builder().build("/fund/result")
                        .withInt("request", REQ_CODE)
                        .permission(PermissionType.MODULE.getPermission()));
            }
        });

        /**
         * 简单的实现调用对方的业务操作(包括进参,入参)
         */
        final EditText et = (EditText) FundActivity.this.findViewById(R.id.count);
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XnRouterResponse response = XnRouter.getInstance().from(FundActivity.this, new XnRouterRequest.Builder().build("/fixed/sumfixed")
                        .withInt("count", Integer.parseInt(et.getText().toString()))
                        .permission(PermissionType.MODULE.getPermission()));
                Toast.makeText(FundActivity.this, (int) response.getObject() + "", Toast.LENGTH_SHORT).show();
            }
        });

        final EditText et1 = (EditText) FundActivity.this.findViewById(R.id.count1);
        findViewById(R.id.btn31).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(et1.getText().toString());
                for(int i = 0 ; i< count; i++){
                    long x = System.currentTimeMillis();
                    XnRouter.getInstance().from(FundActivity.this, new XnRouterRequest.Builder().build("/fixed/sumfixed")
                            .withInt("count", i)
                            .permission(PermissionType.MODULE.getPermission()));
                    Log.e("wzh", "count="+(System.currentTimeMillis() - x));
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //Toast.makeText(FundActivity.this, (int) response.getObject() + "", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 简单的调用对方的对象数据,例如fragment对象
         */
        LinearLayout contrainter = (LinearLayout) findViewById(R.id.contrainter);
        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XnRouterResponse response = XnRouter.getInstance().from(FundActivity.this, new XnRouterRequest.Builder().build("/fixed/fragment")
                        .withString("one", "onesssssssssssssss")
                        .withString("two", "two0000ooooooooooo")
                        .permission(PermissionType.MODULE.getPermission()));
                if (response.parall()) {
                    Fragment fragment = (Fragment) response.getObject();
                    //步骤一：添加一个FragmentTransaction的实例
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    //步骤二：用add()方法加上Fragment的对象rightFragment
                    transaction.add(R.id.contrainter, fragment);
                    //步骤三：调用commit()方法使得FragmentTransaction实例的改变生效
                    transaction.commit();

                }
            }
        });

        /**
         * 简单的调用startActivityForResult
         */
        findViewById(R.id.btn5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XnRouter.getInstance().from(FundActivity.this, new XnRouterRequest.Builder().build("/fixed/cross")
                        .withInt("request", REQ_CODE1));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, data.getStringExtra("wen"), Toast.LENGTH_SHORT).show();
        }

        if (requestCode == REQ_CODE1 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, data.getStringExtra("wen"), Toast.LENGTH_SHORT).show();
        }
    }
}
