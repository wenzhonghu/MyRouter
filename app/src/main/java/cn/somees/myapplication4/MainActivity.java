package cn.somees.myapplication4;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.xiaoniu.finance.myfixed.routes.FixedRouterManager;
import com.xiaoniu.finance.myfund.routes.FundRouterManager;
import com.xiaoniu.finance.router.XnRouter;
import com.xiaoniu.finance.router.core.XnRouterRequest;

import static android.content.ContentValues.TAG;


public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "ok";
    private static final String TAG1 = "ok1";
    private WebView mWebView;
    private Button mButton;
    private Button mButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FixedRouterManager.setup();
        FundRouterManager.setup();
        mWebView = (WebView) findViewById(R.id.webview);
        mButton = (Button) findViewById(R.id.btn);
        mButton2 = (Button) findViewById(R.id.btn2);
        mButton.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        initWeb();
        supportChrome();
        supportClient();
        addJavascript();
    }

    boolean mIsFirstLoadUrl = true;
    private void supportClient() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e(TAG, "onPageFinished, url=" + url);
                super.onPageFinished(view, url);
                //if(mDialog!=null && mDialog.isShowing())mDialog.hide();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e(TAG, "onPageStarted, url=" + url);
                super.onPageStarted(view, url, favicon);
                //if(mDialog!=null && !mDialog.isShowing())mDialog.show();
            }

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

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //super.onReceivedSslError(view, handler, error);
                handler.proceed();
                Log.e(TAG, "onReceivedSslError, e=" + error.toString());
                //view.loadUrl("file:///android_asset/web.html");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //super.onReceivedError(view, errorCode, description, failingUrl);
                Log.e(TAG, "onReceivedError, e=" + description);
            }


            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                Log.e(TAG, "shouldInterceptRequest, url=" + url);
                if (url.equalsIgnoreCase("http://localhost/qijian.png")) {
                    try {

                        AssetFileDescriptor fd = getAssets().openFd("my.jpg");
                        return new WebResourceResponse("image/png", "UTF-8", fd.createInputStream());
                    } catch (Exception e) {
                        Log.e(TAG, "shouldInterceptRequest, e=" + e);
                    }
                }
                return super.shouldInterceptRequest(view, url);
            }
        });
    }

    private void supportChrome() {
        //设置响应js 的Alert()函数
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                result.confirm();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                Log.e(TAG, "onJsPrompt, msg=" + message);
                //Toast.makeText(TestActivity.this, message, Toast.LENGTH_SHORT).show();
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e(TAG, "onConsoleMessage, msg=" + consoleMessage.message());
                Toast.makeText(MainActivity.this, consoleMessage.message(), Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.e(TAG, "progress:" + newProgress);
                super.onProgressChanged(view, newProgress);
            }

        });
    }

    private void addJavascript() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //mWebView.addJavascriptInterface(new JSBridge(), "android");
        mWebView.loadUrl("file:///android_asset/web1.html");
    }

    private void initWeb() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        //settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //settings.setSupportZoom(true);
        //settings.setDisplayZoomControls(true);
        //settings.setBuiltInZoomControls(true);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn){
            XnRouter.getInstance().from(this, new XnRouterRequest.Builder().build("/fund/home"));
        }
        if(view.getId() == R.id.btn2){
            WebActivity.startMe(this,null,"xnoapp://xno.cn/INVESTMENT_CATEGORY_LIST?type=d&productName=bbb");
        }
    }

    public class JSBridge {
        public void toastMessage(String message) {
            Toast.makeText(getApplicationContext(), "通过Natvie传递的Toast:" + message, Toast.LENGTH_LONG).show();
        }

        public String test() {
            return "test";
        }

        public String toString() {
            return "JSBridge";
        }
    }
}
