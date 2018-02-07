package com.ryanliu.hw9_v13;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = "DetailsActivity";
    private SectionsPageAdapter mSectionsPageAdapter;
    private CustomViewPager mViewPager;
    public Stock details_stock;
    public news_type mNews_type;
    private Toolbar mActionBar;
    public ProgressBar progressBar_current;
    public ProgressBar progressBar_news;
    public NonScrollListView detail_list;
    public ListView news_list;
    public ArrayList<String> detail_listItems = new ArrayList<String>();
    public ArrayList<info_object> detail_info_list_items = new ArrayList<info_object>();
    public ArrayAdapter<String> detail_list_adapter;
    public ArrayList<news_type> mNews_typeListItems = new ArrayList<news_type>();
    public static String webView_url_hist = "http://cs-server.usc.edu:34468/webview_historical.html?submit=";
    public static String webView_url_indicator = "http://cs-server.usc.edu:34468/webview_indicators.html?ticker=";
    public ImageView mstar;
    public ImageView star123;
    public boolean isFav = false;
    public String message = "nope";
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    public static String URL_sharing="defalut";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //Log.d(TAG,"onCreate:Starting");
        String stock_symbol = getIntent().getStringExtra("symbol_name");


        webView_url_hist = webView_url_hist + stock_symbol;
        webView_url_indicator = "http://cs-server.usc.edu:34468/webview_indicators.html?ticker=";
        webView_url_indicator = webView_url_indicator + stock_symbol;

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(DetailsActivity.this,"You have successfully shared highchart",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(DetailsActivity.this,"You have cancelled shareing progress",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(DetailsActivity.this,"Something wrong with your sharing progress",Toast.LENGTH_LONG).show();
            }
        });


        mActionBar = findViewById(R.id.toolbar_back);
        mActionBar.setTitle(stock_symbol);
        mActionBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_back = new Intent(getBaseContext(), MainActivity.class);
                intent_back.putExtra("isfavorited", message);
                Log.d(TAG, message);
                startActivity(intent_back);
            }
        });
        String details_url = "http://stockchecker.us-west-1.elasticbeanstalk.com/?submit=" + stock_symbol;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, details_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("Error Message")){
                    TextView myerror = (TextView) findViewById(R.id.error_cur);
                    myerror.setVisibility(View.VISIBLE);
                    progressBar_current = (ProgressBar) findViewById(R.id.progressBar_current);
                    progressBar_current.setVisibility(View.GONE);
                }

                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        details_stock = new Stock(jsonObject);
                        Log.d(TAG, "Data_got");
                        detail_info_list_items.add(new info_object("Stock Symbol", details_stock.symbol));
                        detail_info_list_items.add(new info_object("Last Price", String.valueOf(details_stock.last_price)));
                        info_object change_item = new info_object("Change", String.valueOf(details_stock.change) + "(" + String.valueOf(details_stock.change_percent) + "%" + ")");
                        change_item.setChange(details_stock.change);
                        detail_info_list_items.add(change_item);
                        detail_info_list_items.add(new info_object("TimeStamp", details_stock.timestamp));
                        detail_info_list_items.add(new info_object("Open", String.valueOf(details_stock.today_open)));
                        detail_info_list_items.add(new info_object("Close", String.valueOf(details_stock.close)));
                        detail_info_list_items.add(new info_object("Day's Range", details_stock.days_range));
                        detail_info_list_items.add(new info_object("Volume", String.valueOf(details_stock.volume_today)));
                        detail_table_Adapter mdetail_list_table_adapter = new detail_table_Adapter(getBaseContext(), R.layout.custom_detail_table, detail_info_list_items);
                        detail_list = findViewById(R.id.details_list);
                        detail_list.setAdapter(mdetail_list_table_adapter);
                        progressBar_current = (ProgressBar) findViewById(R.id.progressBar_current);
                        progressBar_current.setVisibility(View.GONE);
                        detail_list.setVisibility(View.VISIBLE);


                    /*detail_listItems.add("Stock Symbol   " + details_stock.symbol.toUpperCase());
                    detail_listItems.add("Last Price     " + String.valueOf(details_stock.last_price));
                    detail_listItems.add("Change         " + String.valueOf(details_stock.change) + "(" + String.valueOf(details_stock.change_percent) + "%" + ")");
                    detail_listItems.add("TimeStamp      " + details_stock.timestamp);
                    detail_listItems.add("Open           " + String.valueOf(details_stock.today_open));
                    detail_listItems.add("Close          " + String.valueOf(details_stock.close));
                    detail_listItems.add("Day's Range    " + details_stock.days_range);
                    detail_listItems.add("Volume         " + details_stock.volume_today);
                    detail_list_adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, detail_listItems);
                    detail_list = findViewById(R.id.details_list);
                    detail_list.setAdapter(detail_list_adapter);
                    progressBar_current = (ProgressBar) findViewById(R.id.progressBar_current);
                    progressBar_current.setVisibility(View.GONE);
                    detail_list.setVisibility(View.VISIBLE);*/


                    } catch (Exception e) {
                        TextView myerror = (TextView) findViewById(R.id.error_cur);
                        myerror.setVisibility(View.VISIBLE);
                        progressBar_current = (ProgressBar) findViewById(R.id.progressBar_current);
                        progressBar_current.setVisibility(View.GONE);

                    }

                }
            }//onResponse

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
                //Toast.makeText(DetailsActivity.this,"Volley Time-Out.Please try again.",Toast.LENGTH_LONG).show();
                TextView myerror = (TextView) findViewById(R.id.error_cur);
                myerror.setVisibility(View.VISIBLE);
                progressBar_current = (ProgressBar) findViewById(R.id.progressBar_current);
                progressBar_current.setVisibility(View.GONE);

            }
        }
        );

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToReqQueue(stringRequest, "jreq3");//end of current_part

        String news_url = "http://stockchecker.us-west-1.elasticbeanstalk.com/?News_Query=" + stock_symbol;
        StringRequest stringRequest_news = new StringRequest(Request.Method.GET, news_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("false")){
                    progressBar_news = findViewById(R.id.progressBar_news);
                    progressBar_news.setVisibility(View.GONE);
                    TextView myerr = findViewById(R.id.error_news);
                    myerr.setVisibility(View.VISIBLE);
                }

               else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject channel = jsonObject.getJSONObject("channel");
                        JSONArray jsonArray = channel.getJSONArray("item");
                        int index = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (index > 4) {
                                break;
                            }
                            JSONObject object_temp = jsonArray.getJSONObject(i);
                            String checker = object_temp.getString("link");

                            if (checker.contains("article")) {
                                mNews_type = new news_type(object_temp);
                                mNews_typeListItems.add(mNews_type);
                                index++;
                            }
                        }

                        news_listAdapter news_listAdapter = new news_listAdapter(getBaseContext(), mNews_typeListItems);
                        news_list = findViewById(R.id.news_list);
                        news_list.setAdapter(news_listAdapter);
                        news_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                String BrowserURL = mNews_typeListItems.get(position).url_news;
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(BrowserURL));
                                startActivity(browserIntent);
                            }
                        });
                        progressBar_news = findViewById(R.id.progressBar_news);
                        progressBar_news.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Log.d(TAG, e.toString());
                    }

                }
            }//onResponse

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        }
        );//
        stringRequest_news.setRetryPolicy(new DefaultRetryPolicy(8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToReqQueue(stringRequest_news, "jreq_news");//end of news_part


        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (CustomViewPager) findViewById(R.id.container);
        mViewPager.setPagingEnabled(false);
        setupViewPaper(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        String favornot = getIntent().getStringExtra("favornot");
        if(Objects.equals(favornot, "Y")){
            isFav=true;
            //star123 = (ImageView) findViewById(R.id.facebook_icon);
            //star123.setVisibility(View.GONE);
        }

    }

    private void setupViewPaper(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new current(), "CURRENT");
        adapter.addFragment(new historical(), "HISTORICAL");
        adapter.addFragment(new news(), "NEWS");
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
    }


    public void getFavClick(View view) {
        Log.d(TAG, details_stock.symbol.toUpperCase());
        mstar = findViewById(R.id.favStar);
        if (!isFav) {
            mstar.setImageResource(R.drawable.filled);
            saveFavInfo(view);
            isFav = true;
            message = "yup";

        } else {
            mstar.setImageResource(R.drawable.empty);
            isFav = false;
            message = "nope";
        }
    }

    public void saveFavInfo(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("fav_stock", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("stock_symbol", details_stock.symbol);
        editor.putString("stock_price", String.valueOf(details_stock.last_price));
        editor.putString("stock_change", String.valueOf(details_stock.change));
        editor.putString("stock_change_percent", String.valueOf(details_stock.change_percent));
        editor.apply();

    }


    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    public void facebook_share_clicked(View view) {
        Log.d(TAG, "facebookclick!!!!1");
        WebView mywebV = findViewById(R.id.indicator_webView);
        mywebV.getSettings().setJavaScriptEnabled(true);
        MyWebChromeClient myWebChromeClient = new MyWebChromeClient();
        mywebV.setWebChromeClient(myWebChromeClient);
        mywebV.loadUrl("javascript:alert(URL_report())");
        Handler mTimerHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(URL_sharing))
                            .build();
                    shareDialog.show(content);

                }


            }
        };
        mTimerHandler.postDelayed(runnable,300);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d("LogTag", message);
            URL_sharing=message;
            result.confirm();
            return true;
        }
    }

}


