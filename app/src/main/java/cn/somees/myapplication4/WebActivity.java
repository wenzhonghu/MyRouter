package cn.somees.myapplication4;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.reflect.Field;

import static android.content.ContentValues.TAG;

/**
 * Created by wenzhonghu on 2018/4/27.
 */

public class WebActivity extends Activity {

    protected WebView mWebView;

    public static void startMe(Context context, String title, String url) {
        String appScheme = context.getString(R.string.appScheme);
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Uri uri = Uri.parse(url);
        if (!TextUtils.isEmpty(url) && url.startsWith(appScheme)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setData(uri);
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, WebActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ViewGroup mWebContainer = (ViewGroup) findViewById(R.id.frame_web_container);
        mWebView = onCreateWebView();
        setWeb();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebContainer.addView(mWebView, params);
        setConfigCallback((WindowManager) getSystemService(Context.WINDOW_SERVICE));
    }

    boolean mIsFirstLoadUrl = true;

    private void setWeb() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "shouldOverrideUrlLoading url:" + url);
                //外部Web能跳转到我们的App,内部WebView不能,需要自己跳转
                if (!TextUtils.isEmpty(url) && url.startsWith(view.getContext().getString(R.string.appScheme))) {
                    Log.d(TAG, "shouldOverrideUrlLoading start activity");
//                   if (!IntentSchemeHelper.isEffectivePath(url)){
//                       return false;
//                   }
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setData(uri);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    String isClose = null;
                    try {
                        isClose = uri.getQueryParameter("close");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if ("true".equalsIgnoreCase(isClose)) {
                        finish();
                    }
                    return true;
                } else if (!TextUtils.isEmpty(url) && url.startsWith("tel:")) {
                    //CallDialogUtils.gotoCall(getActivity(),url);
                    return true;
                }
                WebView.HitTestResult hit = view.getHitTestResult();
                if (hit != null) {
                    int hitType = hit.getType();
                    if (!mIsFirstLoadUrl && hitType != WebView.HitTestResult.SRC_ANCHOR_TYPE) {//不是超链接,解决重定向导致不能返回的问题
                        Log.d(TAG, "shouldOverrideUrlLoading return false");
                        return false;
                    }
                    if (!url.contains("xxx")) {
                        return false;
                    }
                }
                if (!url.contains("xxx")) {
                    return false;
                }
                mIsFirstLoadUrl = false;
                String httpPrefix = "http";
                int httpPreLen = httpPrefix.length();
                if (!TextUtils.isEmpty(url) && url.length() > httpPreLen && (url.substring(0, httpPreLen).equalsIgnoreCase(httpPrefix))) {//是网站链接
                    mWebView.loadUrl(url, null);
                    return true;
                }
                if (TextUtils.isEmpty(url)) {
                    return false;
                }
                try {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(uri);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }


    public void setConfigCallback(WindowManager windowManager) {
        try {
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field = field.getType().getDeclaredField("mBrowserFrame");
            field = field.getType().getDeclaredField("sConfigCallback");
            field.setAccessible(true);
            Object configCallback = field.get(null);
            if (null == configCallback) {
                return;
            }
            field = field.getType().getDeclaredField("mWindowManager");
            field.setAccessible(true);
            field.set(configCallback, windowManager);
        } catch (Exception e) {
        }
    }

    protected WebView onCreateWebView() {
        return new WebView(this.getApplicationContext());
    }

}
