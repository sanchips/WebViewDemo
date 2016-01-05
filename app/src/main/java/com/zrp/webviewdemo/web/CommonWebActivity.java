package com.zrp.webviewdemo.web;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zrp.webviewdemo.App;
import com.zrp.webviewdemo.R;
import com.zrp.webviewdemo.utils.NetUtils;

/**
 * 软件内通用打开网页的容器页面
 *
 * @author ZRP
 */
public class CommonWebActivity extends FragmentActivity {

    private ProgressBar progress_bar;
    protected CustomFrameLayout customFrameLayout;
    protected TextView titleText, errorTxt;
    protected View refresh;// 刷新按钮
    protected WebView webView;

    protected String url = "";// 网址url
    protected String param = "";// 交互参数，如json字符串

    protected WebChromeClient chromeClient = new WebChromeClient() {
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progress_bar.setVisibility(View.GONE);
                // 判断有无网络
                if (!NetUtils.isAvailable(CommonWebActivity.this)) {
                    customFrameLayout.show(R.id.common_net_error);
                    refresh.setVisibility(View.VISIBLE);
                    refresh.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            webView.loadUrl(url);
                        }
                    });
                } else {
                    // 判断网络请求网址是否有效
                    if (!URLUtil.isValidUrl(url)) {
                        customFrameLayout.show(R.id.common_net_error);
                        errorTxt.setText("无效网址");
                    } else {
                        customFrameLayout.show(R.id.common_web);
                    }
                }
            } else {
                progress_bar.setVisibility(View.VISIBLE);
                progress_bar.setProgress(newProgress);
            }
        }

        // 获取到url打开页面的标题
        public void onReceivedTitle(WebView view, String title) {
            titleText.setText(title);
        }

        // js交互提示
        public boolean onJsAlert(WebView view, String url, String message, android.webkit.JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getIntent().getStringExtra("url");
        param = getIntent().getStringExtra("param");
        setContentView(R.layout.common_web_activity);
        initView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        findViewById(R.id.back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleText = (TextView) findViewById(R.id.title);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        customFrameLayout = (CustomFrameLayout) findViewById(R.id.web_fram);
        customFrameLayout.setList(new int[]{R.id.common_web, R.id.common_net_error});
        refresh = findViewById(R.id.error_btn);
        errorTxt = (TextView) findViewById(R.id.error_txt);

        webView = (WebView) findViewById(R.id.common_web);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        synCookies();//格式化写入cookie，需写在setJavaScriptEnabled之后
        webView.setWebChromeClient(chromeClient);
        webView.setWebViewClient(new WebViewClient() {// 让webView内的链接在当前页打开，不调用系统浏览器
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.addJavascriptInterface(new JavaScriptInterface(), "zrp");
        webView.loadUrl(url);

        new Handler().postDelayed(new Runnable() {//异步传本地数据给网页
            @Override
            public void run() {
                transferDataToWeb(param);
            }
        }, 1000);
    }

    /**
     * CookieManager会将这个Cookie存入该应用程序/data/data/databases/目录下的webviewCookiesChromium.db数据库的cookies表中
     * 需要在当前用户退出登录的时候进行清除
     */
    private void synCookies() {
        String[] split = App.cookie.split(";");
        for (int i = 0; i < split.length; i++) {
            CookieSyncManager.createInstance(CommonWebActivity.this);
            CookieManager.getInstance().setCookie(url, split[i]);
            CookieSyncManager.getInstance().sync();
        }
    }

    /**
     * 回调网页中的脚本接口。
     *
     * @param notify 传给网页的通知内容。
     */
    public void transferDataToWeb(String notify) {
        if (webView != null) {
            webView.loadUrl("javascript:test('" + notify + "')");//web网页中已添加了function test(json)方法
        }
    }

    /**
     * android js交互实现：
     * 1. window.zrp.command("");//在网页的方法中添加该代码获取android内容
     * 2. webView.loadUrl("javascript:test('param')");//android给网页传值，须异步执行
     */
    public class JavaScriptInterface {

        @JavascriptInterface
        public void command(String jsonString) {
            if (TextUtils.isEmpty(jsonString)) {
                return;
            }
            //根据网页交互回传的json串进行操作。可以将其传递给外部页面进行处理增加灵活性
            Toast.makeText(CommonWebActivity.this, jsonString, Toast.LENGTH_LONG).show();
        }
    }
}
