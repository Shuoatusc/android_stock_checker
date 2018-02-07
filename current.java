package com.ryanliu.hw9_v13;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;
import com.facebook.FacebookSdk;

public class current extends Fragment implements View.OnClickListener{
    //private String[] toppings = {"Cheese", "Pepperoni", "Black Olives"};
    private static final String TAG ="currentlActivity";
    public String selected_indicator="Price";
    Spinner mSpinner;
    ArrayAdapter<CharSequence> mAdapter;
    private WebView mWebView;
    public Button mbutton;
    public int position_now=10;
    public current() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG,"cur");


        View view = inflater.inflate(R.layout.fragment_current, container, false);

        mSpinner =(Spinner) view.findViewById(R.id.spinner);
        mAdapter=ArrayAdapter.createFromResource(getContext(),R.array.indicator_names,android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
        mWebView=(WebView) view.findViewById(R.id.indicator_webView);
        mWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mbutton = (Button) view.findViewById(R.id.button_change);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("check_postion",String.valueOf(position_now));
                if(position_now!=position){
                mbutton.setTextColor(Color.BLACK);
                mbutton.setClickable(true);}
                else{
                    mbutton.setTextColor(Color.GRAY);
                    mbutton.setClickable(false);
                }

                switch (position) {
                    case 0: selected_indicator="Price";
                            break;

                    case 1: selected_indicator="SMA";
                            break;

                    case 2: selected_indicator="EMA";
                            break;

                    case 3: selected_indicator="STOCH";
                            break;

                    case 4: selected_indicator="RSI";
                            break;

                    case 5: selected_indicator="ADX";
                            break;

                    case 6: selected_indicator="CCI";
                            break;

                    case 7: selected_indicator="BBANDS";
                            break;

                    case 8: selected_indicator="MACD";
                            break;

                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        mbutton.setOnClickListener(this);





        return view;

    }//onCreateView

    @SuppressLint("JavascriptInterface")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
      //Log.d(TAG,selected_indicator);
        Button mbutton = (Button) v.findViewById(R.id.button_change);
        position_now=mSpinner.getSelectedItemPosition();
        mbutton.setClickable(false);
        mbutton.setTextColor(Color.GRAY);
        Log.d("URLTEST",DetailsActivity.webView_url_indicator+"&indicator="+selected_indicator);

        mWebView.loadUrl((DetailsActivity.webView_url_indicator+"&indicator="+selected_indicator).trim());
        //DetailsActivity.webView_url_indicator="http://cs-server.usc.edu:34468/webview_indicators.html?ticker=";
        Log.d(TAG,DetailsActivity.webView_url_indicator+"&indicator="+selected_indicator);


    }//onClick




}
