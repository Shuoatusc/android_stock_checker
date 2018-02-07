package com.ryanliu.hw9_v13;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.Date;


public class historical extends Fragment {
    private static final String TAG ="historicalActivity";

    private WebView mWebView;
    public historical() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historical, container, false);
        mWebView = (WebView) view.findViewById(R.id.historical_webview);
        mWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl(DetailsActivity.webView_url_hist);
        DetailsActivity.webView_url_hist="http://cs-server.usc.edu:34468/webview_historical.html?submit=";
        return view;
    }


}
