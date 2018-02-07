package com.ryanliu.hw9_v13;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by RyanLiu on 11/28/17.
 */

class MyWebViewClient extends WebViewClient {
    @Override
    public void onPageFinished (WebView view, String url){
        view.loadUrl("javascript:alert(functionThatReturnsSomething())");
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }
}
